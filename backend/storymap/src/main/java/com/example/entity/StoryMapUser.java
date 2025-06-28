package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class StoryMapUser extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String username;

    @Column(nullable = false)
    public String passwordHash;

    public String role;
}
