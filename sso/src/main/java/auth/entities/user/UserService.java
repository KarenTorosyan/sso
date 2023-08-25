package auth.entities.user;

import auth.entities.authority.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User create(User user);

    User edit(User user);

    User getById(String id);

    boolean existsById(String id);

    User getByEmail(String email);

    boolean existsByEmail(String email);

    long getCount();

    Page<User> getAll(Pageable pageable);

    Page<User> search(String text, Pageable pageable);

    void deleteById(String id);

    void addAuthority(UserAuthority userAuthority);

    boolean hasAuthority(String userId, Authority authority);

    Page<UserAuthority> getAuthorities(String userId, Pageable pageable);

    void deleteAuthority(UserAuthority userAuthority);
}
