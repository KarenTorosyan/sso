package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Password;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordDocumentTest {

    @Test
    void shouldCreatePasswordDocumentFromPassword() {
        Password password = new Password("value")
                .withRestoreCode("code")
                .withRestoreCodeExpiresIn(Instant.MAX);
        assertThat(PasswordDocument.from(password)).matches(doc ->
                doc.getValue().equals(password.getValue()) &&
                doc.getRestoreCode().equals(password.getRestoreCode()) &&
                doc.getRestoreCodeExpiresIn().equals(password.getRestoreCodeExpiresIn()));
    }

    @Test
    void shouldGetSourcePasswordFromPasswordDocument() {
        Password password = new Password("value")
                .withRestoreCode("code")
                .withRestoreCodeExpiresIn(Instant.MAX);
        assertThat(PasswordDocument.from(password).getPassword())
                .isEqualTo(password);
    }
}
