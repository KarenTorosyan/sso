package auth.entities.authority.in.web;

import auth.entities.authority.Authority;
import org.junit.jupiter.api.Test;
import auth.mock.MockAuthority;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityProjectionTest {

    @Test
    void shouldBuildResponse() {
        Authority authority = new MockAuthority().mock();
        AuthorityProjection authorityProjection = AuthorityProjection.from(authority);
        assertThat(authorityProjection)
                .matches(projection -> match(projection, authority));
    }

    private boolean match(AuthorityProjection projection, Authority authority) {
        return projection.id().equals(authority.getId()) &&
                projection.name().equals(authority.getName()) &&
                projection.description().equals(authority.getDescription());
    }
}
