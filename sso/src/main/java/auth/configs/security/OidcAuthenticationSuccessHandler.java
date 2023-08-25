package auth.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.function.Consumer;

public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Consumer<OAuth2User> oAuth2UserConsumer = oAuth2User -> {
    };
    private Consumer<OidcUser> oidcUserConsumer = oidcUser -> {
    };

    private String defaultSuccessUrl;

    private boolean alwaysRedirectToDefaultSuccessUrl;

    private final SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        if (authentication instanceof OAuth2AuthenticationToken) {

            if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
                oidcUserConsumer.accept(oidcUser);
            }

            if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
                oAuth2UserConsumer.accept(oAuth2User);
            }
        }
        if (defaultSuccessUrl != null) {
            savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl(defaultSuccessUrl);
            savedRequestAwareAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(alwaysRedirectToDefaultSuccessUrl);
        }
        savedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    public OidcAuthenticationSuccessHandler whenOAuth2User(Consumer<OAuth2User> oAuth2UserConsumer) {
        this.oAuth2UserConsumer = oAuth2UserConsumer;
        return this;
    }

    public OidcAuthenticationSuccessHandler whenOidcUser(Consumer<OidcUser> oidcUserConsumer) {
        this.oidcUserConsumer = oidcUserConsumer;
        return this;
    }

    public OidcAuthenticationSuccessHandler defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysRedirectToDefaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
        this.alwaysRedirectToDefaultSuccessUrl = alwaysRedirectToDefaultSuccessUrl;
        return this;
    }

    public OidcAuthenticationSuccessHandler defaultSuccessUrl(String defaultSuccessUrl) {
        return defaultSuccessUrl(defaultSuccessUrl, false);
    }
}
