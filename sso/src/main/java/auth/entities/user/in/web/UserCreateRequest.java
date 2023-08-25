package auth.entities.user.in.web;

import auth.entities.user.Email;
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
        message = "{user_password_not_confirmed}")
public final class UserCreateRequest {

    @JsonProperty(value = "email", required = true)
    @NotBlank(message = "{user_email_required}")
    @Pattern(regexp = Patterns.EMAIL_PATTERN, message = "{user_email_invalid}")
    private String email;

    @JsonProperty(value = "password", required = true)
    @NotBlank(message = "{user_password_required}")
    @LimitPasswordSize
    private String password;

    @JsonProperty(value = "passwordConfirm", required = true)
    private String passwordConfirm;

    @JsonProperty("name")
    @Size(max = 20, message = "{user_name_max}")
    private String name;

    @JsonProperty("familyName")
    @Size(max = 30, message = "{user_familyName_max}")
    private String familyName;

    @JsonIgnore
    public User getUser() {
        return new User(new Email(email), password)
                .withName(name)
                .withFamilyName(familyName);
    }
}
