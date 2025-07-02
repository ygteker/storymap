package com.example.it;

import com.example.dtos.IssueDTO;
import com.example.service.GitlabService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;


@QuarkusTest
public class IssueResourceIT {

    @InjectMock
    GitlabService gitlabService;

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
    public void getIssues_returns200() {
        IssueDTO issue1 = new IssueDTO(123L, "title", "desc", new String[]{"label1", "label2"}, "v1.0", 1L);
        IssueDTO issue2 = new IssueDTO(124L, "title2", "desc2", new String[]{"label1", "label2"}, "v2.0", 2L);

        when(gitlabService.fetchIssues(1, 20)).thenReturn(List.of(issue1, issue2));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                .contentType("application/json")
                .when()
                .get("/api/issues")
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }
}
