package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.repository.UserRepository;
import com.sitetour.sitetourapplication.service.TeamService;
import com.sitetour.sitetourapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TeamService teamService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdminController(TeamService teamService, UserService userService, UserRepository userRepository) {
        this.teamService = teamService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/teams/create")
    public String createTeam(
            @RequestParam String teamName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        //confirm password validation
        if (!password.equals(confirmPassword)) {
            return "redirect:/admin?error=passwordMismatch";
        }
        Team team = teamService.createTeam(teamName);
        userService.createTeamUser(username, password, team);

        return "redirect:/admin";
    }

    @GetMapping
    public String adminPage(Model model) {

        model.addAttribute("teams", teamService.getAllTeams());
        Map<Long, String> teamLogins = new HashMap<>();

        for (Team team : teamService.getAllTeams()) {

            userRepository.findByTeamId(team.getId())
                    .ifPresent(user ->
                            teamLogins.put(
                                    team.getId(),
                                    user.getUsername()
                            ));
        }

        model.addAttribute(
                "teamLogins",
                teamLogins
        );

        return "admin";
    }

    @PostMapping("/teams/delete")
    public String deleteTeam(@RequestParam Long teamId) {

        teamService.deleteTeam(teamId);

        return "redirect:/admin";
    }

    //update notes for team
    @PostMapping("/teams/update-notes")
    public String updateTeamNotes(

            @RequestParam Long teamId,
            @RequestParam String memberNotes

    ) {

        teamService.updateTeamNotes(
                teamId,
                memberNotes
        );

        return "redirect:/admin";
    }

    //team password reset
    @PostMapping("/teams/reset-password")
    public String resetPassword(

            @RequestParam Long teamId,
            @RequestParam String password,
            @RequestParam String confirmPassword

    ) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/admin?error=passwordMismatch";
        }

        userService.resetTeamPassword(
                teamId,
                password
        );

        return "redirect:/admin?success=passwordReset";
    }

    //team rename direction
    @PostMapping("/teams/rename")
    public String renameTeam(
            @RequestParam Long teamId,
            @RequestParam String teamName
    ) {

        teamService.renameTeam(
                teamId,
                teamName
        );

        return "redirect:/admin";
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

        return "redirect:/admin";
    }
}