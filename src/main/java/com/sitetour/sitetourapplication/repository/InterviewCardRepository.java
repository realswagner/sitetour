package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.InterviewCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewCardRepository
        extends JpaRepository<InterviewCard, Long> {

    Optional<InterviewCard> findByEmployeeId(Long employeeId);
    List<InterviewCard> findAllByOrderByEmployee_InterviewDateAsc();
    List<InterviewCard> findByEmployee_Team_IdOrderByEmployee_InterviewDateAsc(Long teamId);
    void deleteAllByEmployeeTeamId(Long teamId);
    void deleteByEmployeeId(Long employeeId);

}