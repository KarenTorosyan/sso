package auth.mock;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Set;

public class MockOidcClient {

    public RegisteredClient mock(String clientId, String... scopes) {
        return RegisteredClient.withId(clientId)
                .clientId(clientId)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://localhost:4200")
                .scopes(c -> c.addAll(Set.of(scopes)))
                .build();
    }
}
