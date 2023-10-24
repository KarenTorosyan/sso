package auth.entities.user;

import lombok.*;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "restoreCodeExpiresIn")
public class Password {

    private String value;

    private String restoreCode;

    private Instant restoreCodeExpiresIn;

    public Password(String value) {
        this.value = value;
    }

    public Password withRestoreCode(String restoreCode) {
        this.restoreCode = restoreCode;
        return this;
    }

    public Password withRestoreCodeExpiresIn(Instant restoreCodeExpiresIn) {
        this.restoreCodeExpiresIn = restoreCodeExpiresIn;
        return this;
    }
}
