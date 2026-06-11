package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TeamService teamService;
    private final InterviewCardService interviewCardService;
    private final UserRepository userRepository;

    public EmployeeController(
            EmployeeService employeeService,
            TeamService teamService,
            InterviewCardService interviewCardService,
            UserRepository userRepository
    ){
        this.employeeService = employeeService;
        this.teamService = teamService;
        this.interviewCardService = interviewCardService;
        this.userRepository = userRepository;
    }



    @GetMapping("/employees/new")
    public String createEmployeeForm(Model model) {

        model.addAttribute("teams", teamService.getAllTeams());

        return "create_employee";
    }
    //navigates to create employee html page
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

        model.addAttribute("employee", employee);

        InterviewCard card =
                interviewCardService.getCardByEmployeeId(id);

        model.addAttribute("card", card);

        return "employee_detail";
    }
    //update status endpoint (enum)
    @PostMapping("/employees/update-status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam InterviewStatus status,
            Authentication authentication) {

        Employee employee =
                employeeService.getEmployeeById(id);

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

        employeeService.updateStatus(id, status);

        return "redirect:/employees/" + id;
    }

    @GetMapping("/employees/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Employee employee =
                employeeService.getEmployeeById(id);

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

        model.addAttribute("employee", employee);

        return "employee-edit";
    }

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

        return "redirect:/employees/" + id;
    }

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

        if (isAdmin) {

            if (teamId != null) {
                employees = employeeService.getEmployeesByTeam(teamId);
            }
            else if (status != null) {
                employees = employeeService.getEmployeesByStatus(status);
            }
            else {
                employees = employeeService.getAllEmployees();
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

        model.addAttribute("employees", employees);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("isAdmin", isAdmin);

        return "employees";
    }

}
