package auth.mock;

import auth.entities.authority.Authority;
import auth.entities.user.User;
import auth.entities.user.UserAuthority;
import auth.entities.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public class MockUserService implements UserService {

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User edit(User user) {
        return null;
    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> getAllById(Set<String> ids, Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> getAllByEmail(Set<String> emails, Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> search(String text, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteById(String id) {
    }

    @Override
    public void addAuthority(UserAuthority userAuthority) {
    }

    @Override
    public boolean hasAuthority(String userId, Authority authority) {
        return false;
    }

    @Override
    public Page<UserAuthority> getAuthorities(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteAuthority(UserAuthority userAuthority) {
    }
}
