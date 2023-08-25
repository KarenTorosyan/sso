package auth.entities.user.in.web;

import auth.entities.user.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailProjectionTest {

    @Test
    void shouldBuildResponse() {
        Email email = new Email("karen.torosyan@outlook.com");
        EmailProjection emailProjection = EmailProjection.from(email);
        assertThat(emailProjection)
                .matches(e -> e.address().equals(email.getAddress()) &&
                        e.verified() == emailProjection.verified());
    }
}
