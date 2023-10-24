package auth.views;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.entities.user.Email;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.intergrations.smtp.SmtpService;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(PasswordForgetController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class PasswordForgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SmtpService smtpService;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldRenderPasswordForgetPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PASSWORD_FORGET)
                        .with(MockPrincipal.anonymous()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget"));
    }

    @Test
    void shouldRenderEmailRequiredMessageWhenEmailNotSpecified() throws Exception {
        String email = "";
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_FORGET)
                        .queryParam("email", email)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("emailInvalid", Matchers.notNullValue()));
    }

    @Test
    void shouldRenderUserNotFoundMessageWhenUserByEmailNotFound() throws Exception {
        String email = "email";
        given(userService.getByEmail(email))
                .willThrow(Errors.userNotExistsByEmail(email));
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_FORGET)
                        .queryParam("email", email)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("userNotFoundByEmail", Matchers.notNullValue()));
        verify(userService).getByEmail(email);
    }

    @Test
    void shouldSetPasswordRestoreCodeToUserAndSentLinkToRestorePasswordToEmail() throws Exception {
        String email = "email";
        User user = mockUser.mock().withEmail(new Email(email));
        given(userService.getByEmail(email))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_FORGET)
                        .queryParam("email", email)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("emailSent", true));
        verify(userService).getByEmail(email);
        verify(userService).edit(user);
        verify(smtpService).sendMimeMessage(any(), any(), any(), any(), any());
    }
}
