package auth;

import auth.configs.security.SecurityConfig;
import auth.configs.security.StaticEndpointPolicy;
import auth.configs.web.SessionInfo;
import auth.entities.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import auth.mock.MockUserService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SecurityConfig.class, IntegrateSecurityConfig.SecurityTestContext.class})
public @interface IntegrateSecurityConfig {

    @Configuration
    @Import({StaticEndpointPolicy.class, SessionInfo.class})
    class SecurityTestContext {

        @Bean
        UserService userService() {
            return new MockUserService();
        }
    }
}
