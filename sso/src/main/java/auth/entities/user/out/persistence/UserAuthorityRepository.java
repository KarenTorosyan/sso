package auth.entities.user.out.persistence;

import auth.entities.authority.Authority;
import auth.entities.user.UserAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserAuthorityRepository {

    UserAuthority save(UserAuthority userAuthority);

    Optional<UserAuthority> find(UserAuthority userAuthority);

    boolean existsByUserIdAndAuthority(String userId, Authority authority);

    Page<UserAuthority> findAllByUserId(String userId, Pageable pageable);

    void delete(UserAuthority userAuthority);
}
