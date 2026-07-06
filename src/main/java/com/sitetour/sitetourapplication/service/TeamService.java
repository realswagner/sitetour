package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.repository.EmployeeRepository;
import com.sitetour.sitetourapplication.repository.InterviewCardRepository;
import com.sitetour.sitetourapplication.repository.TeamRepository;
import com.sitetour.sitetourapplication.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final InterviewCardRepository interviewCardRepository;
    private final UserRepository userRepository;

    public TeamService(
            TeamRepository teamRepository,
            EmployeeRepository employeeRepository,
            InterviewCardRepository interviewCardRepository,
            UserRepository userRepository
    ) {

        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
        this.interviewCardRepository = interviewCardRepository;
        this.userRepository = userRepository;
    }

    //order by id - order created
    public List<Team> getAllTeams() {
        return teamRepository.findAllByOrderByIdAsc();
    }

    public Team createTeam(String name) {
        return teamRepository.save(new Team(name));
    }

    @Transactional
    public void deleteTeam(Long id) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Team not found"));

        interviewCardRepository.deleteAllByEmployeeTeamId(id);

        employeeRepository.deleteAllByTeamId(id);

        userRepository.deleteAllByTeamId(id);

        teamRepository.delete(team);
    }

    //rename team name
    public void renameTeam(
            Long teamId,
            String newName
    ) {

        Team team = teamRepository
                .findById(teamId)
                .orElseThrow();

        team.setName(newName);

        teamRepository.save(team);
    }

//    dummy data for testing purposes
//    @PostConstruct
//    public void initTeams() {
//        if (teamRepository.count() == 0) {
//            teamRepository.save(new Team("Team Alpha"));
//            teamRepository.save(new Team("Team Beta"));
//            teamRepository.save(new Team("Team Gamma"));
//        }
//    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    //updates admin view team notes (members)
    public void updateTeamNotes(
            Long teamId,
            String memberNotes
    ) {

        Team team = teamRepository
                .findById(teamId)
                .orElseThrow();

        team.setMemberNotes(memberNotes);

        teamRepository.save(team);
    }


}