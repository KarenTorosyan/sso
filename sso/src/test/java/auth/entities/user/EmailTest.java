package auth.entities.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {

    @Test
    void shouldCreateInstance() {
        String address = "karen.torosyan@outlook.com";
        Email email = new Email(address);
        assertThat(email).matches(e -> e.getAddress().equals(address) &&
                e.getVerificationCode() != null &&
                !e.isVerified());

        String verificationCode = "code";
        email.withAddress(address)
                .withVerificationCode(verificationCode)
                .verified(true);
        assertThat(email).matches(e -> e.getAddress().equals(address) &&
                e.getVerificationCode().equals(verificationCode) &&
                e.isVerified());
    }
}
