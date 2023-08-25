package auth.entities.user;

import auth.entities.authority.Authority;
import auth.mock.MockAuthority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAuthorityTest {

    @Test
    void shouldCreateInstance() {
        Authority authority = new MockAuthority().mock();
        String userId = "1";
        UserAuthority userAuthority = new UserAuthority(userId, authority);
        assertThat(userAuthority)
                .matches(u -> u.getId() == null &&
                        u.getUserId().equals(userId) &&
                        u.getAuthority().equals(authority));

        String id = "1";
        userAuthority.withId(id)
                .withUserId(userId)
                .withAuthority(authority);
        assertThat(userAuthority)
                .matches(u -> u.getId().equals(id) &&
                        u.getUserId().equals(userId) &&
                        u.getAuthority().equals(authority));
    }
}
