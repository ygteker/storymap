package com.example.resource;

import com.example.dtos.IssueDTO;
import com.example.service.GitlabService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/issues")
@Produces(MediaType.APPLICATION_JSON)
//@RolesAllowed("User")
public class IssueResource {
    @Inject
    GitlabService gitlabService;

    @GET
    public List<IssueDTO> getIssues(@QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("size") @DefaultValue("20") int size) {
        return gitlabService.fetchIssues(page, size);
    }
}
