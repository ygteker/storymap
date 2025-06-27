package com.example.client;

import com.example.dtos.IssueDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/projects/{projectId}/issues")
@RegisterRestClient(configKey = "gitlab-api")
@Produces(MediaType.APPLICATION_JSON)
public interface GitlabClient {

    @GET
    List<IssueDTO> getIssues(@PathParam("projectId") String projectId,
                             @QueryParam("page") @DefaultValue("1") int page,
                             @QueryParam("per_page") @DefaultValue("20") int perPage);
}
