package auth.entities.authority;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Authority {

    private String id;

    private String name;

    private String description;

    public Authority(String name) {
        this.name = name;
    }

    public Authority withId(String id) {
        this.id = id;
        return this;
    }

    public Authority withName(String name) {
        this.name = name;
        return this;
    }

    public Authority withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
