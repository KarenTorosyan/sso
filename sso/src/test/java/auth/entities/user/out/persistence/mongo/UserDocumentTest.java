package auth.entities.user.out.persistence.mongo;

import auth.entities.user.User;
import org.junit.jupiter.api.Test;
import auth.mock.MockUser;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDocumentTest {

    @Test
    void shouldCreateMongoDocumentFromUser() {
        User user = new MockUser().mock();
        UserDocument userDocument = UserDocument.from(user);
        assertThat(userDocument).matches(u -> u.getId().equals(user.getId()) &&
                u.getName().equals(user.getName()) &&
                u.getFamilyName().equals(user.getFamilyName()) &&
                u.getEmailDocument().getEmail().equals(user.getEmail()) &&
                u.getPassword().equals(user.getPassword()) &&
                u.getPictureDocument().getPicture().equals(user.getPicture()) &&
                u.getAccountDocument().getAccount().equals(user.getAccount()) &&
                u.getUser().equals(user));
    }
}
