package auth.entities.user.in.web;

import auth.entities.user.Email;
import auth.entities.user.Password;
import auth.entities.user.User;
import auth.utils.Patterns;
import auth.validators.Confirm;
import auth.validators.LimitPasswordSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Confirm(field = "password", confirm = "passwordConfirm",
        message = "{password_not_confirmed}")
public final class UserCreateRequest {

    @JsonProperty(value = "email", required = true)
    @NotBlank(message = "{email_required}")
    @Pattern(regexp = Patterns.EMAIL_PATTERN, message = "{email_invalid}")
    private String email;

    @JsonProperty(value = "password", required = true)
    @NotBlank(message = "{password_required}")
    @Size(message = "{password_minmax}",
            min = LimitPasswordSize.PASSWORD_MIN_LENGTH,
            max = LimitPasswordSize.PASSWORD_MAX_LENGTH)
    private String password;

    @JsonProperty(value = "passwordConfirm", required = true)
    private String passwordConfirm;

    @JsonProperty("name")
    @Size(max = 20, message = "{name_max}")
    private String name;

    @JsonProperty("familyName")
    @Size(max = 30, message = "{familyName_max}")
    private String familyName;

    @JsonIgnore
    public User getUser() {
        return new User(new Email(email), new Password(password))
                .withName(name)
                .withFamilyName(familyName);
    }
}
