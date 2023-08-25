package auth.entities.authority.in.web;

import auth.entities.authority.Authority;
import org.junit.jupiter.api.Test;
import auth.mock.MockAuthority;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityCreateRequestTest {

    @Test
    void shouldGetAuthorityFromRequest() {
        Authority authority = new MockAuthority().mock();
        AuthorityCreateRequest authorityCreateRequest = new AuthorityCreateRequest(authority.getName(), authority.getDescription());
        assertThat(authorityCreateRequest.getAuthority()).isEqualTo(authority);
    }
}
