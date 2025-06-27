package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
public class UserJourney extends PanacheEntity {
    public String title;

    @OneToMany(mappedBy = "userJourney")
    public List<UserStep> userSteps;
}
