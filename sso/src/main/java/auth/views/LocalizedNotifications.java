package auth.views;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizedNotifications {

    private final MessageSource messageSource;

    private final Locale locale = LocaleContextHolder.getLocale();

    public String yourEmailVerified() {
        return messageSource.getMessage("your_email_verified", new Object[]{}, "Your email verified!", locale);
    }
}
