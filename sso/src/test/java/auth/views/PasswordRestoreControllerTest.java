package auth.views;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.entities.user.Email;
import auth.entities.user.Password;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(PasswordRestoreController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class PasswordRestoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldRenderUserNotFoundMessageWhenUserByEmailNotFound() throws Exception {
        String email = "email";
        String code = "code";
        given(userService.getByEmail(email))
                .willThrow(Errors.userNotExistsByEmail(email));
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PASSWORD_RESTORE)
                        .with(MockPrincipal.anonymous())
                        .queryParam("email", email)
                        .queryParam("code", code))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("userNotFoundByEmail", Matchers.notNullValue()));
        verify(userService).getByEmail(email);
    }

    @Test
    void shouldRenderPasswordRestoreCodeInvalidMessageWhenUserHasNoSpecifiedPasswordRestoreCode() throws Exception {
        String email = "email";
        String code = "code";
        User user = mockUser.mock().withEmail(new Email(email))
                .withPassword(new Password("password")
                        .withRestoreCode("other"));
        given(userService.getByEmail(email))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PASSWORD_RESTORE)
                        .with(MockPrincipal.anonymous())
                        .queryParam("email", email)
                        .queryParam("code", code))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("user", user))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("passwordRestoreCodeInvalid", Matchers.notNullValue()));
        verify(userService).getByEmail(email);
    }

    @Test
    void shouldRenderPasswordRestoreCodeExpiredWhenUserHaveExpiredPasswordRestoreCode() throws Exception {
        String email = "email";
        String code = "code";
        User user = mockUser.mock().withEmail(new Email(email))
                .withPassword(new Password()
                        .withRestoreCode(code)
                        .withRestoreCodeExpiresIn(Instant.MIN));
        given(userService.getByEmail(email))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PASSWORD_RESTORE)
                        .with(MockPrincipal.anonymous())
                        .queryParam("email", email)
                        .queryParam("code", code))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("user", user))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("passwordRestoreCodeExpired", Matchers.notNullValue()));
        verify(userService).getByEmail(email);
    }

    @Test
    void shouldRenderPasswordRestorePageWithoutErrorMessagesWhenUserByEmailFoundAndUserHaveSpecifiedNotExpiredPasswordRestoreCode() throws Exception {
        String email = "email";
        String code = "code";
        User user = mockUser.mock().withEmail(new Email(email))
                .withPassword(new Password()
                        .withRestoreCode(code)
                        .withRestoreCodeExpiresIn(Instant.MAX));
        given(userService.getByEmail(email))
                .willReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PASSWORD_RESTORE)
                        .with(MockPrincipal.anonymous())
                        .queryParam("email", email)
                        .queryParam("code", code))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("user", user));
        verify(userService).getByEmail(email);
    }

    @Test
    void shouldRenderPasswordNotConfirmedMessageWhenUserSpecifiedPasswordNotConfirmed() throws Exception {
        String newPassword = "password";
        String confirmNewPassword = "other";
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_RESTORE)
                        .sessionAttr("user", mockUser.mock())
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmNewPassword", confirmNewPassword)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("passwordNotConfirmed", Errors.passwordNotConfirmed()
                                .getMessage()));
    }

    @Test
    void shouldRenderPasswordLengthValidationMessageWhenUserSpecifiedPasswordInvalid() throws Exception {
        String newPassword = "pass"; // LimitPasswordSize.PASSWORD_MIN_LENGTH
        String confirmNewPassword = "pass";
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_RESTORE)
                        .sessionAttr("user", mockUser.mock())
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmNewPassword", confirmNewPassword)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("password-forget-restore"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("passwordLengthValidation", Matchers.notNullValue()));
    }

    @Test
    void shouldRenderLoginPageWithPasswordChangedSuccessMessageWhenUserPasswordChangedInUserSpecifiedPassword() throws Exception {
        String newPassword = "password";
        String confirmNewPassword = "password";
        given(passwordEncoder.encode(newPassword))
                .willReturn(newPassword);
        mockMvc.perform(MockMvcRequestBuilders.post(Endpoints.PASSWORD_RESTORE)
                        .sessionAttr("user", mockUser.mock())
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmNewPassword", confirmNewPassword)
                        .with(MockPrincipal.anonymous())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("login"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("successPasswordRestore", true));
        verify(passwordEncoder).encode(newPassword);
    }
}
