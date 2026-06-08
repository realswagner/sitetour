package com.sitetour.sitetourapplication.controller;

import com.sitetour.sitetourapplication.entity.InterviewCard;
import com.sitetour.sitetourapplication.service.InterviewCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InterviewCardController {

    private final InterviewCardService interviewCardService;

    public InterviewCardController(
            InterviewCardService interviewCardService) {

        this.interviewCardService = interviewCardService;
    }

    @GetMapping("/interviewcards")
    public String interviewCards(Model model) {

        model.addAttribute(
                "cards",
                interviewCardService.getAllCards());
        model.addAttribute(
                "cards",
                interviewCardService.getAllCardsOrdered());

        return "interviewcards";
    }

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

            @RequestParam(required = false) String impressions
    ) {

        InterviewCard card =
                interviewCardService.getCardById(id);

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

        interviewCardService.save(card);

        return "redirect:/interviewcards#card-" + card.getId();
    }
}