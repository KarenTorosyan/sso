package auth.entities.authority.in.web;

import auth.entities.authority.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityCreateRequest {

    @JsonProperty(value = "name", required = true)
    @NotBlank(message = "{name_required}")
    @Size(max = 20, message = "{name_max}")
    private String name;

    @JsonProperty("description")
    @Size(max = 100, message = "{description_max}")
    private String description;

    @JsonIgnore
    public Authority getAuthority() {
        return new Authority(name)
                .withDescription(description);
    }
}
