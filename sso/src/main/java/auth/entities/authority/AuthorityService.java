package auth.entities.authority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorityService {

    Authority create(Authority authority);

    Authority edit(Authority authority);

    Authority getById(String id);

    boolean existsById(String id);

    Authority getByName(String name);

    boolean existsByName(String name);

    long getCount();

    Page<Authority> getAll(Pageable pageable);

    Page<Authority> search(String text, Pageable pageable);

    void deleteById(String id);
}
