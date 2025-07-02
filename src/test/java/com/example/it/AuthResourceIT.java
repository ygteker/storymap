package com.example.it;

import com.example.dtos.AuthDTO;
import com.example.dtos.TokenDTO;
import com.example.entity.StoryMapUser;
import com.example.service.AuthService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AuthResourceIT {

    @InjectMock
    AuthService authService;

    @Test
    public void register_returns201() {
        StoryMapUser dummyUser = new StoryMapUser();
        AuthDTO dto = new AuthDTO();
        dto.username = "username";
        dto.password = "password";

        when(authService.register(any())).thenReturn(dummyUser);

        given()
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);
    }

    static Stream<AuthDTO> invalidUsers() {
        AuthDTO user1 = new AuthDTO();
        user1.username = null;
        user1.password = "pass";

        AuthDTO user2 = new AuthDTO();
        user2.username = "user";
        user2.password = null;

        AuthDTO user3 = new AuthDTO();
        user3.username = "";
        user3.password = "";

        AuthDTO user4 = new AuthDTO();
        user4.username = "u";
        user4.password = "password";

        AuthDTO user5 = new AuthDTO();
        user5.username = "username";
        user5.password = "a";

        return Stream.of(user1, user2, user3, user4, user5);
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void register_returns400(AuthDTO dto) {
        StoryMapUser dummyUser = new StoryMapUser();

        when(authService.register(any())).thenReturn(dummyUser);

        given()
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void login_returns200() {
        TokenDTO tokenDTO = new TokenDTO("TOKEN");
        AuthDTO dto = new AuthDTO();
        dto.username = "username";
        dto.password = "password";

        when(authService.login(any())).thenReturn(tokenDTO);

        given()
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200);
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void login_returns400(AuthDTO dto) {
        TokenDTO tokenDTO = new TokenDTO("TOKEN");

        when(authService.login(any())).thenReturn(tokenDTO);

        given()
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400);
    }

    @Test
    public void logout_returns200() {
        given()
                .contentType("application/json")
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(200);
    }
}
