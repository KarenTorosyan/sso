package auth.configs.security;

import auth.Endpoints;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StaticEndpointPolicy implements EndpointPolicy {

    private static final RequestMatcher[] AUTHENTICATION = {
            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, Endpoints.USERS + "/*"),
            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, Endpoints.USERS + "/*"),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, Endpoints.USERS + "/*/picture"),
            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, Endpoints.USERS + "/*/password"),
            AntPathRequestMatcher.antMatcher(Endpoints.SESSIONS),
            AntPathRequestMatcher.antMatcher(Endpoints.EMAIL_VERIFY)
    };

    private static final Map<RequestMatcher, String[]> AUTHORIZATION = Map.of(
            AntPathRequestMatcher.antMatcher(Endpoints.AUTHORITIES + "/**"), InitialAuthorities.admin(),
            AntPathRequestMatcher.antMatcher(HttpMethod.PUT, Endpoints.USERS + "/*/account"), InitialAuthorities.admin(),
            AntPathRequestMatcher.antMatcher(HttpMethod.POST, Endpoints.USERS + "/*/authorities/*"), InitialAuthorities.admin(),
            AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, Endpoints.USERS + "/*/authorities/*"), InitialAuthorities.admin()
    );

    @Override
    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                  .AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(AUTHENTICATION).authenticated();
        AUTHORIZATION.forEach(((requestMatcher, authorities) ->
                registry.requestMatchers(requestMatcher).hasAnyAuthority(authorities)));
        registry.anyRequest().permitAll();
    }
}
