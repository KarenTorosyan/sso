package auth.mock;

import auth.entities.picture.Picture;
import auth.entities.user.Email;
import auth.entities.user.Password;
import auth.entities.user.User;

public class MockUser implements Mock<User> {

    @Override
    public User mock() {
        return new User(new Email("karen.torosyan@outlook.com"), new Password("password"))
                .withName("user")
                .withFamilyName("[users]")
                .withId("1")
                .withPicture(new Picture("url"));
    }
}
