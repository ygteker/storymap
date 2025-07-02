package com.example.resource;

import com.example.service.GitlabService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/issues")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class IssueResource {
    @Inject
    GitlabService gitlabService;

    @GET
    public Response getIssues(@QueryParam("page") @DefaultValue("1") int page,
                              @QueryParam("size") @DefaultValue("20") int size) {
        return Response.ok().entity(gitlabService.fetchIssues(page, size)).build();
    }
}
