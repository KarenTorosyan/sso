package auth.configs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionInfo {

    private static final String DEFAULT_SESSION_COOKIE_NAME = "JSESSIONID";

    private final ServerProperties serverProperties;

    public String getSessionCookieName() {
        return Optional.ofNullable(getCookie().getName())
                .orElse(DEFAULT_SESSION_COOKIE_NAME);
    }

    private Session.Cookie getCookie() {
        return serverProperties.getServlet().getSession().getCookie();
    }
}
