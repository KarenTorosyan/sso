package auth.views;

import auth.Endpoints;
import auth.configs.security.TypedUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.web.servlet.MockMvc;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.mock.MockOidcClient;
import auth.mock.MockPrincipal;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorizationConsentController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class AuthorizationConsentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisteredClientRepository registeredClientRepository;

    private final MockOidcClient mockOidcClient = new MockOidcClient();

    @Test
    void shouldRenderAuthorizationConsentPage() throws Exception {
        String clientId = "{clientId}";
        String requestedScopes = "openid profile";
        Set<String> scopesToApprove = Set.of("profile");
        String state = "{state}";
        RegisteredClient registeredClient = mockOidcClient.mock(clientId, requestedScopes.split(" "));
        given(registeredClientRepository.findByClientId(clientId)).willReturn(registeredClient);
        TypedUserDetails principal = MockPrincipal.typedUserDetails();
        mockMvc.perform(get(Endpoints.AUTHORIZATION_CONSENT)
                        .with(user(principal))
                        .param(OAuth2ParameterNames.CLIENT_ID, clientId)
                        .param(OAuth2ParameterNames.SCOPE, requestedScopes)
                        .param(OAuth2ParameterNames.STATE, state))
                .andExpect(status().isOk())
                .andExpect(view().name("consent"))
                .andExpect(model().attribute("clientId", clientId))
                .andExpect(model().attribute("scopes", scopesToApprove))
                .andExpect(model().attribute("state", state))
                .andExpect(model().attribute("principalName", principal.getName()));
        verify(registeredClientRepository).findByClientId(clientId);
    }
}
