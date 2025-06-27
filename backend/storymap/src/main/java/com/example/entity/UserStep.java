package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserStep extends PanacheEntity {
    public String title;

    @ManyToOne
    @JoinColumn(name = "user_journey_id")
    public UserJourney userJourney;

    @OneToMany(mappedBy = "userStep")
    public List<IssueAssignment> assignments;
}
