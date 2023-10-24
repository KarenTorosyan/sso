package auth.views;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.entities.user.UserService;
import auth.entities.user.in.web.UserCreateRequest;
import auth.errors.Errors;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegisterController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldRenderRegisterPage() throws Exception {
        given(userService.getCount())
                .willReturn(1L);
        mockMvc.perform(get(Endpoints.REGISTER)
                        .with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void shouldRenderRegisterPageWithMessageRegisterAsAdministratorWhenFirstUser() throws Exception {
        given(userService.getCount())
                .willReturn(0L);
        mockMvc.perform(get(Endpoints.REGISTER)
                        .with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("registerAsAdmin", true));
    }
}
