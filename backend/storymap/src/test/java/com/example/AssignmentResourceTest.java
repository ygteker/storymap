package com.example;

import com.example.entity.IssueAssignment;
import com.example.resource.AssignmentResource;
import com.example.service.UserStoryMapService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AssignmentResourceTest {

    @InjectMock
    UserStoryMapService userStoryMapService;

    @Test
    void assign_returns201() {
        AssignmentResource.AssignmentPayload payload = new AssignmentResource.AssignmentPayload();
        payload.gitlabIssueId = 123L;
        payload.userStepId = 1L;
        payload.releaseId = null;

        IssueAssignment mockAssignment = new IssueAssignment();
        mockAssignment.gitlabIssueId = 123L;

        when(userStoryMapService.assignIssue(123L, 1L, null))
                .thenReturn(mockAssignment);

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/assignments")
                .then()
                .statusCode(201)
                .body("gitlabIssueId", Matchers.equalTo(123));
    }
}
