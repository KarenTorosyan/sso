package auth.entities.user;

import auth.entities.authority.Authority;
import auth.entities.user.out.persistence.UserAuthorityRepository;
import auth.entities.user.out.persistence.UserRepository;
import auth.errors.Errors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Transactional
    @Override
    public User create(User user) {
        if (user.getId() != null && existsById(user.getId())) {
            throw Errors.userAlreadyExistsById(user.getId());
        }
        if (existsByEmail(user.getEmail().getAddress())) {
            throw Errors.userAlreadyExistsByEmail(user.getEmail().getAddress());
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User edit(User user) {
        if (user.getId() != null && !existsById(user.getId())) {
            throw Errors.userNotExistsById(user.getId());
        }
        if (existsByEmail(user.getEmail().getAddress()) &&
            !getByEmail(user.getEmail().getAddress()).equals(user)) {
            throw Errors.userAlreadyExistsByEmail(user.getEmail().getAddress());
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> Errors.userNotExistsById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> Errors.userNotExistsByEmail(email));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> search(String text, Pageable pageable) {
        return userRepository.search(text, pageable);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        userRepository.deleteById(getById(id).getId());
    }

    @Transactional
    @Override
    public void addAuthority(UserAuthority userAuthority) {
        if (hasAuthority(userAuthority.getUserId(), userAuthority.getAuthority())) {
            throw Errors.userAlreadyHasAuthority(userAuthority.getUserId(), userAuthority.getAuthority().getName());
        }
        userAuthorityRepository.save(userAuthority);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasAuthority(String userId, Authority authority) {
        return userAuthorityRepository.existsByUserIdAndAuthority(userId, authority);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserAuthority> getAuthorities(String userId, Pageable pageable) {
        return userAuthorityRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void deleteAuthority(UserAuthority userAuthority) {
        if (!hasAuthority(userAuthority.getUserId(), userAuthority.getAuthority())) {
            throw Errors.userHasNotAuthority(userAuthority.getUserId(), userAuthority.getAuthority().getName());
        }
        userAuthorityRepository.delete(userAuthority);
    }
}
