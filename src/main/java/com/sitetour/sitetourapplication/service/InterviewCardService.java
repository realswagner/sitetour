package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.entity.InterviewCard;
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
    public InterviewCard getCardByEmployeeId(Long employeeId) {

        return interviewCardRepository
                .findByEmployeeId(employeeId)
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
}