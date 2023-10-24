package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PasswordDocument {

    private String value;

    private String restoreCode;

    private Instant restoreCodeExpiresIn;

    public static PasswordDocument from(Password password) {
        if (password == null) {
            return new PasswordDocument(null, null, null);
        }
        return new PasswordDocument(password.getValue(),
                password.getRestoreCode(), password.getRestoreCodeExpiresIn());
    }

    public Password getPassword() {
        return new Password(value, restoreCode, restoreCodeExpiresIn);
    }
}
