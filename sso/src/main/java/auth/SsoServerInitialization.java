package auth;

import auth.configs.security.InitialAuthorities;
import auth.entities.authority.Authority;
import auth.entities.authority.AuthorityService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class SsoServerInitialization {

    @Order(0)
    @Bean
    ApplicationRunner initAuthorities(AuthorityService authorityService) {
        return args -> {
            if (!authorityService.existsByName(InitialAuthorities.ROLE_ADMIN)) {
                authorityService.create(new Authority(InitialAuthorities.ROLE_ADMIN)
                        .withDescription("Role for admins"));
            }
            if (!authorityService.existsByName(InitialAuthorities.ROLE_USER)) {
                authorityService.create(new Authority(InitialAuthorities.ROLE_USER)
                        .withDescription("Role for users"));
            }
        };
    }
}
