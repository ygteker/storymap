package com.example.it;

import com.example.dtos.AuthDTO;
import com.example.dtos.TokenDTO;
import com.example.entity.StoryMapUser;
import com.example.service.AuthService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AuthServiceIT {
    @InjectSpy
    AuthService authService;

    @Inject
    UserTransaction userTransaction;

    @Test
    public void encode_shouldEncodePasswordCorrectly() {
        String encoded = authService.encode("secret");
        assertEquals(Base64.getEncoder().encodeToString("secret".getBytes(StandardCharsets.UTF_8)), encoded);
    }

    @Test
    public void matches_shouldReturnTrueForCorrectMatch() {
        String encoded = authService.encode("secret");
        assertTrue(authService.matches("secret", encoded));
    }

    @Test
    public void matches_shouldReturnFalseForWrongPassword() {
        String encoded = authService.encode("secret");
        assertFalse(authService.matches("wrong", encoded));
    }

    @Test
    public void login_shouldReturnTokenWhenCredentialsAreCorrect() throws SystemException, NotSupportedException {
        userTransaction.begin();
        // Arrange
        AuthDTO dto = new AuthDTO();
        dto.username = "user";
        dto.password = "password";

        StoryMapUser user = new StoryMapUser();
        user.username = "user";
        user.passwordHash = Base64.getEncoder()
                .encodeToString("password".getBytes(StandardCharsets.UTF_8));
        user.role = "User";
        user.persist();

        TokenDTO token = authService.login(dto);
        assertNotNull(token.token);
        assertTrue(token.token.startsWith("ey"));
        userTransaction.rollback();
    }


    @Test
    public void login_shouldThrow404WhenUserNotFound() {
        AuthDTO dto = new AuthDTO();
        dto.username = "notfound";
        dto.password = "password";

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> authService.login(dto));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    public void login_shouldThrow401WhenPasswordWrong() throws SystemException, NotSupportedException {
        userTransaction.begin();
        AuthDTO dto = new AuthDTO();
        dto.username = "user";
        dto.password = "wrong";

        StoryMapUser user = new StoryMapUser();
        user.username = "user";
        user.passwordHash = authService.encode("correct");
        user.role = "User";
        user.persist();

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> authService.login(dto));
        assertEquals(401, ex.getResponse().getStatus());
        userTransaction.rollback();
    }

    @Test
    public void register_shopuldReturnUser() throws SystemException, NotSupportedException {
        userTransaction.begin();
        AuthDTO dto = new AuthDTO();
        dto.username = "username";
        dto.password = "password";

        StoryMapUser user = authService.register(dto);
        String hash = Base64.getEncoder()
                .encodeToString("password".getBytes(StandardCharsets.UTF_8));

        assertEquals(user.username, dto.username);
        assertEquals(user.passwordHash, hash);
        assertEquals(user.role, "User");

        StoryMapUser dbUser = StoryMapUser.find("username", dto.username).firstResult();
        assertNotNull(dbUser);

        userTransaction.rollback();
    }

    @Test
    public void register_shouldThrow409() throws SystemException, NotSupportedException {
        userTransaction.begin();

        AuthDTO dto = new AuthDTO();
        dto.username = "username";
        dto.password = "password";

        StoryMapUser user = new StoryMapUser();
        user.username = "username";
        user.passwordHash = authService.encode("password");
        user.role = "User";
        user.persist();

        assertThrows(WebApplicationException.class, () -> authService.register(dto));
        userTransaction.rollback();
    }

}
