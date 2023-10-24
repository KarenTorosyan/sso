package auth.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.function.Consumer;

public class OidcAuthenticationSuccessHandler extends DefaultSuccessAuthenticationHandler {

    private Consumer<OAuth2User> oAuth2UserConsumer = oAuth2User -> {
    };
    private Consumer<OidcUser> oidcUserConsumer = oidcUser -> {
    };

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        if (authentication instanceof OAuth2AuthenticationToken) {

            if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
                oidcUserConsumer.accept(oidcUser);
            }

            if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
                oAuth2UserConsumer.accept(oAuth2User);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public OidcAuthenticationSuccessHandler whenOAuth2User(Consumer<OAuth2User> oAuth2UserConsumer) {
        this.oAuth2UserConsumer = oAuth2UserConsumer;
        return this;
    }

    public OidcAuthenticationSuccessHandler whenOidcUser(Consumer<OidcUser> oidcUserConsumer) {
        this.oidcUserConsumer = oidcUserConsumer;
        return this;
    }
}
