package auth.configs.security;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.Map;

@FunctionalInterface
public interface AccessTokenClaims {

    String AUTHORITIES = "authorities";

    Map<String, Object> getClaims(JwtEncodingContext context);
}
