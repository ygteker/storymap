package com.example.resource;

import com.example.service.UserStoryMapService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/api/assignments")
@Consumes(MediaType.APPLICATION_JSON)
public class AssignmentResource {

    public static class AssignmentPayload {
        public Long gitlabIssueId;
        public Long userStepId;
        public Long releaseId;
    }

    @Inject
    UserStoryMapService userStoryMapService;

    @POST
    public void assign(AssignmentPayload payload) {
        userStoryMapService.assignIssue(payload.gitlabIssueId, payload.userStepId, payload.releaseId);
    }
}
