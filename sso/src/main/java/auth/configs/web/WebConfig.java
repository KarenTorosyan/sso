package auth.configs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    public static final String LOCALE_ATTR = "locale";

    private final CorsConfiguration corsConfiguration;

    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .combine(corsConfiguration);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Configuration
    static class CorsConfig {

        @Bean
        @ConfigurationProperties("cors")
        CorsConfiguration corsConfiguration() {
            return new CorsConfiguration();
        }
    }

    @Configuration
    static class LocaleConfig {

        @Bean
        LocaleResolver localeResolver() {
            var resolver = new CookieLocaleResolver(LOCALE_ATTR);
            resolver.setCookieMaxAge(Duration.ofDays(10000));
            resolver.setCookiePath("/");
            return resolver;
        }

        @Bean
        LocaleChangeInterceptor localeChangeInterceptor() {
            var interceptor = new LocaleChangeInterceptor();
            interceptor.setParamName(LOCALE_ATTR);
            return interceptor;
        }
    }
}
