package auth.views;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = LocalizedNotifications.class)
public class LocalizedNotificationsTest {

    @Autowired
    private LocalizedNotifications localizedNotifications;

    @Test
    void shouldReturnLocalizedMessageByCode() {
        assertThat(localizedNotifications.yourEmailVerified()).isEqualTo("Your email verified!");
    }
}
