package auth.entities.user.in.web;

import auth.validators.Confirm;
import auth.validators.LimitPasswordSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Confirm(message = "{password_not_confirmed}",
        field = "newPassword", confirm = "confirmNewPassword")
public record PasswordChangeRequest(
        @JsonProperty(value = "current", required = true)
        @NotBlank(message = "{password_required}")
        String currentPassword,

        @JsonProperty(value = "new", required = true)
        @Size(message = "{password_minmax}",
                min = LimitPasswordSize.PASSWORD_MIN_LENGTH,
                max = LimitPasswordSize.PASSWORD_MAX_LENGTH)
        String newPassword,

        @JsonProperty(value = "confirmNew", required = true)
        String confirmNewPassword) {

    @JsonIgnore
    public String getCurrentPassword() {
        return currentPassword;
    }

    @JsonIgnore
    public String getNewPassword() {
        return newPassword;
    }
}
