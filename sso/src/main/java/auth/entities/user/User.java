package auth.entities.user;

import auth.entities.picture.Picture;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private String id;

    private String name;

    private String familyName;

    private Picture picture;

    private Email email;

    private Password password;

    private Account account = new Account();

    public User(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public User withId(String id) {
        this.id = id;
        return this;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public User withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public User withEmail(Email email) {
        this.email = email;
        return this;
    }

    public User withPassword(Password password) {
        this.password = password;
        return this;
    }

    public User withPicture(Picture picture) {
        this.picture = picture;
        return this;
    }

    public User withAccount(Account account) {
        this.account = account;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail().hashCode());
    }
}
