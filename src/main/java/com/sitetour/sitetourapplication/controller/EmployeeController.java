package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
import com.sitetour.sitetourapplication.enums.Role;
import com.sitetour.sitetourapplication.repository.InterviewCardRepository;
import com.sitetour.sitetourapplication.service.AuditLogService;
import com.sitetour.sitetourapplication.service.EmployeeService;
import com.sitetour.sitetourapplication.service.InterviewCardService;
import com.sitetour.sitetourapplication.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TeamService teamService;
    private final InterviewCardService interviewCardService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final InterviewCardRepository interviewCardRepository;

    public EmployeeController(
            EmployeeService employeeService,
            TeamService teamService,
            InterviewCardService interviewCardService,
            UserRepository userRepository,
            AuditLogService auditLogService,
            InterviewCardRepository interviewCardRepository
    ){
        this.employeeService = employeeService;
        this.teamService = teamService;
        this.interviewCardService = interviewCardService;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.interviewCardRepository = interviewCardRepository;
    }



    @GetMapping("/employees/new")
    public String createEmployeeForm(
            Authentication authentication,
            Model model) {

        User user =
                userRepository
                        .findByUsername(
                                authentication.getName()
                        )
                        .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        model.addAttribute(
                "teams",
                teamService.getAllTeams()
        );

        model.addAttribute(
                "isAdmin",
                isAdmin
        );

        return "create_employee";
    }
    //create and save new employee logic
    @PostMapping("/employees")
    public String saveEmployee(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String siteLocation,
            @RequestParam InterviewStatus status,
            @RequestParam(required = false) String proposedDates,
            @RequestParam(required = false) LocalDate interviewDate,
            @RequestParam(required = false) LocalTime interviewTime,
            @RequestParam(required = false) String zoomLink,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        Long teamId = user.getTeam().getId();


        Employee employee =
                employeeService.createEmployee(
                        name,
                        email,
                        phoneNumber,
                        siteLocation,
                        status,
                        proposedDates,
                        interviewDate,
                        interviewTime,
                        zoomLink,
                        teamId
                );
        interviewCardService.getOrCreateCard(
                employee,
                user
        );

        String details =
                "Employee=" + employee.getName()
                        + " | Owner=" + user.getUsername();
        //creates audit log entry upon employee creation
        auditLogService.log(
                user.getUsername(),
                user.getTeam().getName(),
                "EMPLOYEE_CREATED", details

        );

        return "redirect:/employees";
    }

    //view individual employee page routing
    @GetMapping("/employees/{id}")
    public String viewEmployee(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Employee employee = employeeService.getEmployeeById(id);

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();



        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        if (!isAdmin &&
                !employee.getTeam().getId()
                        .equals(user.getTeam().getId())) {

            return "redirect:/employees";
        }
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("employee", employee);

        InterviewCard card =
                interviewCardService
                        .getOrCreateCard(
                                employee,
                                user
                        );

        model.addAttribute("card", card);

        return "employee_detail";
    }
    //update status endpoint (enum)
    @PostMapping("/employees/update-status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam InterviewStatus status,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Employee employee =
                employeeService.getEmployeeById(id);
        //audit log details need previous status for
        //monitoring changes
        InterviewStatus oldStatus =
                employee.getStatus();

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();
        boolean isAdmin =
                user.getRole().name().equals("ADMIN");


        if (user.getRole() == Role.ADMIN) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "管理者は社員情報を編集できません"
            );

            return "redirect:/employees";
        }

        employeeService.updateStatus(id, status);

        auditLogService.log(
                user.getUsername(),
                user.getTeam().getName(),
                "STATUS_CHANGED",
                "UpdatedBy="
                        + user.getUsername()
                        + " | Employee="
                        + employee.getName()
                        + " : "
                        + oldStatus
                        + " -> "
                        + status
        );

        return "redirect:/employees/" + id;
    }

    @GetMapping("/employees/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        Employee employee =
                employeeService.getEmployeeById(id);

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        if (user.getRole() == Role.ADMIN) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "管理者は社員情報を編集できません"
            );

            return "redirect:/employees";
        }
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("employee", employee);

        return "employee-edit";
    }

    //employee update page logic
    @PostMapping("/employees/update")
    public String updateEmployee(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String siteLocation,
            @RequestParam InterviewStatus status,
            @RequestParam(required = false) String proposedDates,
            @RequestParam(required = false) LocalDate interviewDate,
            @RequestParam(required = false) LocalTime interviewTime,
            @RequestParam(required = false) String zoomLink,
            Authentication authentication
    ) {
        Employee employee =
                employeeService.getEmployeeById(id);
        String oldEmail = employee.getEmail();
        String oldPhone = employee.getPhoneNumber();
        String oldLocation = employee.getSiteLocation();
        InterviewStatus oldStatus = employee.getStatus();

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");
        //admin doesn't handle employee updates - left to user/team
        if (!isAdmin &&
                !employee.getTeam().getId()
                        .equals(user.getTeam().getId())) {

            return "redirect:/employees";
        }

        employeeService.updateEmployee(
                id,
                name,
                email,
                phoneNumber,
                siteLocation,
                status,
                proposedDates,
                interviewDate,
                interviewTime,
                zoomLink
        );

        auditLogService.log(
                user.getUsername(),
                user.getTeam().getName(),
                "EMPLOYEE_UPDATED",
                "Employee: " + employee.getName()
                        + " | Email: " + oldEmail + " -> " + email
                        + " | Phone: " + oldPhone + " -> " + phoneNumber
                        + " | Location: " + oldLocation + " -> " + siteLocation + "UpdatedBy="
                        + user.getUsername()
                        + " | Employee="
                        + employee.getName()
        );

        return "redirect:/employees/" + id;
    }
    //employee db fetching logic
    @GetMapping("/employees")
    public String getEmployees(
            @RequestParam(required = false) InterviewStatus status,
            @RequestParam(required = false) Long teamId,
            Authentication authentication,
            Model model
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        List<Employee> employees;
        //admin can view entire employee list and filter by team
        if (isAdmin) {

            if (teamId != null && status != null) {

                employees =
                        employeeService
                                .getEmployeesByTeam(teamId)
                                .stream()
                                .filter(e -> e.getStatus() == status)
                                .toList();

            }
            else if (teamId != null) {

                employees =
                        employeeService.getEmployeesByTeam(teamId);

            }
            else if (status != null) {

                employees =
                        employeeService.getEmployeesByStatus(status);

            }
            else {

                employees =
                        employeeService.getAllEmployees();

            }

            model.addAttribute(
                    "teams",
                    teamService.getAllTeams()
            );
        }
        else {

            Long userTeamId =
                    user.getTeam().getId();

            employees =
                    employeeService.getEmployeesByTeam(userTeamId);

            if (status != null) {

                employees = employees.stream()
                        .filter(e -> e.getStatus() == status)
                        .toList();
            }
        }

        //interview card created or not logic for employee table on /employees
        Map<Long, Boolean> cardStatus =
                new HashMap<>();

        for (Employee employee : employees) {

            boolean hasCard =
                    interviewCardRepository
                            .findByEmployeeIdAndOwnerId(
                                    employee.getId(),
                                    user.getId()
                            )
                            .isPresent();

            cardStatus.put(
                    employee.getId(),
                    hasCard
            );
        }

        model.addAttribute(
                "cardStatus",
                cardStatus
        );
        model.addAttribute("employees", employees);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("selectedTeamId", teamId);
        return "employees";
    }
    //deletion logic includes deletion details in audit log
    //
    @PostMapping("/employees/delete")
    public String deleteEmployee(
            @RequestParam Long id,
            @RequestParam String confirmName,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        Employee employee =
                employeeService.getEmployeeById(id);

        if (!employee.getName().equals(confirmName)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "社員名が一致しません"
            );

            return "redirect:/employees/" + id;
        }

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        if (user.getRole() == Role.ADMIN) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "管理者は社員を削除できません"
            );

            return "redirect:/employees";
        }
        String details =
                "Name=" + employee.getName()
                        + ", Status=" + employee.getStatus()
                        + ", Date=" + employee.getInterviewDate()
                        + ", Time=" + employee.getInterviewTime() + ", DeletedBy="
                        + user.getUsername();

        List<InterviewCard> cards =
                interviewCardRepository
                        .findAllByEmployeeId(id);

        //logs interview card answers upon deletion
        //safeguards against accidental loss of interview records
        if (!cards.isEmpty()) {

            details += " | CardCount=" + cards.size();

            for (InterviewCard card : cards) {

                details +=
                        " | [Owner="
                                + card.getOwner().getUsername();

                if (card.getImpressions() != null
                        && !card.getImpressions().isBlank()) {

                    details +=
                            ", Impressions="
                                    + card.getImpressions();
                }

                if (card.getAnswer1() != null
                        && !card.getAnswer1().isBlank()) {

                    details +=
                            ", A1="
                                    + card.getAnswer1();
                }

                if (card.getAnswer2() != null
                        && !card.getAnswer2().isBlank()) {

                    details +=
                            ", A2="
                                    + card.getAnswer2();
                }

                if (card.getAnswer3() != null
                        && !card.getAnswer3().isBlank()) {

                    details +=
                            ", A3="
                                    + card.getAnswer3();
                }

                if (card.getAnswer4() != null
                        && !card.getAnswer4().isBlank()) {

                    details +=
                            ", A4="
                                    + card.getAnswer4();
                }

                if (card.getAnswer5() != null
                        && !card.getAnswer5().isBlank()) {

                    details +=
                            ", A5="
                                    + card.getAnswer5();
                }

                details += "]";
            }
        }

        auditLogService.log(
                user.getUsername(),
                user.getTeam().getName(),
                "EMPLOYEE_DELETED",
                details
        );
        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "社員を削除しました"
        );

        return "redirect:/employees";
    }
}
