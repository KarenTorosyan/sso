package auth.entities.user.in.web;

import auth.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class UserEditRequest {

    @JsonProperty(value = "name", required = true)
    @Size(max = 20, message = "{user_name_max}")
    private String name;

    @JsonProperty(value = "familyName", required = true)
    @Size(max = 30, message = "{user_familyName_max}")
    private String familyName;

    @JsonIgnore
    public User getModifiedUser(User user) {
        return user
                .withName(name)
                .withFamilyName(familyName);
    }
}
