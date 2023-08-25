package auth.entities.session.in.web;

import auth.Endpoints;
import auth.configs.security.SecurityAttributes;
import auth.configs.web.SessionInfo;
import auth.docs.openapi.NotDocumentedSchema;
import auth.docs.openapi.RestDocumentedDeleteMappingRequireAuthentication;
import auth.docs.openapi.RestDocumentedGetMappingResponseCollectionRequireAuthentication;
import auth.errors.Errors;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.SESSIONS)
@Tag(name = "Session")
@RequiredArgsConstructor
public class SessionRegistryRestController {

    private final SessionRegistry sessionRegistry;

    private final SessionInfo sessionInfo;

    @GetMapping
    @RestDocumentedGetMappingResponseCollectionRequireAuthentication
    ResponseEntity<List<SessionInfoProjection>> getSessions(@AuthenticationPrincipal AuthenticatedPrincipal principal,
                                                            @NotDocumentedSchema HttpServletRequest request) {
        return ResponseEntity.ok(sessionRegistry.getAllSessions(principal, false).stream()
                .map(sessionInfo -> SessionInfoProjection.from(sessionInfo, request))
                .toList());
    }

    @DeleteMapping("/{sessionId}")
    @RestDocumentedDeleteMappingRequireAuthentication
    ResponseEntity<Void> deleteSession(@PathVariable String sessionId, @NotDocumentedSchema HttpServletRequest request) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInformation == null) {
            throw Errors.sessionNotFound(sessionId);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        if (sessionInformation.getSessionId().equals(request.getSession().getId())) {
            request.getSession().invalidate();
            String expire = "=none; path=/; max-age=0;";
            httpHeaders.add(HttpHeaders.SET_COOKIE, sessionInfo.getSessionCookieName().concat(expire));
            httpHeaders.add(HttpHeaders.SET_COOKIE, SecurityAttributes.REMEMBER_ME_COOKIE_NAME.concat(expire));
        } else {
            sessionInformation.expireNow();
        }
        return ResponseEntity.noContent()
                .headers(httpHeaders)
                .build();
    }
}
