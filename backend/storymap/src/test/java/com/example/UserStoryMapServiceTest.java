package com.example;

import com.example.dtos.UserJourneyDTO;
import com.example.entity.IssueAssignment;
import com.example.entity.Release;
import com.example.entity.UserJourney;
import com.example.entity.UserStep;
import com.example.service.UserStoryMapService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@QuarkusTest
public class UserStoryMapServiceTest {

    @Inject
    UserStoryMapService userStoryMapService;

    //TODO cannot get Mockito working with Panache, I will try if I get more time
    @Test
    @Disabled
    void testBuildMap_withData() {
        UserJourney journey = new UserJourney();
        journey.id = 1L;
        journey.title = "Journey 1";

        UserStep step = new UserStep();
        step.id = 2L;
        step.title = "Step 1";
        journey.userSteps = List.of(step);

        IssueAssignment assignment = new IssueAssignment();
        assignment.id = 3L;
        assignment.gitlabIssueId = 123L;

        Release release = new Release();
        release.id = 4L;
        release.name = "v1.0";
        assignment.release = release;

        step.assignments = List.of(assignment);

        try (MockedStatic<UserJourney> userJourneyMock = mockStatic(UserJourney.class)) {
            userJourneyMock.when(UserJourney::listAll).thenReturn(List.of(journey));

            List<UserJourneyDTO> result = userStoryMapService.buildMap();

            assertEquals(1, result.size());
            assertEquals("Journey 1", result.get(0).title);
            assertEquals(1, result.get(0).userSteps.size());
            assertEquals(123L, result.get(0).userSteps.get(0).issues.get(0).gitlabIssueId);
        }
    }

}
