package com.example.it;

import com.example.entity.StoryMapUser;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
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
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
public class StoryMapUserResourceIT {

    @Inject
    UserTransaction userTransaction;

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
    public void getUser_returns200() throws SystemException, NotSupportedException {
        userTransaction.begin();
        StoryMapUser user = new StoryMapUser();
        user.username = "testuser";
        user.passwordHash = "testhash";
        user.role = "User";
        user.persist();

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                .contentType("application/json")
                .when()
                .get("/user")
                .then()
                .log().body()
                .statusCode(200);

        userTransaction.rollback();
    }
}
