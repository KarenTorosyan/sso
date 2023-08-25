package auth.entities.authority.in.web;

import auth.entities.authority.Authority;
import auth.mock.MockAuthority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityEditRequestTest {

    @Test
    void shouldBuildModifiedAuthorityFromRequest() {
        Authority authority = new MockAuthority().mock();
        AuthorityEditRequest authorityEditRequest = new AuthorityEditRequest(authority.getName(), authority.getDescription());
        assertThat(authorityEditRequest.getModifiedAuthority(authority))
                .isEqualTo(authority);
    }
}
