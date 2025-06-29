package com.example.service;

import com.example.dtos.AuthDTO;
import com.example.dtos.TokenDTO;
import com.example.entity.StoryMapUser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "PRIVATE_KEY")
    String privateKeyBse64;

    @Transactional
    public StoryMapUser register(AuthDTO dto) {
        if (StoryMapUser.find("username", dto.username).firstResult() != null) {
            throw new WebApplicationException("User already exists", 409);
        }

        StoryMapUser storyMapUser = new StoryMapUser();
        storyMapUser.username = dto.username;
        storyMapUser.passwordHash = encode(dto.password);
        storyMapUser.role = "User";
        storyMapUser.persist();
        return storyMapUser;
    }

    public TokenDTO login(AuthDTO dto) {
        StoryMapUser storyMapUser = StoryMapUser.find("username", dto.username).firstResult();
        if (storyMapUser == null) {
            throw new WebApplicationException("User not found", 404);
        }
        if (!matches(dto.password, storyMapUser.passwordHash)) {
            throw new WebApplicationException("Wrong credentials", 401);
        }

        String token = generateToken(storyMapUser.username, storyMapUser.role);
        return new TokenDTO(token);
    }

    public String generateToken(String username, String role) {
        String token = Jwt.issuer("https://example.com/issuer")
                .upn("jdoe@quarkus.io")
                .groups(new HashSet<>(Collections.singletonList(role)))
                .claim("username", username)
                .sign(loadPrivateKey());
        return token;
    }

    public PrivateKey loadPrivateKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBse64);
            String pem = new String(keyBytes, StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key", e);
        }
    }

    public String encode(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    public boolean matches(String raw, String encoded) {
        return encode(raw).equals(encoded);
    }
}
