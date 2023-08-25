package auth.entities.user.in.web;

import auth.entities.user.User;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProjectionTest {

    @Test
    void shouldBuildResponse() {
        User user = new MockUser().mock();
        UserProjection userProjection = UserProjection.from(user);
        assertThat(userProjection)
                .matches(u -> u.name().equals(user.getName()) &&
                        u.familyName().equals(user.getFamilyName()) &&
                        u.emailPrimaryProjection().address().equals(user.getEmail().getAddress()) &&
                        u.emailPrimaryProjection().verified() == user.getEmail().isVerified() &&
                        u.pictureProjection().url().equals(user.getPicture().getUrl()));
    }
}
