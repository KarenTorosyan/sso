package auth.entities.user;

import auth.entities.user.out.persistence.UserAuthorityRepository;
import auth.entities.user.out.persistence.UserRepository;
import auth.errors.AlreadyExistsException;
import auth.errors.NotExistsException;
import auth.mock.MockUser;
import auth.mock.MockUserAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserAuthorityRepository userAuthorityRepository;

    private final MockUser mockUser = new MockUser();

    private final MockUserAuthority mockUserAuthority = new MockUserAuthority();

    @Test
    void shouldCreateUserWhenUserByNameNotExists() {
        User user = mockUser.mock();
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(false);
        given(userRepository.save(user))
                .willReturn(user);
        User createdUser = userService.create(user);
        assertThat(createdUser).isEqualTo(user);
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowErrorWhenCreateUserWhenUserByNameAlreadyExists() {
        User user = mockUser.mock();
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(true);
        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(AlreadyExistsException.class);
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldEditUserWhenUserByIdExistsAndOtherUserByEmailNotExists() {
        User user = mockUser.mock();
        given(userRepository.existsById(user.getId()))
                .willReturn(true);
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(false);
        given(userRepository.save(user))
                .willReturn(user);
        User editedUser = userService.edit(user);
        assertThat(editedUser).isEqualTo(user);
        verify(userRepository).existsById(user.getId());
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowErrorWhenEditUserByIdWhenUserByIdNotExists() {
        User user = mockUser.mock();
        given(userRepository.existsById(user.getId()))
                .willReturn(false);
        assertThatThrownBy(() -> userService.edit(user))
                .isInstanceOf(NotExistsException.class);
        verify(userRepository).existsById(user.getId());
    }

    @Test
    void shouldThrowErrorWhenEditUserByIdWhenOtherUserByEmailAlreadyExists() {
        User user = mockUser.mock();
        User otherUser = mockUser.mock()
                .withEmail(new Email("other@email.com"));
        given(userRepository.existsById(user.getId()))
                .willReturn(true);
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(true);
        given(userRepository.findByEmail(user.getEmail().getAddress()))
                .willReturn(Optional.of(otherUser));
        assertThatThrownBy(() -> userService.edit(user))
                .isInstanceOf(AlreadyExistsException.class);
        verify(userRepository).existsById(user.getId());
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
        verify(userRepository).findByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldGetUserByIdWhenUserByIdExists() {
        User user = mockUser.mock();
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        User userById = userService.getById(user.getId());
        assertThat(userById).isEqualTo(user);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldGetUserByEmailWhenUserByEmailExists() {
        User user = mockUser.mock();
        given(userRepository.findByEmail(user.getEmail().getAddress()))
                .willReturn(Optional.of(user));
        User userByEmail = userService.getByEmail(user.getEmail().getAddress());
        assertThat(userByEmail).isEqualTo(user);
        verify(userRepository).findByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldThrowErrorWhenGetUserByIdWhenUserByEmailIdExists() {
        User user = mockUser.mock();
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getById(user.getId()))
                .isInstanceOf(NotExistsException.class);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldThrowErrorWhenGetUserByEmailWhenUserByEmailNotExists() {
        User user = mockUser.mock();
        given(userRepository.findByEmail(user.getEmail().getAddress()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getByEmail(user.getEmail().getAddress()))
                .isInstanceOf(NotExistsException.class);
        verify(userRepository).findByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldReturnTrueWhenUserByIdExists() {
        User user = mockUser.mock();
        given(userRepository.existsById(user.getId()))
                .willReturn(true);
        assertThat(userService.existsById(user.getId()))
                .isTrue();
        verify(userRepository).existsById(user.getId());
    }

    @Test
    void shouldReturnFalseWhenUserByIdNotExists() {
        User user = mockUser.mock();
        given(userRepository.existsById(user.getId()))
                .willReturn(false);
        assertThat(userService.existsById(user.getId()))
                .isFalse();
        verify(userRepository).existsById(user.getId());
    }

    @Test
    void shouldReturnTrueWhenUserByEmailExists() {
        User user = mockUser.mock();
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(true);
        assertThat(userService.existsByEmail(user.getEmail().getAddress()))
                .isTrue();
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldReturnFalseWhenUserByEmailNotExists() {
        User user = mockUser.mock();
        given(userRepository.existsByEmail(user.getEmail().getAddress()))
                .willReturn(false);
        assertThat(userService.existsByEmail(user.getEmail().getAddress()))
                .isFalse();
        verify(userRepository).existsByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldReturnUsersCount() {
        long expectedCount = 10;
        given(userRepository.count()).willReturn(expectedCount);
        assertThat(userService.getCount()).isEqualTo(expectedCount);
        verify(userRepository).count();
    }

    @Test
    void shouldGetUsersPage() {
        Pageable pageable = Pageable.unpaged();
        Page<User> users = mockUser.mockPage(pageable);
        given(userRepository.findAll(pageable)).willReturn(users);
        assertThat(userService.getAll(pageable))
                .containsAll(users)
                .hasSize(1);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void shouldSearchAndGetUserPage() {
        Pageable pageable = Pageable.unpaged();
        Page<User> users = mockUser.mockPage(pageable);
        String search = "text";
        given(userRepository.search(search, pageable)).willReturn(users);
        assertThat(userService.search(search, pageable))
                .containsAll(users)
                .hasSize(1);
        verify(userRepository).search(search, pageable);
    }

    @Test
    void shouldDeleteUserByIdWhenUserByIdExists() {
        User user = mockUser.mock();
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        userService.deleteById(user.getId());
        verify(userRepository).findById(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void shouldThrowErrorWhenDeleteUserByIdWhenUserByIdNotExists() {
        User user = mockUser.mock();
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteById(user.getId()))
                .isInstanceOf(NotExistsException.class);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldAddAuthorityWhenUserHasNotAuthorityByName() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        given(userAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .willReturn(false);
        given(userAuthorityRepository.save(userAuthority))
                .willReturn(userAuthority);
        userService.addAuthority(userAuthority);
        verify(userAuthorityRepository).existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority());
        verify(userAuthorityRepository).save(userAuthority);
    }

    @Test
    void shouldThrowErrorWhenAddAuthorityWhenUserAlreadyHasAuthorityByName() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        given(userAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .willReturn(true);
        assertThatThrownBy(() -> userService.addAuthority(userAuthority))
                .isInstanceOf(AlreadyExistsException.class);
        verify(userAuthorityRepository).existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority());
    }

    @Test
    void shouldGetUserAuthoritiesPage() {
        Pageable pageable = Pageable.unpaged();
        Page<UserAuthority> userAuthorities = mockUserAuthority.mockPage(pageable);
        String userId = userAuthorities.getContent().get(0).getUserId();
        given(userAuthorityRepository.findAllByUserId(userId, pageable))
                .willReturn(userAuthorities);
        Page<UserAuthority> authorities = userService.getAuthorities(userId, pageable);
        assertThat(authorities)
                .containsAll(userAuthorities);
        verify(userAuthorityRepository).findAllByUserId(userId, pageable);
    }

    @Test
    void shouldDeleteUserAuthorityWhenUserHasAuthorityByName() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        given(userAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .willReturn(true);
        userService.deleteAuthority(userAuthority);
        verify(userAuthorityRepository).existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority());
        verify(userAuthorityRepository).existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority());
    }

    @Test
    void shouldThrowErrorWhenDeleteUserAuthorityWhenUserHasNoAuthorityByName() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        given(userAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .willReturn(false);
        assertThatThrownBy(() -> userService.deleteAuthority(userAuthority))
                .isInstanceOf(NotExistsException.class);
        verify(userAuthorityRepository).existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority());
    }
}
