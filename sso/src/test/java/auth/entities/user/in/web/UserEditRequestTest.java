package auth.entities.user.in.web;

import auth.entities.user.User;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEditRequestTest {

    @Test
    void shouldGetUserFromRequest() {
        User user = new MockUser().mock();
        UserEditRequest userEditRequest = new UserEditRequest(user.getName(), user.getFamilyName());
        assertThat(userEditRequest.getModifiedUser(user)).isEqualTo(user);
        assertThat(userEditRequest)
                .matches(u -> u.getName().equals(user.getName()) &&
                        u.getFamilyName().equals(user.getFamilyName()));
    }
}

