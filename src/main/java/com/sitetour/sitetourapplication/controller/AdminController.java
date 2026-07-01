package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.AuditLog;
import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
import com.sitetour.sitetourapplication.enums.Role;
import com.sitetour.sitetourapplication.repository.AuditLogRepository;
import com.sitetour.sitetourapplication.repository.EmployeeRepository;
import com.sitetour.sitetourapplication.repository.UserRepository;
import com.sitetour.sitetourapplication.service.AuditLogService;
import com.sitetour.sitetourapplication.service.EmployeeService;
import com.sitetour.sitetourapplication.service.TeamService;
import com.sitetour.sitetourapplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.PrintWriter;
import java.util.Comparator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TeamService teamService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final AuditLogRepository auditLogRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    //status filtering for csv export of employee lists
    private String statusLabel(
            InterviewStatus status
    ) {

        return switch (status) {

            case NOT_CONTACTED -> "未連絡";
            case CONTACTING -> "連絡中";
            case AWAITING_REPLY -> "返信待ち";
            case SCHEDULED -> "予定確定";
            case COMPLETED -> "完了";
        };
    }
    // ordering for csv/excel export
    private int statusOrder(
            InterviewStatus status
    ) {

        return switch(status) {

            case NOT_CONTACTED -> 1;
            case CONTACTING -> 2;
            case AWAITING_REPLY -> 3;
            case SCHEDULED -> 4;
            case COMPLETED -> 5;
        };
    }

    public AdminController(TeamService teamService, UserService userService, UserRepository userRepository, AuditLogService auditLogService, AuditLogRepository auditLogRepository, EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.teamService = teamService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.auditLogRepository = auditLogRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @PostMapping("/teams/create")
    public String createTeam(
            @RequestParam String teamName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        //confirm password validation
        if (!password.equals(confirmPassword)) {
            return "redirect:/admin?error=passwordMismatch";
        }
        Team team = teamService.createTeam(teamName);
        try {

            userService.createTeamUser(username, password, team);

        } catch (DataIntegrityViolationException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ログインIDは既に使用されています"
            );

            return "redirect:/admin/teams";
        }

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "TEAM_CREATED",
                "Team="
                        + teamName
                        + " | InitialUser="
                        + username
        );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "チームを参加しました"
        );

        return "redirect:/admin/teams";
    }

    //delete team function
    @PostMapping("/teams/delete")
    public String deleteTeam(@RequestParam Long teamId, RedirectAttributes redirectAttributes) {

        Team team =
                teamService.getTeamById(teamId);

        String details =
                "Deleted team: "
                        + team.getName();

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "TEAM_DELETED",
                details
        );


        teamService.deleteTeam(teamId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "チームを消しました"
        );

        return "redirect:/admin/teams";
    }

    //update notes for team
    @PostMapping("/teams/update-notes")
    public String updateTeamNotes(

            @RequestParam Long teamId,
            @RequestParam String memberNotes,
            RedirectAttributes redirectAttributes

    ) {

        Team team =
                teamService.getTeamById(teamId);

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "TEAM_NOTES_UPDATED",
                "Updated notes for team: "
                        + team.getName()
        );

        teamService.updateTeamNotes(
                teamId,
                memberNotes
        );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "チームメモを変更しました"
        );
        return "redirect:/admin/teams";
    }


    //team rename logic
    @PostMapping("/teams/rename")
    public String renameTeam(
            @RequestParam Long teamId,
            @RequestParam String teamName,
            RedirectAttributes redirectAttributes
    ) {
        Team team =
                teamService.getTeamById(teamId);

        String oldName =
                team.getName();

        teamService.renameTeam(
                teamId,
                teamName
        );

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "TEAM_RENAMED",
                oldName + " -> " + teamName
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "チーム名の変更できました"
        );
        return "redirect:/admin/teams";
    }

    //user login update nav
    @PostMapping("/teams/update-login")
    public String updateLogin(

            @RequestParam Long teamId,
            @RequestParam String username

    ) {

        userService.updateUsername(
                teamId,
                username
        );

        return "redirect:/admin/teams";
    }

    //Add user to team method
    @PostMapping("/teams/add-user")
    public String addUser(

            @RequestParam Long teamId,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes

    ) {

        if (!password.equals(confirmPassword)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ユーザー参加ができませんでした、パスワードが違います"
            );

            return "redirect:/admin/teams";
        }

        try {

            userService.createAdditionalTeamUser(
                    username,
                    password,
                    teamId
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ユーザー名が既に存在します"
            );

            return "redirect:/admin/teams";
        }

        Team team =
                teamService.getTeamById(teamId);

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "TEAM_USER_CREATED",
                "Created user "
                        + username
                        + " for team "
                        + team.getName()
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "ユーザーを追加しました"
        );
        return "redirect:/admin/teams";
    }

    //delete user / admin function
    @PostMapping("/users/delete")
    public String deleteUser(

            @RequestParam Long userId,
            RedirectAttributes redirectAttributes

    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow();

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "USER_DELETED",
                "Deleted user: "
                        + user.getUsername()
                        + " (Team: "
                        + user.getTeam().getName()
                        + ")"
        );

        userService.deleteUser(userId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "ユーザーを消しました"
        );
        return "redirect:/admin/teams";
    }

    //reset individual team user passwords
    @PostMapping("/users/reset-password")
    public String resetUserPassword(

            @RequestParam Long userId,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes

    ) {

        if (!password.equals(confirmPassword)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "パスワードが一致しません"
            );

            return "redirect:/admin/teams";
        }


        User user =
                userRepository.findById(userId)
                        .orElseThrow();

        //protects against unlikely admin deletion
        if (user.getRole() == Role.ADMIN) {

            throw new RuntimeException(
                    "Cannot delete admin user"
            );
        }

        userService.resetUserPassword(
                userId,
                password
        );

        auditLogService.log(
                "ADMIN",
                "SYSTEM",
                "USER_PASSWORD_RESET",
                "Reset password for "
                        + user.getUsername()
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "パスワードを変更しました"
        );

        return "redirect:/admin/teams";
    }



    //admin view audit log page
    @GetMapping("/auditlogs")
    public String auditLogs(

            @RequestParam(required = false)
            String team,

            Model model

    ) {

        List<AuditLog> logs;

        if (team == null || team.isBlank()
                || team.equals("ALL")) {

            logs = auditLogRepository
                    .findAllByOrderByTimestampDesc();

        } else {

            logs = auditLogRepository
                    .findByTeamNameOrderByTimestampDesc(
                            team
                    ); logs.stream()
                    .limit(100)
                    .toList();
        }

        List<String> teams =
                teamService.getAllTeams()
                        .stream()
                        .map(Team::getName)
                        .toList();

        model.addAttribute(
                "logs",
                logs
        );

        model.addAttribute(
                "teams",
                teams
        );

        model.addAttribute(
                "selectedTeam",
                team
        );
        model.addAttribute("isAdmin", true);
        model.addAttribute(
                "selectedTeam",
                team
        );

        return "auditlogs";

    }

    //team view/edit mapping for admin view
    @GetMapping("/teams")
    public String teamManagementPage(Model model) {

        model.addAttribute("teams", teamService.getAllTeams());

        Map<Long, List<User>> teamUsers = new HashMap<>();

        for (Team team : teamService.getAllTeams()) {

            teamUsers.put(
                    team.getId(),
                    userRepository.findAllByTeamId(team.getId())
            );
        }

        model.addAttribute(
                "teamUsers",
                teamUsers
        );
        model.addAttribute("isAdmin", true);

        return "team-management";
    }

    //admindashboard retool
    @GetMapping
    public String adminDashboard(Model model) {

        List<AuditLog> recentLogs =
                auditLogRepository
                        .findAllByOrderByTimestampDesc()
                        .stream()
                        .limit(5)
                        .toList();

        model.addAttribute(
                "recentLogs",
                recentLogs
        );

        List<Team> teams =
                teamService.getAllTeams();

        Map<Long, List<User>> teamUsers =
                new HashMap<>();

        for (Team team : teams) {

            teamUsers.put(
                    team.getId(),
                    userRepository.findByTeamId(
                            team.getId()
                    )
            );
        }

        model.addAttribute(
                "teams",
                teams
        );

        model.addAttribute(
                "teamUsers",
                teamUsers
        );

        model.addAttribute(
                "isAdmin",
                true
        );

        return "admin";
    }

    //admin team schedule view
    @GetMapping("/schedules")
    public String adminSchedules(

            @RequestParam(required = false)
            LocalDate date,

            Model model

    ) {

        if (date == null) {

            date = LocalDate.now();

        }

        List<Employee> interviews =
                employeeService
                        .getScheduledEmployeesByDate(
                                date
                        );
        //helper for html grouping/styling by team
        Map<String, List<Employee>> groupedSchedules =
                interviews.stream()
                        .collect(Collectors.groupingBy(
                                employee ->
                                        employee.getTeam().getName(),
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        model.addAttribute(
                "groupedSchedules",
                groupedSchedules
        );

        model.addAttribute(
                "selectedDate",
                date
        );

        model.addAttribute(
                "interviews",
                interviews
        );
        model.addAttribute("isAdmin", true);

        return "admin-schedules";
    }

    //export employee lists to csv
    @GetMapping("/export/employees")
    public void exportEmployees(

            @RequestParam(required = false)
            Long teamId,

            HttpServletResponse response

    ) throws IOException {

        List<Employee> employees;

        String filename;

        if (teamId == null) {

            employees =
                    employeeService.getAllEmployees();

            filename =
                    "all_employees_"
                            + LocalDate.now()
                            + ".csv";

        }
        else {

            employees =
                    employeeService.getEmployeesByTeam(
                            teamId
                    );

            Team team =
                    teamService.getTeamById(teamId);

            filename =
                    team.getName()
                            + "_employees_"
                            + LocalDate.now()
                            + ".csv";
        }

        employees.sort(
                Comparator.comparing(
                        e -> statusOrder(e.getStatus())
                )
        );

        response.setContentType(
                "text/csv; charset=UTF-8"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + filename + "\""
        );

        PrintWriter writer =
                response.getWriter();

        writer.write("\uFEFF");

        writer.println(
                "氏名,メール,電話番号,現場,状況,面接日,面接時間,チーム"
        );

        for (Employee employee : employees) {

            writer.println(

                    employee.getName() + "," +

                            employee.getEmail() + "," +

                            employee.getPhoneNumber() + "," +

                            employee.getSiteLocation() + "," +

                            statusLabel(employee.getStatus()) + "," +

                            (employee.getInterviewDate() == null
                                    ? ""
                                    : employee.getInterviewDate()) + "," +

                            (employee.getInterviewTime() == null
                                    ? ""
                                    : employee.getInterviewTime()) + "," +

                            (employee.getTeam() == null
                                    ? ""
                                    : employee.getTeam().getName())
            );
        }

        auditLogService.log(

                "ADMIN",
                "SYSTEM",
                "EMPLOYEE_EXPORT",

                teamId == null
                        ? "Exported ALL employees"
                        : "Exported employee list for team " + teamId
        );
    }

    @GetMapping("/export/employees/excel")
    public void exportEmployeesExcel(

            @RequestParam(required = false)
            Long teamId,

            HttpServletResponse response

    ) throws IOException {

        List<Employee> employees;

        if (teamId == null) {

            employees =
                    employeeService.getAllEmployees();

        } else {

            employees =
                    employeeService.getEmployeesByTeam(
                            teamId
                    );
        }

        employees.sort(
                Comparator.comparing(
                        Employee::getStatus
                )
        );

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Employees"
                );

        // Header Style
        CellStyle headerStyle =
                workbook.createCellStyle();

        Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        headerStyle.setFont(
                headerFont
        );

        headerStyle.setFillForegroundColor(
                IndexedColors.ORANGE.getIndex()
        );

        headerStyle.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );
        //centered header style
        CellStyle centeredHeaderStyle =
                workbook.createCellStyle();

        centeredHeaderStyle.cloneStyleFrom(
                headerStyle
        );

        centeredHeaderStyle.setAlignment(
                HorizontalAlignment.CENTER
        );

        centeredHeaderStyle.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        // Completed Style
        CellStyle completedStyle =
                workbook.createCellStyle();

        completedStyle.setFillForegroundColor(
                IndexedColors.LIGHT_GREEN.getIndex()
        );

        completedStyle.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        // Scheduled Style
        CellStyle scheduledStyle =
                workbook.createCellStyle();

        scheduledStyle.setFillForegroundColor(
                IndexedColors.LIGHT_ORANGE.getIndex()
        );

        scheduledStyle.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );



        // Header Row
        String[] columns = {

                "氏名",
                "メール",
                "電話番号",
                "現場",
                "状況",
                "面接日",
                "面接時間",
                "チーム"
        };

        Row header =
                sheet.createRow(0);
        //scroll keeps headers visible
        sheet.createFreezePane(0, 1);

        header.setHeightInPoints(28);

        sheet.setAutoFilter(
                new CellRangeAddress(
                        0,
                        0,
                        0,
                        columns.length - 1
                )
        );

        for (int i = 0; i < columns.length; i++) {

            Cell cell =
                    header.createCell(i);

            cell.setCellValue(
                    columns[i]
            );

            cell.setCellStyle(centeredHeaderStyle);
        }

        // Data Rows
        int rowNum = 1;

        for (Employee employee : employees) {

            Row row =
                    sheet.createRow(
                            rowNum++
                    );

            row.setHeightInPoints(25);

            row.createCell(0)
                    .setCellValue(
                            employee.getName()
                    );

            row.createCell(1)
                    .setCellValue(
                            employee.getEmail()
                    );

            row.createCell(2)
                    .setCellValue(
                            employee.getPhoneNumber()
                    );

            row.createCell(3)
                    .setCellValue(
                            employee.getSiteLocation()
                    );

            Cell statusCell =
                    row.createCell(4);

            statusCell.setCellValue(
                    statusLabel(
                            employee.getStatus()
                    )
            );

            if (employee.getStatus()
                    == InterviewStatus.COMPLETED) {

                statusCell.setCellStyle(
                        completedStyle
                );
            }

            if (employee.getStatus()
                    == InterviewStatus.SCHEDULED) {

                statusCell.setCellStyle(
                        scheduledStyle
                );
            }

            row.createCell(5)
                    .setCellValue(

                            employee.getInterviewDate() != null
                                    ? employee.getInterviewDate().toString()
                                    : ""

                    );

            row.createCell(6)
                    .setCellValue(

                            employee.getInterviewTime() != null
                                    ? employee.getInterviewTime().toString()
                                    : ""

                    );

            row.createCell(7)
                    .setCellValue(

                            employee.getTeam() != null
                                    ? employee.getTeam().getName()
                                    : ""

                    );
        }

        // Auto size
        for (int i = 0; i < columns.length; i++) {

            sheet.autoSizeColumn(i);
        }

        sheet.setColumnWidth(0, 6000);  // Name
        sheet.setColumnWidth(1, 9000);  // Email
        sheet.setColumnWidth(2, 5000);  // Phone
        sheet.setColumnWidth(3, 6000);  // Site
        sheet.setColumnWidth(4, 5000);  // Status
        sheet.setColumnWidth(5, 4500);  // Interview Date
        sheet.setColumnWidth(6, 4500);  // Interview Time
        sheet.setColumnWidth(7, 5000);  // Team

        String filename =
                teamId == null

                        ? "ALL_employees_"
                          + LocalDate.now()
                          + ".xlsx"

                        : "Team_"
                          + teamId
                          + "_employees_"
                          + LocalDate.now()
                          + ".xlsx";

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(

                "Content-Disposition",

                "attachment; filename=\""
                        + filename
                        + "\""
        );

        workbook.write(
                response.getOutputStream()
        );

        workbook.close();

        auditLogService.log(

                "ADMIN",
                "SYSTEM",
                "EMPLOYEE_EXPORT",

                teamId == null

                        ? "Exported ALL employees"

                        : "Exported employee list for team "
                          + teamId
        );
    }
}