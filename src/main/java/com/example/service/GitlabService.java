package com.example.service;

import com.example.client.GitlabClient;
import com.example.dtos.IssueDTO;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class GitlabService {

    @Inject
    @RestClient
    GitlabClient gitlabClient;

    @ConfigProperty(name = "gitlab.project-id")
    String projectId;

    @ConfigProperty(name = "gitlab.token")
    String token;

    @CacheResult(cacheName = "gitlab-issues")
    public List<IssueDTO> fetchIssues(int page, int size) {
        return gitlabClient.getIssues(projectId, page, size, token);
    }
}
