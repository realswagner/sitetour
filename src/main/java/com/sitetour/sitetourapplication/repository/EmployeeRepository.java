package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.Team;
import com.sitetour.sitetourapplication.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sitetour.sitetourapplication.entity.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStatus(InterviewStatus status);
    List<Employee> findByTeamId(Long teamId);
    long countByStatus(InterviewStatus status);
    long countByStatusAndTeamId(InterviewStatus status, Long teamId);
    @Modifying
    @Transactional
    void deleteAllByTeamId(Long teamId);

}
