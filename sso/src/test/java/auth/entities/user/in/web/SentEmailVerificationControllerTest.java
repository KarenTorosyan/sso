package auth.entities.user.in.web;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.configs.security.InitialAuthorities;
import auth.entities.user.EmailVerifier;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(SentEmailVerificationController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class SentEmailVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailVerifier emailVerifier;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldSentUserNotFoundResponseWhenUserByEmailNotFound() throws Exception {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress()))
                .willThrow(Errors.userNotExistsByEmail(user.getEmail().getAddress()));
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.EMAIL_VERIFY)
                        .queryParam("email", user.getEmail().getAddress())
                        .with(MockPrincipal.authorization(InitialAuthorities.ROLE_ADMIN))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(userService).getByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldSentErrorResponseWhenUserAlreadyVerified() throws Exception {
        User user = mockUser.mock();
        user.withEmail(user.getEmail()
                .verified(true));
        given(userService.getByEmail(user.getEmail().getAddress()))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.EMAIL_VERIFY)
                        .queryParam("email", user.getEmail().getAddress())
                        .with(MockPrincipal.authorization(InitialAuthorities.ROLE_ADMIN))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        verify(userService).getByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldSetNewVerificationCodeToUserEmailWhenAndEditUserAndSentLinkToVerifyEmailToUserEmailAddress() throws Exception {
        User user = mockUser.mock();
        user.withEmail(user.getEmail()
                .verified(false));
        given(userService.getByEmail(user.getEmail().getAddress()))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.EMAIL_VERIFY)
                        .queryParam("email", user.getEmail().getAddress())
                        .with(MockPrincipal.authorization(InitialAuthorities.ROLE_ADMIN))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(userService).getByEmail(user.getEmail().getAddress());
        verify(userService).edit(user);
    }
}
