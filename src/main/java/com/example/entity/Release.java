package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
public class Release extends PanacheEntity {

    public String name;

    @OneToMany(mappedBy = "release")
    public List<IssueAssignment> assignments;
}
