package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.service.TeamService;
import com.sitetour.sitetourapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TeamService teamService;
    private final UserService userService;

    public AdminController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @PostMapping("/teams/create")
    public String createTeam(
            @RequestParam String teamName,
            @RequestParam String username,
            @RequestParam String password
    ) {
        Team team = teamService.createTeam(teamName);
        userService.createTeamUser(username, password, team);

        return "redirect:/admin";
    }

    @GetMapping
    public String adminPage(Model model) {

        model.addAttribute("teams", teamService.getAllTeams());

        return "admin";
    }

    @PostMapping("/teams/delete")
    public String deleteTeam(@RequestParam Long teamId) {

        teamService.deleteTeam(teamId);

        return "redirect:/admin";
    }
}