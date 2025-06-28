package com.example.service;

import com.example.dtos.IssueDTO;
import com.example.dtos.UserJourneyDTO;
import com.example.dtos.UserStepDTO;
import com.example.entity.IssueAssignment;
import com.example.entity.Release;
import com.example.entity.UserJourney;
import com.example.entity.UserStep;
import io.quarkus.cache.CacheInvalidate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserStoryMapService {

    public List<UserJourneyDTO> buildMap() {
        return UserJourney.<UserJourney>listAll().stream().map(j -> {
            UserJourneyDTO dto = new UserJourneyDTO();
            dto.id = j.id;
            dto.title = j.title;
            dto.userSteps = mapUserSteps(j);
            return dto;
        }).toList();
    }

    private List<UserStepDTO> mapUserSteps(UserJourney journey) {
        return journey.userSteps.stream().map(step -> {
            UserStepDTO dto = new UserStepDTO();
            dto.id = step.id;
            dto.title = step.title;
            dto.issues = mapIssues(step);
            return dto;
        }).toList();
    }

    private List<IssueDTO> mapIssues(UserStep step) {
        return step.assignments.stream().map(assignment -> {
            IssueDTO dto = new IssueDTO();
            dto.id = assignment.id;
            /*TODO
                Hier würde ich den Titel, die Description und die Labels auch mappen,
                aber die IssueAssignment-Struktur hat keine solche Felder im Code Challenge definiert.
             */
            dto.gitlabIssueId = assignment.gitlabIssueId;
            dto.releaseName = assignment.release.name;
            return dto;
        }).toList();
    }

    @CacheInvalidate(cacheName = "gitlab-issues")
    @Transactional
    public IssueAssignment assignIssue(Long issueId, Long userStepId, Long releaseId) {
        UserStep userStep = UserStep.findById(userStepId);
        Release release = releaseId != null ? Release.findById(releaseId) : null;

        IssueAssignment existing = IssueAssignment.find("gitlabIssueId", issueId).firstResult();
        if (existing == null) {
            existing = new IssueAssignment();
            existing.gitlabIssueId = issueId;
            existing.userStep = userStep;
            existing.release = release;
            existing.persist();
        }



        return existing;
    }
}

