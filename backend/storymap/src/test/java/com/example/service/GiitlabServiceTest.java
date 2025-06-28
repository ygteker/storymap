package com.example.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;


@QuarkusTest
public class GiitlabServiceTest {

    @Inject
    GitlabService gitlabService;

    @Test
    void testGitlabIssueCaching() {
        var issues1 = gitlabService.fetchIssues(1, 10);
        var issues2 = gitlabService.fetchIssues(1, 10);

        assertSame(issues1, issues2, "Expected cached result to be reused");
    }
}
