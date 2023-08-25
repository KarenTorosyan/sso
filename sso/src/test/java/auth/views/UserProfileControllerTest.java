package auth.views;

import auth.Endpoints;
import auth.configs.security.TypedUserDetails;
import auth.entities.user.User;
import auth.entities.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.web.servlet.MockMvc;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
@Import(AuthenticatedUserModel.class)
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldRenderUserProfilePageWithDefaultUser() throws Exception {
        TypedUserDetails principal = MockPrincipal.typedUserDetails(mockUser.mock());
        mockMvc.perform(get(Endpoints.PROFILE)
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute(AuthenticatedUserModel.PRINCIPAL_NAME_ATTR, principal.getName()))
                .andExpect(model().attribute(AuthenticatedUserModel.USER_ATTR, principal.getUser()));
    }

    @Test
    void shouldRenderUserProfilePageWithOidcUser() throws Exception {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress())).willReturn(user);
        OidcUser principal = MockPrincipal.oidcUser(user);
        mockMvc.perform(get(Endpoints.PROFILE)
                        .with(oidcLogin().oidcUser(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute(AuthenticatedUserModel.PRINCIPAL_NAME_ATTR, principal.getName()))
                .andExpect(model().attribute(AuthenticatedUserModel.USER_ATTR, user));
        verify(userService).getByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldRenderUserProfilePageWithOAuth2User() throws Exception {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress())).willReturn(user);
        OidcUser principal = MockPrincipal.oidcUser(user);
        mockMvc.perform(get(Endpoints.PROFILE)
                        .with(oauth2Login().oauth2User(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute(AuthenticatedUserModel.PRINCIPAL_NAME_ATTR, principal.getName()))
                .andExpect(model().attribute(AuthenticatedUserModel.USER_ATTR, user));
        verify(userService).getByEmail(user.getEmail().getAddress());
    }
}
