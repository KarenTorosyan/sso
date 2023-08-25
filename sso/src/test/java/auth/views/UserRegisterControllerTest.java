package auth.views;

import auth.Endpoints;
import auth.entities.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(model().attribute("registerAsAdminText", "register_as_admin"));
    }
}
