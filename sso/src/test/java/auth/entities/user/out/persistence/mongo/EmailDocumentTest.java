package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailDocumentTest {

    @Test
    void shouldCreateMongoDocumentFromEmail() {
        Email email = new Email("karen.torosyan@outlook.com");
        EmailDocument emailDocument = EmailDocument.from(email);
        assertThat(emailDocument)
                .matches(e -> e.getAddress().equals(email.getAddress()) &&
                        e.getEmail().equals(email));
    }
}

