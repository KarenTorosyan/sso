package auth.configs.security;

import auth.Endpoints;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.utils.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;
import java.util.Map;

@Configuration
public class AuthorizationServerConfig {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity security) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(security);
        security.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .authorizationEndpoint(c -> c.consentPage(Endpoints.AUTHORIZATION_CONSENT));
        return security
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
                .exceptionHandling(c -> c.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(Endpoints.LOGIN)))
                .build();
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserService userService) {
        return context -> {
            JwtClaimsSet claims = context.getClaims().build();
            User user = userService.getByEmail(claims.getSubject());
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                List<String> authorities = userService.getAuthorities(user.getId(), Pageable.unpaged())
                        .map(userAuthority -> userAuthority.getAuthority().getName())
                        .getContent();
                context.getClaims().claims(c -> c.putAll(customizeAccessToken(context, user, authorities)));

            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                context.getClaims().claims(c -> c.putAll(customizeIdToken(context, user)));
            }
        };
    }

    private Map<String, Object> customizeIdToken(JwtEncodingContext context, User user) {
        JwtClaimsSet jwtClaimsSet = context.getClaims().build();
        OidcUserInfo.Builder builder = OidcUserInfo.builder();
        if (context.getAuthorizedScopes().contains(StandardClaimNames.PROFILE)) {
            builder
                    .preferredUsername(user.getEmail().getAddress())
                    .picture(user.getPicture() != null ? user.getPicture().getUrl() : null)
                    .profile(jwtClaimsSet.getIssuer().toString() +
                            Endpoints.USERS + "/email/" + user.getEmail().getAddress())
                    .email(user.getEmail().getAddress())
                    .emailVerified(user.getEmail().isVerified())
                    .locale(LocaleContextHolder.getLocale().toString());

            if (user.getName() != null) {
                builder.givenName(user.getName());
                builder.name(user.getName());

                if (user.getFamilyName() != null) {
                    builder.familyName(user.getFamilyName());
                    builder.name(user.getName() + " " + user.getFamilyName());
                }
            }
        }
        return builder.build().getClaims();
    }

    private Map<String, Object> customizeAccessToken(JwtEncodingContext context,
                                                     User user, List<String> authorities) {
        OAuth2TokenClaimsSet.Builder builder = OAuth2TokenClaimsSet.builder();
        if (context.getAuthorizedScopes().contains(StandardClaimNames.PROFILE)) {
            builder
                    .claim(Claims.AUTHORITIES, authorities)
                    .claim(StandardClaimNames.EMAIL, user.getEmail().getAddress())
                    .claim(StandardClaimNames.EMAIL_VERIFIED, user.getEmail().isVerified());
        }
        return builder.build().getClaims();
    }

}
