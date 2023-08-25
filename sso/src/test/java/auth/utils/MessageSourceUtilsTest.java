package auth.utils;

import auth.errors.Errors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.messages.basename=locales/messages")
@ContextConfiguration(classes = MessageSourceAutoConfiguration.class)
public class MessageSourceUtilsTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    void shouldLocalizeErrors() {
        String username = "username";
        LocaleContextHolder.setLocale(new Locale("en", "US"));
        String message = MessageSourceUtils.localize(messageSource, Errors.userAlreadyExistsByEmail(username));
        assertThat(message)
                .isEqualTo("The user '" + username + "' already exists!");
    }
}
