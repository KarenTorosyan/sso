package auth.mock;

import auth.configs.security.TypedUserDetails;
import auth.entities.user.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;

public class MockPrincipal {

    public static TypedUserDetails typedUserDetails(User user, String... authorities) {
        return new TypedUserDetails(user, new LinkedHashSet<>(AuthorityUtils.createAuthorityList(authorities)));
    }

    public static TypedUserDetails typedUserDetails(String... authorities) {
        return typedUserDetails(new MockUser().mock(), authorities);
    }

    public static OidcUser oidcUser(User user, String... authorities) {
        Map<String, Object> claims = OidcUserInfo.builder()
                .subject(user.getEmail().getAddress())
                .build().getClaims();
        OidcIdToken token = new OidcIdToken("token", Instant.now(), Instant.now().plus(Duration.ofHours(1)), claims);
        return new DefaultOidcUser(AuthorityUtils.createAuthorityList(authorities), token);
    }

    public static OidcUser oidcUser(String... authorities) {
        return oidcUser(new MockUser().mock(), authorities);
    }

    public static RequestPostProcessor authorization(User user, String... authorities) {
        return SecurityMockMvcRequestPostProcessors.user(typedUserDetails(user, authorities));
    }

    public static RequestPostProcessor authorization(String... authorities) {
        return authorization(new MockUser().mock(), authorities);
    }

    public static RequestPostProcessor anonymous() {
        return SecurityMockMvcRequestPostProcessors.anonymous();
    }

    public static RequestPostProcessor oidc(User user, String... authorities) {
        return SecurityMockMvcRequestPostProcessors.oidcLogin().oidcUser(oidcUser(user, authorities));
    }

    public static RequestPostProcessor oidc() {
        return oidc(new MockUser().mock());
    }

}
