package auth.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

public class DefaultSuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    private String defaultSuccessUrl;

    private boolean alwaysRedirectToDefaultSuccessUrl;

    private boolean createCsrfToken = true;

    private final SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        if (createCsrfToken) {
            createCsrfToken(request, response);
        }
        if (defaultSuccessUrl != null) {
            savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl(defaultSuccessUrl);
            savedRequestAwareAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(alwaysRedirectToDefaultSuccessUrl);
        }
        savedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    private void createCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfToken csrfToken = cookieCsrfTokenRepository.generateToken(request);
        cookieCsrfTokenRepository.saveToken(csrfToken, request, response);
    }

    public DefaultSuccessAuthenticationHandler defaultSuccessUrl(String defaultSuccessUrl) {
        return defaultSuccessUrl(defaultSuccessUrl, false);
    }

    public DefaultSuccessAuthenticationHandler defaultSuccessUrl(String defaultSuccessUrl,
                                                                 boolean alwaysRedirectToDefaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
        this.alwaysRedirectToDefaultSuccessUrl = alwaysRedirectToDefaultSuccessUrl;
        return this;
    }

    public DefaultSuccessAuthenticationHandler createCsrfToken(boolean createCsrfToken) {
        this.createCsrfToken = createCsrfToken;
        return this;
    }
}
