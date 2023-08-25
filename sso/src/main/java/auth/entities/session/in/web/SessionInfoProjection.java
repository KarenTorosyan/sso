package auth.entities.session.in.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.session.SessionInformation;

import java.time.Instant;

public record SessionInfoProjection(@JsonProperty("id") String id,
                                    @JsonProperty("isCurrent") boolean isCurrent,
                                    @JsonProperty("lastRequest") Instant lastRequest,
                                    @JsonProperty("remoteAddress") String remoteAddress,
                                    @JsonProperty("userAgent") String userAgent) {

    public static SessionInfoProjection from(SessionInformation sessionInformation, HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Forwarded-For");
        return new SessionInfoProjection(sessionInformation.getSessionId(),
                request.getSession().getId().equals(sessionInformation.getSessionId()),
                sessionInformation.getLastRequest().toInstant(),
                remoteAddr != null ? remoteAddr : request.getRemoteAddr(),
                request.getHeader("user-agent"));
    }
}
