package com.example.resource;

import com.example.dtos.AuthDTO;
import com.example.entity.StoryMapUser;
import com.example.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    //TODO correct return types and HTTP codes

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response register(@Valid AuthDTO dto) {
        StoryMapUser user = authService.register(dto);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid AuthDTO dto) {
        return Response.ok().entity(authService.login(dto)).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        return Response.ok(Map.of("message", "Logged out. Please remove the token.")).build();
    }
}
