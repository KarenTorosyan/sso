package auth.configs.security;

import auth.configs.web.WebConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class LocaleResolverFilter extends OncePerRequestFilter {

    private boolean checkInCookies = true;

    private boolean checkInHeaders = true;

    private String cookieName = WebConfig.LOCALE_ATTR;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (checkInCookies && cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findAny().ifPresent(cookie -> LocaleContextHolder.setLocale(fromString(cookie.getValue())));

        } else if (checkInHeaders) {
            LocaleContextHolder.setLocale(request.getLocale());
        }

        filterChain.doFilter(request, response);
    }

    private Locale fromString(String locale) {
        if (locale.matches(".*-.*") && !locale.startsWith("-") && !locale.endsWith("-")) {
            String[] attr = locale.split("-");
            return new Locale(attr[0], attr[1]);
        } else return new Locale(locale);
    }

    public LocaleResolverFilter cookieName(String cookieName) {
        this.cookieName = cookieName;
        return this;
    }

    public LocaleResolverFilter checkInCookies(boolean checkInCookies) {
        this.checkInCookies = checkInCookies;
        return this;
    }

    public LocaleResolverFilter checkInHeaders(boolean checkInHeaders) {
        this.checkInHeaders = checkInHeaders;
        return this;
    }
}
