package com.sitetour.sitetourapplication.entity;


import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public String getMemberNotes() {
        return memberNotes;
    }

    public void setMemberNotes(String memberNotes) {
        this.memberNotes = memberNotes;
    }

    @Column(length = 1000)
    private String memberNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team() {}

    public Team(String name) {
        this.name = name;
    }

    // getters/setters
}