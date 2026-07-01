package com.sitetour.sitetourapplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "interview_card")
public class InterviewCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;


    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    // Future: Q&A + impressions (leave empty for now)
    private String question1;

    @Column(columnDefinition = "TEXT")
    private String answer1;

    private String question2;

    @Column(columnDefinition = "TEXT")
    private String answer2;

    private String question3;

    @Column(columnDefinition = "TEXT")
    private String answer3;

    private String question4;

    @Column(columnDefinition = "TEXT")
    private String answer4;

    private String question5;

    @Column(columnDefinition = "TEXT")
    private String answer5;

    @Column(columnDefinition = "TEXT")
    private String impressions;

    // Constructors
    public InterviewCard() {}

    public InterviewCard(Employee employee) {
        this.employee = employee;
    }

    // Getters & setters
    public Long getId() {
        return id;
    }


}