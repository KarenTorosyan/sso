package auth.configs.security;

import auth.Endpoints;
import auth.entities.user.User;
import auth.entities.user.UserService;
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

import java.time.ZoneId;
import java.util.List;

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
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(AccessTokenClaims accessTokenClaims,
                                                            IdTokenClaims idTokenClaims) {
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claims(c -> c.putAll(accessTokenClaims.getClaims(context)));

            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                context.getClaims().claims(c -> c.putAll(idTokenClaims.getClaims(context)));
            }
        };
    }

    @Bean
    AccessTokenClaims accessTokenClaims(UserService userService) {
        return context -> {
            JwtClaimsSet jwtClaimsSet = context.getClaims().build();
            User user = userService.getByEmail(jwtClaimsSet.getSubject());
            List<String> authorities = userService.getAuthorities(user.getId(), Pageable.unpaged())
                    .map(userAuthority -> userAuthority.getAuthority().getName())
                    .getContent();
            OAuth2TokenClaimsSet.Builder builder = OAuth2TokenClaimsSet.builder()
                    .claim(AccessTokenClaims.AUTHORITIES, authorities);
            if (context.getAuthorizedScopes().contains(StandardClaimNames.EMAIL)) {
                builder.claim(StandardClaimNames.EMAIL, user.getEmail().getAddress());
                builder.claim(StandardClaimNames.EMAIL_VERIFIED, user.getEmail().isVerified());
            }
            return builder.build().getClaims();
        };
    }

    @Bean
    IdTokenClaims idTokenClaims(UserService userService) {
        return context -> {
            JwtClaimsSet jwtClaimsSet = context.getClaims().build();
            User user = userService.getByEmail(jwtClaimsSet.getSubject());
            OidcUserInfo.Builder builder = OidcUserInfo.builder()
                    .subject(user.getEmail().getAddress());

            if (context.getAuthorizedScopes().contains(StandardClaimNames.PROFILE)) {
                builder.givenName(user.getName())
                        .familyName(user.getFamilyName())
                        .name(user.getName() + " " + user.getFamilyName())
                        .preferredUsername(user.getEmail().getAddress())
                        .picture(user.getPicture() != null ? user.getPicture().getUrl() : null)
                        .profile(jwtClaimsSet.getIssuer().toString() + StandardClaimNames.PROFILE)
                        .locale(LocaleContextHolder.getLocale().toString())
                        .zoneinfo(ZoneId.systemDefault().getId());
            }
            if (context.getAuthorizedScopes().contains(StandardClaimNames.EMAIL)) {
                builder.email(user.getEmail().getAddress())
                        .emailVerified(user.getEmail().isVerified());
            }
            return builder.build().getClaims();
        };
    }
}
