package auth.views;

import auth.Endpoints;
import auth.entities.user.Email;
import auth.entities.user.User;
import auth.entities.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.mock.MockUser;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailVerifierController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class EmailVerifierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LocalizedNotifications localizedNotifications;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldVerifyEmailByVerificationCodeAndRenderLoginPage() throws Exception {
        Email email = new Email("email").withVerificationCode("code");
        User user = mockUser.mock().withEmail(email);
        given(userService.getByEmail(email.getAddress())).willReturn(user);
        String successEmailVerifiedText = "Your email verified!";
        given(localizedNotifications.yourEmailVerified()).willReturn(successEmailVerifiedText);
        mockMvc.perform(get(Endpoints.EMAIL_VERIFY + "/{email}", email.getAddress())
                        .with(anonymous())
                        .param("code", email.getVerificationCode()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("successEmailVerifiedText", successEmailVerifiedText));
    }

    @Test
    void shouldReturnErrorWhenEmailVerificationCodeInvalid() throws Exception {
        Email email = new Email("email").withVerificationCode("code");
        User user = mockUser.mock();
        given(userService.getByEmail(email.getAddress())).willReturn(user);
        mockMvc.perform(get(Endpoints.EMAIL_VERIFY + "/{email}", email.getAddress())
                        .with(anonymous())
                        .param("code", email.getVerificationCode()))
                .andExpect(status().isBadRequest());
    }
}
