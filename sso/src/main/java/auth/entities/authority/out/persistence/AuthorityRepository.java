package auth.entities.authority.out.persistence;

import auth.entities.authority.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuthorityRepository {

    Authority save(Authority authority);

    Optional<Authority> findById(String id);

    boolean existsById(String id);

    Optional<Authority> findByName(String name);

    boolean existsByName(String name);

    long count();

    Page<Authority> findAll(Pageable pageable);

    Page<Authority> search(String text, Pageable pageable);

    void deleteById(String id);
}