package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.entity.Employee;
import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.repository.InterviewCardRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class InterviewCardService {

    private final InterviewCardRepository interviewCardRepository;

    public InterviewCardService(
            InterviewCardRepository interviewCardRepository) {

        this.interviewCardRepository = interviewCardRepository;
    }

    public List<InterviewCard> getAllCards() {
        return interviewCardRepository
                .findAllByOrderByEmployee_InterviewDateAsc();
    }

    public InterviewCard getCardById(Long id) {
        return interviewCardRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview card not found"));
    }


    public void save(InterviewCard card) {
        interviewCardRepository.save(card);
    }

    public List<InterviewCard> getAllCardsOrdered() {

        return interviewCardRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                card -> card.getEmployee()
                                        .getInterviewDate(),
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .toList();
    }
    //return card by team ID
    public List<InterviewCard> getCardsByTeam(Long teamId) {
        return interviewCardRepository
                .findByEmployee_Team_IdOrderByEmployee_InterviewDateAsc(teamId);
    }

    //filters personal report cards by user
    public InterviewCard getCardByEmployeeAndUser(

            Long employeeId,
            Long userId

    ) {

        return interviewCardRepository
                .findByEmployeeIdAndOwnerId(
                        employeeId,
                        userId
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Interview card not found"
                        )
                );
    }


    //separates team from user interview card / individual cards PER USER not team
    public InterviewCard getOrCreateCard(

            Employee employee,
            User owner

    ) {

        return interviewCardRepository
                .findByEmployeeIdAndOwnerId(
                        employee.getId(),
                        owner.getId()
                )
                .orElseGet(() -> {

                    InterviewCard card =
                            new InterviewCard();

                    card.setEmployee(employee);

                    card.setOwner(owner);

                    return interviewCardRepository
                            .save(card);
                });
    }
}