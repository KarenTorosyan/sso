package auth.entities.session.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.session.SessionInformation;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionInfoProjectionTest {

    private static final String USER_AGENT = "User-Agent";

    @Test
    void shouldBuildResponse() {
        SessionInformation sessionInfo = new SessionInformation("principal", "sessionId", new Date());
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setSession(new MockHttpSession(null, sessionInfo.getSessionId()));
        httpServletRequest.addHeader(USER_AGENT, "agent");
        httpServletRequest.setRemoteAddr("45.15.149.171");
        SessionInfoProjection sessionInfoProjection = SessionInfoProjection.from(sessionInfo, httpServletRequest);
        System.out.println(sessionInfoProjection);
        assertThat(sessionInfoProjection).matches(projection ->
                match(sessionInfoProjection, sessionInfo, httpServletRequest));
    }

    private boolean match(SessionInfoProjection sessionInfoProjection, SessionInformation sessionInfo,
                          MockHttpServletRequest httpServletRequest) {
        return sessionInfoProjection.id().equals(sessionInfo.getSessionId()) &&
                sessionInfoProjection.isCurrent() &&
                sessionInfoProjection.lastRequest().equals(sessionInfo.getLastRequest().toInstant()) &&
                sessionInfoProjection.userAgent().equals(httpServletRequest.getHeader(USER_AGENT)) &&
                sessionInfoProjection.remoteAddress().equals(httpServletRequest.getRemoteAddr());
    }
}
