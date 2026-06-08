package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.dto.DashboardStats;
import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
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

import java.util.List;

@Controller
public class DashboardController {

    private final EmployeeService employeeService;
    private final TeamService teamService;

    public DashboardController(EmployeeService employeeService, TeamService teamService) {

        this.employeeService = employeeService;
        this.teamService = teamService;

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

        DashboardStats stats;

        if (teamId == null) {
            stats = employeeService.getDashboardStats();
        } else {
            stats = employeeService.getDashboardStatsByTeam(teamId);
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                auth.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

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
    public String schedule(Model model) {

        List<Employee> scheduledEmployees =
                employeeService.getEmployeesByStatus(InterviewStatus.SCHEDULED);

        model.addAttribute("scheduledEmployees", scheduledEmployees);

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