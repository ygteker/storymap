package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class IssueAssignment extends PanacheEntity {

    public Long gitlabIssueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_step_id")
    public UserStep userStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id")
    public Release release;
}
