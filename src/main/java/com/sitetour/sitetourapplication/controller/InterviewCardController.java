package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.entity.User;
import com.sitetour.sitetourapplication.repository.UserRepository;
import com.sitetour.sitetourapplication.service.AuditLogService;
import com.sitetour.sitetourapplication.service.InterviewCardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InterviewCardController {

    private final InterviewCardService interviewCardService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;;

    public InterviewCardController(
            InterviewCardService interviewCardService,
            UserRepository userRepository,
            AuditLogService auditLogService
    ) {
        this.interviewCardService = interviewCardService;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }


    // view list (secured)

    @GetMapping("/interviewcards")
    public String interviewCards(Model model,
                                 Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        List<InterviewCard> cards;

        if (isAdmin) {
            cards = interviewCardService.getAllCardsOrdered();
        } else {
            cards = interviewCardService.getCardsByTeam(
                    user.getTeam().getId()
            );
        }

        model.addAttribute("cards", cards);
        model.addAttribute("isAdmin", isAdmin);

        return "interviewcards";
    }


    // update card (secured)
    @PostMapping("/interviewcards/update")
    public String updateCard(

            @RequestParam Long id,

            @RequestParam(required = false) String question1,
            @RequestParam(required = false) String answer1,

            @RequestParam(required = false) String question2,
            @RequestParam(required = false) String answer2,

            @RequestParam(required = false) String question3,
            @RequestParam(required = false) String answer3,

            @RequestParam(required = false) String question4,
            @RequestParam(required = false) String answer4,

            @RequestParam(required = false) String question5,
            @RequestParam(required = false) String answer5,

            @RequestParam(required = false) String impressions,

            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        InterviewCard card =
                interviewCardService.getCardById(id);

        //details + if statements are audit log checking
        //for detail saving on card update for log purposes
        String details =
                "Employee="
                        + card.getEmployee().getName();

        if (answer1 != null && !answer1.isBlank()) {
            details += " | Q1 Answer Updated";
        }

        if (answer2 != null && !answer2.isBlank()) {
            details += " | Q2 Answer Updated";
        }

        if (answer3 != null && !answer3.isBlank()) {
            details += " | Q3 Answer Updated";
        }

        if (answer4 != null && !answer4.isBlank()) {
            details += " | Q4 Answer Updated";
        }

        if (answer5 != null && !answer5.isBlank()) {
            details += " | Q5 Answer Updated";
        }

        if (impressions != null && !impressions.isBlank()) {
            details += " | Impressions Updated";
        }

        // security check

        if (!isAdmin &&
                !card.getEmployee()
                        .getTeam()
                        .getId()
                        .equals(user.getTeam().getId())) {

            return "redirect:/interviewcards";
        }

        //update fields

        card.setQuestion1(question1);
        card.setAnswer1(answer1);

        card.setQuestion2(question2);
        card.setAnswer2(answer2);

        card.setQuestion3(question3);
        card.setAnswer3(answer3);

        card.setQuestion4(question4);
        card.setAnswer4(answer4);

        card.setQuestion5(question5);
        card.setAnswer5(answer5);

        card.setImpressions(impressions);

        System.out.println(details);
        //save changes to audit log
        auditLogService.log(
                user.getUsername(),
                user.getTeam().getName(),
                "INTERVIEW_CARD_UPDATED",
                details
        );

        interviewCardService.save(card);


        return "redirect:/interviewcards#card-" + card.getId();
    }
}