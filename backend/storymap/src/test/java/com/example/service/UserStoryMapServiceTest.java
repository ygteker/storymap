package com.example.service;

import com.example.dtos.UserJourneyDTO;
import com.example.entity.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStoryMapServiceTest {

    @Inject
    UserStoryMapService userStoryMapService;

    @Inject
    UserTransaction userTransaction;

    @BeforeAll
    @Transactional
    void setup() {
        IssueAssignment.deleteAll();
        Release.deleteAll();
        UserStep.deleteAll();
        UserJourney.deleteAll();
    }

    @Test
    void testBuildMap_withData() throws SystemException, NotSupportedException {
        userTransaction.begin();

        Release release = new Release();
        release.name = "v1.0";
        release.persist();

        IssueAssignment assignment = new IssueAssignment();
        assignment.gitlabIssueId = 123L;
        assignment.release = release;

        UserStep userStep = new UserStep();
        userStep.title = "Step 1";
        userStep.assignments = List.of(assignment);
        assignment.userStep = userStep;

        userStep.persist();
        assignment.persist();

        UserJourney userJourney = new UserJourney();
        userJourney.title = "Journey 1";
        userJourney.userSteps = List.of(userStep);
        userStep.userJourney = userJourney;

        userJourney.persist();

        userJourney.getEntityManager().flush();
        userJourney.getEntityManager().clear();

        List<UserJourneyDTO> result = userStoryMapService.buildMap();

        assertEquals(1, result.size());
        assertEquals("Journey 1", result.getFirst().title);
        assertEquals(1, result.getFirst().userSteps.size());
        assertEquals(123L, result.getFirst().userSteps.getFirst().issues.getFirst().gitlabIssueId);

        userTransaction.rollback();
    }

    @Test
    void assignIssue_shouldCreateNewAssignment() throws SystemException, NotSupportedException {
        userTransaction.begin();
        UserStep userStep = new UserStep();
        userStep.title = "Test Step";
        userStep.persist();

        Release release = new Release();
        release.name = "v2.0";
        release.persist();

        Long issueId = 999L;

        IssueAssignment assignment = userStoryMapService.assignIssue(issueId, userStep.id, release.id);

        assertEquals(issueId, assignment.gitlabIssueId);
        assertEquals(userStep.id, assignment.userStep.id);
        assertEquals(release.id, assignment.release.id);
        userTransaction.rollback();
    }

    @Test
    void assignIssue_shouldWorkWithoutRelease() throws SystemException, NotSupportedException {
        userTransaction.begin();
        UserStep userStep = new UserStep();
        userStep.title = "NO RELEASE";
        userStep.persist();

        Long issueId = 999L;
        IssueAssignment assignment = userStoryMapService.assignIssue(issueId, userStep.id, null);
        assertEquals(issueId, assignment.gitlabIssueId);
        assertEquals(userStep.id, assignment.userStep.id);
        assertNull(assignment.release);
        userTransaction.rollback();
    }

}
