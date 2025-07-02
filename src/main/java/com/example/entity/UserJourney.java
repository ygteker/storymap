package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class UserJourney extends PanacheEntity {
    public String title;

    @OneToMany(mappedBy = "userJourney", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<UserStep> userSteps;
}
