package auth.utils;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternsTest {

    private static final String EMAIL = "karen.torosyan@outlook.com";

    @Test
    void shouldTestEmailPattern() {
        Pattern pattern = Pattern.compile(Patterns.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(EMAIL);
        assertThat(matcher.matches()).isTrue();
    }
}
