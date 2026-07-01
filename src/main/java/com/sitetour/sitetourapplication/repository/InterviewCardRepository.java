package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.InterviewCard;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface InterviewCardRepository
        extends JpaRepository<InterviewCard, Long> {

    Optional<InterviewCard>
    findByEmployeeIdAndOwnerId(
            Long employeeId,
            Long ownerId);
    List<InterviewCard> findAllByEmployeeId(Long employeeId);
    List<InterviewCard> findAllByOrderByEmployee_InterviewDateAsc();
    List<InterviewCard>
    findByOwnerIdOrderByEmployee_InterviewDateAsc(
            Long ownerId);
    List<InterviewCard> findByEmployee_Team_IdOrderByEmployee_InterviewDateAsc(Long teamId);
    void deleteAllByEmployeeTeamId(Long teamId);
    void deleteByEmployeeId(Long employeeId);

    @Modifying
    @Transactional
    void deleteAllByOwnerId(Long ownerId);

}