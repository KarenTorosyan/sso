package auth.views;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.mock.MockPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ProfileController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderProfilePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Endpoints.PROFILE)
                        .with(MockPrincipal.authorization()))
                .andExpect(MockMvcResultMatchers.view()
                        .name("profile"));
    }
}
