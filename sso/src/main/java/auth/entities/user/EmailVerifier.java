package auth.entities.user;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface EmailVerifier {
    void verify(HttpServletRequest request, Email email);
}
