package auth.views;

import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(EmailVerifierController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class EmailVerifierControllerTest {
    // test
}
