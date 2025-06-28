package com.example.resource;

import com.example.entity.IssueAssignment;
import com.example.service.UserStoryMapService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

@Path("/api/assignments")
@Consumes(MediaType.APPLICATION_JSON)
//@RolesAllowed("All")
public class AssignmentResource {

    public static class AssignmentPayload {
        public Long gitlabIssueId;
        public Long userStepId;
        public Long releaseId;
    }

    @Inject
    UserStoryMapService userStoryMapService;

    @POST
    public Response assign(AssignmentPayload payload) {
        IssueAssignment created = userStoryMapService.assignIssue(payload.gitlabIssueId, payload.userStepId, payload.releaseId);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}
