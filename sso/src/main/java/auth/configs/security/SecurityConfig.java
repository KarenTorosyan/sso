package auth.configs.security;

import auth.Endpoints;
import auth.configs.web.SessionInfo;
import auth.entities.user.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserService userService,
                                            EndpointPolicy endpointPolicy,
                                            SessionInfo sessionInfo) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(c -> {
                    c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                    c.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
                })
                .sessionManagement(c -> {
                    c.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    c.maximumSessions(10);
                })
                .addFilterBefore(new LocaleResolverFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(c -> {
                    c.loginPage(Endpoints.LOGIN);
                    c.usernameParameter("email");
                    c.defaultSuccessUrl(Endpoints.PROFILE);
                })
                .rememberMe(c -> c.rememberMeCookieName(SecurityAttributes.REMEMBER_ME_COOKIE_NAME))
                .oauth2Login(c -> {
                    c.loginPage(Endpoints.LOGIN);
                    c.successHandler(new OidcAuthenticationSuccessHandler()
                            .whenOidcUser(new SaveOidcUserConsumer(userService))
                            .whenOAuth2User(new SaveOAuth2UserConsumer(userService))
                            .defaultSuccessUrl(Endpoints.PROFILE));
                })
                .authorizeHttpRequests(endpointPolicy)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new ErrorResponseUnauthorizedAuthenticationEntryPoint()
                            .redirect(Endpoints.PROFILE, Endpoints.LOGIN)
                            .redirect(Endpoints.AUTHORIZATION_CONSENT, Endpoints.LOGIN));
                    c.accessDeniedHandler(new ErrorResponseAccessDeniedHandler());
                })
                .logout(c -> {
                    c.logoutUrl(Endpoints.LOGOUT);
                    c.logoutSuccessUrl(Endpoints.LOGIN.concat("?logout"));
                    c.invalidateHttpSession(true);
                    c.deleteCookies(sessionInfo.getSessionCookieName(),
                            SecurityAttributes.REMEMBER_ME_COOKIE_NAME);
                }).build();
    }

    @Bean
    UserDetailsService userDetailsService(UserService userService, MessageSource messageSource) {
        return new UserDetailsServiceImpl(userService, messageSource);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
