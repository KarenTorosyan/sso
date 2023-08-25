package auth.views;

import auth.configs.security.TypedUserDetails;
import auth.entities.user.User;
import auth.entities.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = AuthenticatedUserModel.class)
public class AuthenticatedUserModelImplTest {

    @Autowired
    private AuthenticatedUserModel authenticatedUserModel;

    @MockBean
    private UserService userService;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldMutateModelAddPrincipalNameAndUser() {
        Model model = new ConcurrentModel();
        User user = mockUser.mock();
        TypedUserDetails principal = MockPrincipal.typedUserDetails(user);
        authenticatedUserModel.mutate(model, principal);
        assertThat(principal.getName()).isEqualTo(user.getEmail().getAddress());
        assertThat(principal.getUser()).isEqualTo(user);
        assertThat(model.getAttribute(AuthenticatedUserModel.PRINCIPAL_NAME_ATTR)).isEqualTo(principal.getName());
        assertThat(model.getAttribute(AuthenticatedUserModel.USER_ATTR)).isEqualTo(principal.getUser());
    }

    @Test
    void shouldMutateModelAddOAuth2PrincipalAndUser() {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress())).willReturn(user);
        Model model = new ConcurrentModel();
        OidcUser principal = MockPrincipal.oidcUser(user);
        authenticatedUserModel.mutate(model, principal);
        assertThat(principal.getName()).isEqualTo(user.getEmail().getAddress());
        assertThat(model.getAttribute(AuthenticatedUserModel.PRINCIPAL_NAME_ATTR)).isEqualTo(principal.getName());
        assertThat(model.getAttribute(AuthenticatedUserModel.USER_ATTR)).isEqualTo(user);
        verify(userService).getByEmail(user.getEmail().getAddress());
    }
}
