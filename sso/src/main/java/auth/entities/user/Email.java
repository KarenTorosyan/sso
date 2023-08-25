package auth.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Email {

    private String address;

    private String verificationCode;

    private boolean verified;

    public Email(String address) {
        this.address = address;
        this.verificationCode = UUID.randomUUID().toString();
    }

    public Email withAddress(String address) {
        this.address = address;
        return this;
    }

    public Email withVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public Email verified(boolean verified) {
        this.verified = verified;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(getAddress(), email.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress());
    }
}
