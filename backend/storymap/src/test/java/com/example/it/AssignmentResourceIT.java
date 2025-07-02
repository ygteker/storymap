package com.example.it;

import com.example.entity.IssueAssignment;
import com.example.resource.AssignmentResource;
import com.example.service.UserStoryMapService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AssignmentResourceIT {

    @InjectMock
    UserStoryMapService userStoryMapService;

    private String generateToken() {
        try (InputStream is = getClass().getResourceAsStream("/privateKey.pem")) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            return Jwt.issuer("https://example.com/issuer")
                    .upn("test@quarkus.io")
                    .groups(Set.of("User"))
                    .claim("username", "testuser")
                    .sign(privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    @Test
    public void assign_returns201() {
        AssignmentResource.AssignmentPayload payload = new AssignmentResource.AssignmentPayload();
        payload.gitlabIssueId = 123L;
        payload.userStepId = 1L;
        payload.releaseId = null;

        IssueAssignment assignment = new IssueAssignment();
        assignment.gitlabIssueId = 123L;

        when(userStoryMapService.assignIssue(123L, 1L, null)).thenReturn(assignment);

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/assignments")
                .then()
                .statusCode(201)
                .body("gitlabIssueId", equalTo(123));
    }
}
