package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.dto.DashboardStats;
import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
import com.sitetour.sitetourapplication.repository.EmployeeRepository;
import com.sitetour.sitetourapplication.repository.TeamRepository;
import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.repository.InterviewCardRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;



@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final InterviewCardRepository interviewCardRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            TeamRepository teamRepository,
            InterviewCardRepository interviewCardRepository) {

        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.interviewCardRepository = interviewCardRepository;
    }

    //create employee entity function
    public void createEmployee(
            String name,
            String email,
            String phoneNumber,
            String siteLocation,
            InterviewStatus status,
            String proposedDates,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String zoomLink,
            Long teamId
    ) {

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Employee employee = new Employee();

        employee.setName(name);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setSiteLocation(siteLocation);
        employee.setStatus(status);
        employee.setProposedDates(proposedDates);
        employee.setInterviewDate(interviewDate);
        employee.setInterviewTime(interviewTime);
        employee.setZoomLink(zoomLink);
        employee.setTeam(team);

        employeeRepository.save(employee);

        InterviewCard card = new InterviewCard();
        card.setEmployee(employee);

        interviewCardRepository.save(card);
    }

    //method to display employee list
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //gets individual employee entry for display on employee detail page
    //(/employee/id)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //update status method
    public void updateStatus(Long id, InterviewStatus status) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setStatus(status);

        employeeRepository.save(employee);
    }

    //filter by interview status
    public List<Employee> getEmployeesByStatus(InterviewStatus status) {
        return employeeRepository.findByStatus(status);

    }

    //edit page function to update employee information
    public void updateEmployee(
            Long id,
            String name,
            String email,
            String phoneNumber,
            String siteLocation,
            InterviewStatus status,
            String proposedDates,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String zoomLink
    ) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(name);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setSiteLocation(siteLocation);
        employee.setStatus(status);
        employee.setProposedDates(proposedDates);
        employee.setInterviewDate(interviewDate);
        employee.setInterviewTime(interviewTime);
        employee.setZoomLink(zoomLink);

        employeeRepository.save(employee);

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
    }

    //dashboard status counting logic:
    public DashboardStats getDashboardStats() {

        DashboardStats stats = new DashboardStats();

        stats.setNotContacted(
                employeeRepository.countByStatus(InterviewStatus.NOT_CONTACTED));

        stats.setContacting(
                employeeRepository.countByStatus(InterviewStatus.CONTACTING));

        stats.setAwaitingReply(
                employeeRepository.countByStatus(InterviewStatus.AWAITING_REPLY));

        stats.setScheduled(
                employeeRepository.countByStatus(InterviewStatus.SCHEDULED));

        stats.setCompleted(
                employeeRepository.countByStatus(InterviewStatus.COMPLETED));

        return stats;
    }
    //service helper for teamID
    public List<Employee> getEmployeesByTeam(Long teamId) {
        return employeeRepository.findByTeamId(teamId);
    }

    public DashboardStats getDashboardStatsByTeam(Long teamId) {

        DashboardStats stats = new DashboardStats();

        stats.setNotContacted(employeeRepository.countByStatusAndTeamId(InterviewStatus.NOT_CONTACTED, teamId));
        stats.setContacting(employeeRepository.countByStatusAndTeamId(InterviewStatus.CONTACTING, teamId));
        stats.setAwaitingReply(employeeRepository.countByStatusAndTeamId(InterviewStatus.AWAITING_REPLY, teamId));
        stats.setScheduled(employeeRepository.countByStatusAndTeamId(InterviewStatus.SCHEDULED, teamId));
        stats.setCompleted(employeeRepository.countByStatusAndTeamId(InterviewStatus.COMPLETED, teamId));

        return stats;
    }

    //retrieves scheduled interviews for dashboard on day of
    public List<Employee> getTodaysInterviews() {

        return employeeRepository.findAll()
                .stream()
                .filter(employee ->
                        employee.getInterviewDate() != null
                                && employee.getInterviewDate().equals(LocalDate.now()))
                .toList();
    }
}