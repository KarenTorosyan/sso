package auth.entities.user.out.persistence;

import auth.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    boolean existsById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long count();

    Page<User> findAll(Pageable pageable);

    Page<User> findAllById(Set<String> ids, Pageable pageable);

    Page<User> findAllByEmail(Set<String> emails, Pageable pageable);

    Page<User> search(String text, Pageable pageable);

    void deleteById(String id);
}
