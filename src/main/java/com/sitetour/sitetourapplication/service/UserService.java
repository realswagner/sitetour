package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.enums.Role;
import com.sitetour.sitetourapplication.repository.TeamRepository;
import com.sitetour.sitetourapplication.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TeamRepository teamRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamRepository = teamRepository;

    }

    public void createAdmin(
            String username,
            String password) {

        User user = new User();

        user.setUsername(username);

        user.setPassword(
                passwordEncoder.encode(password));

        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }

    public void createTeamUser(String username, String password, Team team) {

        User user = new User();

        user.setUsername(username);

        user.setPassword(passwordEncoder.encode(password));

        user.setRole(Role.TEAM);

        user.setTeam(team);

        userRepository.save(user);
    }

    //for dev purposes create standard admin login
    @PostConstruct
    public void initAdmin() {

        if (userRepository.count() == 0) {

            createAdmin(
                    "admin",
                    "admin123");

            System.out.println(
                    "Default admin created");
        }
    }

    public void createTeamUser(String username, String password, Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow();

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.TEAM);
        user.setTeam(team);

        userRepository.save(user);
    }
}