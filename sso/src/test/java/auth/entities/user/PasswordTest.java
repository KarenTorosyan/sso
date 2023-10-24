package auth.entities.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordTest {

    @Test
    void shouldCreateInstanceWithNoParametersConstructor() {
        Password password = new Password();
        assertThat(password).matches(p -> p.getValue() == null &&
                p.getRestoreCode() == null &&
                p.getRestoreCodeExpiresIn() == null);
    }

    @Test
    void shouldCreateInstanceWithConstructorWithParameters() {
        String value = "password";
        Password password = new Password(value);
        assertThat(password).matches(p -> p.getValue().equals(value) &&
                p.getRestoreCode() == null &&
                p.getRestoreCodeExpiresIn() == null);
    }

    @Test
    void shouldCreateInstanceWithConstructorWithParametersAndAdditionalSetters() {
        String value = "password";
        String restoreCode = "code";
        Instant restoreCodeExpiresIn = Instant.MAX;
        Password password = new Password(value)
                .withRestoreCode(restoreCode)
                .withRestoreCodeExpiresIn(restoreCodeExpiresIn);
        assertThat(password).matches(p -> p.getValue().equals(value) &&
                p.getRestoreCode().equals(restoreCode) &&
                p.getRestoreCodeExpiresIn().equals(restoreCodeExpiresIn));
    }
}
