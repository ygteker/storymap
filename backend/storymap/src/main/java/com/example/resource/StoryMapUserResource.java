package com.example.resource;

import com.example.entity.StoryMapUser;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class StoryMapUserResource {

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getUser() {
        String username = jwt.getClaim("username");
        StoryMapUser user = StoryMapUser.find("username", username).firstResult();
        System.out.println(user.username);
        return Response.ok().entity(user).build();
    }
}
