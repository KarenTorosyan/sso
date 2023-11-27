package auth.entities.user.out.persistence.mongo;

import auth.IntegrateTestContainers;
import auth.entities.authority.Authority;
import auth.entities.authority.out.persistence.mongo.MongoAuthorityRepository;
import auth.entities.user.UserAuthority;
import auth.mock.MockUserAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({MongoUserAuthorityRepository.class, MongoAuthorityRepository.class})
public class MongoUserAuthorityRepositoryTest extends IntegrateTestContainers.Mongo {

    @Autowired
    private MongoAuthorityRepository mongoAuthorityRepository;

    @Autowired
    private MongoUserAuthorityRepository mongoUserAuthorityRepository;

    private final MockUserAuthority mockUserAuthority = new MockUserAuthority();

    @Test
    void shouldSaveUserAuthority() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(userAuthority.getAuthority());
        UserAuthority savedUserAuthority = mongoUserAuthorityRepository.save(userAuthority);
        mongoUserAuthorityRepository.save(userAuthority);
        assertThat(savedUserAuthority)
                .isEqualTo(userAuthority);
        assertThat(mongoUserAuthorityRepository.find(userAuthority))
                .contains(userAuthority);
        mongoUserAuthorityRepository.delete(savedUserAuthority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldReturnUserAuthorityWhenUserAuthorityExists() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(userAuthority.getAuthority());
        UserAuthority savedUserAuthority = mongoUserAuthorityRepository.save(userAuthority);
        assertThat(mongoUserAuthorityRepository.find(userAuthority))
                .contains(userAuthority);
        mongoUserAuthorityRepository.delete(savedUserAuthority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldReturnEmptyWhenUserAuthorityNotExists() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        assertThat(mongoUserAuthorityRepository.find(userAuthority))
                .isEmpty();
    }

    @Test
    void shouldReturnTrueWhenUserAuthorityByUserIdAndAuthorityExists() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        UserAuthority savedUserAuthority = mongoUserAuthorityRepository.save(userAuthority);
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isTrue();
        mongoUserAuthorityRepository.delete(savedUserAuthority);
    }

    @Test
    void shouldReturnFalseWhenUserAuthorityByUserIdAndAuthorityNotExists() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isFalse();
    }

    @Test
    void shouldFindAndReturnUserAuthoritiesByUserId() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(userAuthority.getAuthority());
        UserAuthority savedUserAuthority = mongoUserAuthorityRepository.save(userAuthority);
        assertThat(mongoUserAuthorityRepository.findAllByUserId(
                userAuthority.getUserId(), Pageable.unpaged()))
                .contains(userAuthority)
                .hasSize(1);
        mongoUserAuthorityRepository.delete(savedUserAuthority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldDeleteUserAuthorityWhenUserUserHasThisAuthority() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        UserAuthority savedUserAuthority = mongoUserAuthorityRepository.save(userAuthority);
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isTrue();
        mongoUserAuthorityRepository.delete(savedUserAuthority);
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isFalse();
    }

    @Test
    void shouldBeNoActionWhenDeleteUserAuthorityWhenUserHasNoThisAuthority() {
        UserAuthority userAuthority = mockUserAuthority.mock();
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isFalse();
        mongoUserAuthorityRepository.delete(userAuthority);
    }
}
