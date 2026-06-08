package com.sitetour.sitetourapplication.controller;
import com.sitetour.sitetourapplication.entity.Team;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.sitetour.sitetourapplication.service.EmployeeService;
import com.sitetour.sitetourapplication.service.TeamService;

@Controller
public class TeamController {

    private final EmployeeService employeeService;
    private final TeamService teamService;

    public TeamController(
            TeamService teamService,
            EmployeeService employeeService
    ) {

        this.teamService = teamService;
        this.employeeService = employeeService;
    }

    @GetMapping("/teams/{id}")
    public String teamDetail(
            @PathVariable Long id,
            Model model
    ) {

        Team team =
                teamService.getTeamById(id);

        model.addAttribute(
                "team",
                team);

        model.addAttribute(
                "employees",
                employeeService.getEmployeesByTeam(id));

        return "team_detail";
    }
}
