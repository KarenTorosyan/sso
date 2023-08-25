package auth.entities.user;

import auth.entities.picture.Picture;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void shouldCreateInstance() {
        Email email = new Email("karen.torosyan@outlook.com");
        String password = "pass";
        User user = new User(email, password);
        assertThat(user).matches(u -> u.getId() == null &&
                u.getName() == null &&
                u.getFamilyName() == null &&
                u.getEmail().equals(email) &&
                u.getPassword().equals(password) &&
                u.getPicture() == null &&
                u.getAccount().equals(new Account()));

        String id = "1";
        String name = "Name";
        String familyName = "FamilyName";
        Picture picture = new Picture("url");
        Account account = new Account()
                .lockedIn(Instant.now());
        user.withId(id)
                .withName(name)
                .withFamilyName(familyName)
                .withEmail(email)
                .withPassword(password)
                .withPicture(picture)
                .withAccount(account);
        assertThat(user).matches(u -> u.getId().equals(id) &&
                u.getName().equals(name) &&
                u.getFamilyName().equals(familyName) &&
                u.getEmail().equals(email) &&
                u.getPassword().equals(password) &&
                u.getPicture().equals(picture) &&
                u.getAccount().equals(account));
    }
}

