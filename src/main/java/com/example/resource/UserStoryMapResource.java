package com.example.resource;

import com.example.dtos.UserJourneyDTO;
import com.example.service.UserStoryMapService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/user-story-map")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class UserStoryMapResource {

    @Inject
    UserStoryMapService userStoryMapService;

    @GET
    public List<UserJourneyDTO> getUserStoryMap() {
        return userStoryMapService.buildMap();
    }
}

