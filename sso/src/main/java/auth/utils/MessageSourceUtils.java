package auth.utils;

import auth.errors.ResourceBundleRuntimeException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageSourceUtils {

    public static String localize(MessageSource messageSource, ResourceBundleRuntimeException e) {
        return messageSource.getMessage(e.getCode(), e.getArguments(), e.getMessage(), LocaleContextHolder.getLocale());
    }
}
