package com.example.service;

import com.example.client.GitlabClient;
import com.example.dtos.IssueDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@QuarkusTest
public class GiitlabServiceTest {

    @InjectMock
    @RestClient
    GitlabClient gitlabClient;

    @Inject
    GitlabService gitlabService;

    @Test
    void fetchIssues_shouldReturnListOfIssues() {
        List<IssueDTO> mockResponse = List.of(new IssueDTO(1L, "Test", "description", new String[]{"label"}, "releaseName", 1L));
        when(gitlabClient.getIssues(anyString(), eq(1), eq(20), anyString()))
                .thenReturn(mockResponse);

        List<IssueDTO> result = gitlabService.fetchIssues(1, 20);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test", result.getFirst().title);
    }

    @Test
    void testGitlabIssueCaching() {
        var issues1 = gitlabService.fetchIssues(1, 10);
        var issues2 = gitlabService.fetchIssues(1, 10);

        assertSame(issues1, issues2, "Expected cached result to be reused");
    }
}
