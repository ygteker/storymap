package com.example.service;

import com.example.dtos.AuthDTO;
import com.example.entity.StoryMapUser;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @Inject
    UserTransaction userTransaction;

    @BeforeEach
    void setup() {
        String DUMMY_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nLS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRQ2wraUpCSGlJZmhIMngKc3ZZZFZuOXpWQW5rN05rVk9UbmhVSWpLeHBpZUNoTThVdi9IckJsUjNsRm5QWnRZcXM3KzRTVFNQTVAyQnJ4aApETHRhZ3VqWDVkOVlPV2ZyNG1RWm83Nlc3SVNaOUlqRjVhK2c5eEdzZFNCSGpSR3JvMWlKUjNOVG44T3lObGkwCjE1MFQzWDZScFVUWW45V2Z4V3NyOEo0N05mbFJIU3dsS0lTTU9uZHZZdkRNMkU4RE9CSWFwWlNhMk9wcU1BWmYKUE5sam9UQnEzWUJqRkNNZytHV3NiL3BZcEVubWxpblI0ZWJ1Z3BaTlVlOEpBS2V3UjhvZDBGTGo1c3pNbVFzOQpLcE9TaERtWDBXVUE1WXgxcFdPK2MzNXdBL25hR2hmbWJ6NnRmSU1XWHpVZjNpMVRPdjV2M1EzcmpXc3NKQ1JyCnIyekNrMU52QWdNQkFBRUNnZ0VBTTZGWU4zNXFxZmc2YTJVYXB5eXBqOVFVRWRqVmZJTWxMS0F6TlRUSzg5cDUKZWxudFJBMFdnVCtSZmZhTmxPcnBZaUxpSTk0UUxUK1NLUFJmY1h2b2Q0U0QwZnRsaHR5Uk8rS1NYbnRya2tLZApxMFhVT05PeWRUMm9FV25pRmN6RnRVcDdKd2U3UmZ3ZTR2NXhWK1VlUXFzdys5dlloWTlSVThWUlMyVHlUZGN1CmU3cHUzd2dNeEFhSXRidW5URm9PM3YwVm9LdmdLVW5zdUdRc1NCM3NxVmZaNGljTFkyajErN3JqbGZoVWtzU1QKQVJ0cXNNY1pyVnBiRXpiamZWaU9LYzhXLzIySGJPcVpQaHRhMGV5UFdsTDFnNUFjVFNmZWovbnVqZEQvRDZLKwptUUhWZkF5eHpCaE9pc1VHWXhSQVFqckwxTUZFUHA4ek5QeG1ScjltVlFLQmdRRHFrdUk2bU9JcWZtbFVrV3I4CkNZUTEvaTk0ZVNYQnh3NHc0eEo4WE5JWjJZbW1hejJvTXFqMlRQdHl4Z1ArSURHeG1FSUY1NEFXQktnalNyR2oKZktOc0hWb1JVNmtjMklIbU5PTFkxcFZjOXUwdm85UkFCdCtSc3o4YW8vQ1VTUjlNb0tsU3VWV2ZYVVA1Ynk4Sgpkdk5BZlQ1VUxRWEtmdm9admREenk0NVZoUUtCZ1FDMUl6c3NwR2Q5dnBLTEJOckhtZjRPNnVSUlhYOXY3RkpNCmY5bzJ6R2IzTHYydUlXUVRmWTlYTkRKaFArTVFFZVhZS1N0TW4xYk9LS0VNOCtYRHZGak1ETGIzYVlVbGluN2EKaUkxVjY1MWZ0QTVvemVzeDd2TVVwTHltSnp3dG55d0Exc1lDZTZmaStvS0taR3d4NnNha1labmIzS1Fia0ZpSgprWk4yM0JhTll3S0JnRlRGYUVSTTQ4Ny9pQWtyMUVIcEhvSjByYXNYL1BFZWdwNWdNVm9JSVZWK0xDU2NOTmdJCkZzK0wyb01MMUgyT0hPZFZZdWJIVnA2ZWFEVDQxV0dEdFh0bzBCZmE2QTZvbjZrV0M3VFpOM2sxTTFvNEZvaGMKODRhd0JHb1lQT1VaMTluVlpkdWpacklFL24reUVvbVZGeXVERjNkTUhmK2tDN1lweHMyVURvZ1JBb0dCQUxROAprVFh6aE5MWEhNUGRtY0JBMTA2S3BaTGNvT0NDV3NXcDlqek9tS25laFNlT0xDRkVyNnVLMFpKNDJudjBBeXRhCms2NFRZbkdTZGVZSWpoaDh3aktQZUVPcEtJWlpUNjR0YjgwTnZETXRXNlVuT0o4Zjc1b2I3V2E2NWFOVG5acGMKR3Z6L2crZFRjeTgzaTMwRDZwSklWNnN3MmM1ZmkrbWZCQWVpS1lLWEFvR0FIZHpyOFFSdXM0MFdGbWo2MG9EagpPcW5XKzFaSXIrTjBkc3AyZmNFL3ZHWWs3OSs0dU8yUkFCcEc2MlBPS09GUU1GQUtaSjYvdEplOVZaUTNxaVhZCms1SE5BR3E5V0poWlFVMlE3SFRSQ3J0V2NCL1dCc3FnQkg1OUl0d3JiNXFOUis4OTU4NE9EdXVvdHEwMGJuOHkKVDBvbHRXOGs2MHMwL3VTZTZXVEpTLzg9Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K\n-----END PRIVATE KEY-----";
        System.setProperty("PRIVATE_KEY", Base64.getEncoder().encodeToString(DUMMY_PRIVATE_KEY.getBytes()));
    }

    @Test
    void testEncodeDecodeMatches() {
        String password = "secret";
        String encoded = authService.encode(password);
        assertTrue(authService.matches(password, encoded));
    }

    @Test
    void testRegisterThrowsIfUserExists() throws SystemException, NotSupportedException {
        userTransaction.begin();
        AuthDTO dto = new AuthDTO();
        dto.username = "existing";
        dto.password = "secret";

        StoryMapUser existing = new StoryMapUser();
        existing.username = "existing";
        existing.passwordHash = "secretHash";
        existing.persist();

        assertThrows(WebApplicationException.class, () -> authService.register(dto));
        userTransaction.rollback();
    }

    @Test
    void testLoginFailsIfUserNotFound() {
        AuthDTO dto = new AuthDTO();
        dto.username = "nonexistent";
        dto.password = "secret";

        assertThrows(WebApplicationException.class, () -> authService.login(dto));
    }
}
