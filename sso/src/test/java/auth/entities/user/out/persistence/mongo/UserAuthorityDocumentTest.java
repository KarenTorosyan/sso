package auth.entities.user.out.persistence.mongo;

import auth.entities.user.UserAuthority;
import auth.mock.MockUserAuthority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAuthorityDocumentTest {

    @Test
    void shouldCreateMongoDocumentFromUserAuthority() {
        UserAuthority userAuthority = new MockUserAuthority().mock();
        UserAuthorityDocument userAuthorityDocument = UserAuthorityDocument.from(userAuthority);
        assertThat(userAuthorityDocument)
                .matches(u -> u.getId().equals(userAuthority.getId()) &&
                        u.getUserId().equals(userAuthority.getUserId()) &&
                        u.getAuthorityDocument().getAuthority().equals(userAuthority.getAuthority()) &&
                        u.getUserAuthority().equals(userAuthority));
    }
}
