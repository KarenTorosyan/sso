package auth.entities.session.in.web;

import auth.Endpoints;
import auth.configs.security.TypedUserDetails;
import auth.configs.web.SessionInfo;
import auth.errors.Errors;
import auth.utils.MessageSourceUtils;
import auth.utils.ObjectMapperUtils;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.test.web.servlet.MockMvc;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.mock.MockPrincipal;

import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionRegistryRestController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class SessionRegistryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionInfo sessionInfo;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private SessionRegistry sessionRegistry;

    @Test
    void shouldGetSessions() throws Exception {
        TypedUserDetails principal = MockPrincipal.typedUserDetails();
        List<SessionInformation> sessions = List.of(
                new SessionInformation(principal, "sessionId", new Date())
        );
        List<SessionInfoProjection> sessionProjections = sessions.stream()
                .map(sessionInfo -> SessionInfoProjection.from(sessionInfo, new MockHttpServletRequest()))
                .toList();
        boolean includeExpiredSessions = false;
        given(sessionRegistry.getAllSessions(principal, includeExpiredSessions)).willReturn(sessions);
        mockMvc.perform(get(Endpoints.SESSIONS)
                        .with(MockPrincipal.authorization()))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(sessionProjections)));
        verify(sessionRegistry).getAllSessions(principal, includeExpiredSessions);
    }

    @Test
    void shouldDeleteSessionBySessionId() throws Exception {
        String sessionId = "sessionId";
        SessionInformation sessionInformation = new SessionInformation(MockPrincipal.typedUserDetails(), sessionId, new Date());
        given(sessionRegistry.getSessionInformation(sessionId)).willReturn(sessionInformation);
        mockMvc.perform(delete(Endpoints.SESSIONS + "/{sessionId}", sessionId)
                        .with(MockPrincipal.authorization())
                        .with(csrf())
                        .cookie(new Cookie(sessionInfo.getSessionCookieName(), sessionId)))
                .andExpect(status().isNoContent())
                .andExpect(cookie().doesNotExist(sessionInfo.getSessionCookieName()));
        verify(sessionRegistry).getSessionInformation(sessionId);
    }

    @Test
    void shouldReceiveErrorResponseWhenSessionByIdNotExists() throws Exception {
        String sessionId = "sessionId";
        given(sessionRegistry.getSessionInformation(sessionId)).willReturn(null);
        mockMvc.perform(delete(Endpoints.SESSIONS + "/{sessionId}", sessionId)
                        .with(MockPrincipal.authorization())
                        .with(csrf())
                        .cookie(new Cookie(sessionInfo.getSessionCookieName(), sessionId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value(MessageSourceUtils.localize(messageSource, Errors.sessionNotFound(sessionId))))
                .andExpect(jsonPath("$.uri").value(Matchers.endsWith(Endpoints.SESSIONS + "/" + sessionId)))
                .andExpect(jsonPath("$.datetime").isNotEmpty());
        verify(sessionRegistry).getSessionInformation(sessionId);
    }
}
