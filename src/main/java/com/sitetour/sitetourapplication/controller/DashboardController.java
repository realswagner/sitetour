package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.dto.DashboardStats;
import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
import com.sitetour.sitetourapplication.repository.UserRepository;
import com.sitetour.sitetourapplication.service.EmployeeService;
import com.sitetour.sitetourapplication.service.TeamService;
import com.sitetour.sitetourapplication.controller.EmployeeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Controller
public class DashboardController {

    private final EmployeeService employeeService;
    private final TeamService teamService;
    private final UserRepository userRepository;;


    public DashboardController(EmployeeService employeeService, TeamService teamService, UserRepository userRepository) {

        this.employeeService = employeeService;
        this.teamService = teamService;
        this.userRepository = userRepository;

    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) Long teamId,
            Model model
    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user =
                userRepository
                        .findByUsername(auth.getName())
                        .orElseThrow();

        boolean isAdmin =
                auth.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        DashboardStats stats;

        if (isAdmin) {

            if (teamId == null) {
                stats = employeeService.getDashboardStats();
            } else {
                stats = employeeService.getDashboardStatsByTeam(teamId);
            }

        } else {

            stats = employeeService.getDashboardStatsByTeam(
                    user.getTeam().getId());

        }


        model.addAttribute("username", auth.getName());
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("stats", stats);
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("selectedTeam", teamId);

        model.addAttribute(
                "todaysInterviews",
                employeeService.getTodaysInterviews()
        );

        return "dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        List<Employee> employees;

        if (isAdmin) {

            employees =
                    employeeService.getScheduledEmployees();

        }
        else {

            employees =
                    employeeService.getScheduledEmployeesByTeam(
                            user.getTeam().getId()
                    );
        }

        Map<LocalDate, List<Employee>> groupedSchedules =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                Employee::getInterviewDate,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        model.addAttribute(
                "groupedSchedules",
                groupedSchedules
        );

        return "schedule";
    }

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    @ResponseBody
    public String dbTest() {
        return "DB is connected!";
    }
}