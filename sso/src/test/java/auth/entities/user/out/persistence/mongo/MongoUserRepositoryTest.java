package auth.entities.user.out.persistence.mongo;

import auth.IntegrateTestContainers;
import auth.entities.user.User;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoUserRepository.class)
public class MongoUserRepositoryTest extends IntegrateTestContainers.Mongo {

    @Autowired
    private MongoUserRepository mongoUserRepository;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldSaveUser() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        mongoUserRepository.save(user);
        assertThat(savedUser)
                .isEqualTo(user);
        assertThat(mongoUserRepository.findByEmail(user.getEmail().getAddress()))
                .contains(user);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldReturnUserWhenUserByIdExists() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.findById(user.getId()))
                .contains(user);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldReturnEmptyWhenUserByIdNotExists() {
        User user = mockUser.mock();
        assertThat(mongoUserRepository.findById(user.getId()))
                .isEmpty();
    }

    @Test
    void shouldReturnUserWhenUserByEmailExists() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.findByEmail(user.getEmail().getAddress()))
                .contains(user);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldReturnEmptyWhenUserByEmailNotExists() {
        User user = mockUser.mock();
        assertThat(mongoUserRepository.findByEmail(user.getEmail().getAddress()))
                .isEmpty();
    }

    @Test
    void shouldReturnTrueWhenUserByIdExists() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.existsById(user.getId()))
                .isTrue();
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldReturnFalseWhenUserByIdNotExists() {
        User user = mockUser.mock();
        assertThat(mongoUserRepository.existsByEmail(user.getId()))
                .isFalse();
    }

    @Test
    void shouldReturnTrueWhenUserByEmailExists() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isTrue();
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldReturnFalseWhenUserByEmailNotExists() {
        User user = mockUser.mock();
        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isFalse();
    }

    @Test
    void shouldFindUsersCount() {
        assertThat(mongoUserRepository.count()).isEqualTo(0);
        mongoUserRepository.save(mockUser.mock());
        assertThat(mongoUserRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldFindAllAndReturnUsersPage() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.findAll(Pageable.unpaged()))
                .contains(user)
                .hasSize(1);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldFindAllByIdAndReturnPage() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        Set<String> ids = Set.of(savedUser.getId());
        assertThat(mongoUserRepository.findAllById(ids, Pageable.unpaged()))
                .contains(user)
                .hasSize(1);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldFindAllByEmailAndReturnPage() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        Set<String> emails = Set.of(user.getEmail().getAddress());
        assertThat(mongoUserRepository.findAllByEmail(emails, Pageable.unpaged()))
                .contains(user)
                .hasSize(1);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldSearchAndReturnUsersPage() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.search(user.getName(), Pageable.unpaged()))
                .contains(user)
                .hasSize(1);
        mongoUserRepository.deleteById(savedUser.getId());
    }

    @Test
    void shouldDeleteUserByEmailWhenUserByEmailExists() {
        User user = mockUser.mock();
        User savedUser = mongoUserRepository.save(user);
        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isTrue();
        mongoUserRepository.deleteById(savedUser.getId());
        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isFalse();
    }

    @Test
    void shouldBeNoActionWhenDeleteUserByEmailWhenUserByEmailNotExists() {
        User user = mockUser.mock();
        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isFalse();
        mongoUserRepository.deleteById(user.getId());
    }
}
