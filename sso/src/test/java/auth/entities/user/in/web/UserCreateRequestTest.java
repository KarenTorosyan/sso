package auth.entities.user.in.web;

import auth.entities.user.User;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreateRequestTest {

    @Test
    void shouldGetUserFromRequest() {
        User user = new MockUser().mock();
        UserCreateRequest userCreateRequest = new UserCreateRequest(user.getEmail().getAddress(),
                user.getPassword(), user.getPassword(),
                user.getName(), user.getFamilyName());
        assertThat(userCreateRequest)
                .matches(u -> u.getName().equals(user.getName()) &&
                        u.getFamilyName().equals(user.getFamilyName()) &&
                        u.getEmail().equals(user.getEmail().getAddress()) &&
                        u.getPassword().equals(user.getPassword()) &&
                        u.getPasswordConfirm().equals(user.getPassword()) &&
                        u.getUser().equals(user));
    }
}

