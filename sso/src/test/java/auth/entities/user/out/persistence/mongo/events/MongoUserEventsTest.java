package auth.entities.user.out.persistence.mongo.events;

import auth.IntegrateTestContainers;
import auth.configs.security.InitialAuthorities;
import auth.entities.authority.Authority;
import auth.entities.authority.out.persistence.mongo.MongoAuthorityRepository;
import auth.entities.user.Email;
import auth.entities.user.User;
import auth.entities.user.UserAuthority;
import auth.entities.user.out.persistence.mongo.MongoUserAuthorityRepository;
import auth.entities.user.out.persistence.mongo.MongoUserRepository;
import auth.mock.MockAuthority;
import auth.mock.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({MongoUserEvents.class,
        MongoUserRepository.class,
        MongoAuthorityRepository.class,
        MongoUserAuthorityRepository.class,
})
public class MongoUserEventsTest extends IntegrateTestContainers.Mongo {

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private MongoAuthorityRepository mongoAuthorityRepository;

    @Autowired
    private MongoUserAuthorityRepository mongoUserAuthorityRepository;

    private final MockUser mockUser = new MockUser();

    @Test
    void shouldAddInitialAdminAuthoritiesAfterSaveUserWhenSavedUserIsFirst() {
        List<Authority> initialAdminAuthorities = Stream.of(InitialAuthorities.admin())
                .map(name -> new Authority(name).withId(name))
                .toList();
        initialAdminAuthorities.forEach(mongoAuthorityRepository::save);

        User admin = mockUser.mock();
        mongoUserRepository.save(admin);

        assertThat(mongoUserAuthorityRepository.findAllByUserId(admin.getId(), Pageable.unpaged()))
                .containsAll(initialAdminAuthorities.stream()
                        .map(authority -> new UserAuthority(admin.getId(), authority))
                        .toList())
                .hasSize(initialAdminAuthorities.size());

        mongoUserRepository.deleteById(admin.getId());
        initialAdminAuthorities.forEach(authority -> mongoAuthorityRepository.deleteById(authority.getId()));
    }

    @Test
    void shouldAddInitialUserAuthoritiesAfterSaveUserWhenSavedUserIsNotFirst() {
        List<Authority> initialUserAuthorities = Stream.of(InitialAuthorities.user())
                .map(name -> new Authority(name).withId(name))
                .toList();
        initialUserAuthorities.forEach(mongoAuthorityRepository::save);

        User admin = mockUser.mock().withId("1").withEmail(new Email("admin@email.com"));
        mongoUserRepository.save(admin);

        User user = mockUser.mock().withId("2").withEmail(new Email("user@email.com"));
        mongoUserRepository.save(user);

        assertThat(mongoUserAuthorityRepository.findAllByUserId(user.getId(), Pageable.unpaged()))
                .containsAll(initialUserAuthorities.stream()
                        .map(authority -> new UserAuthority(user.getId(), authority))
                        .toList())
                .hasSize(initialUserAuthorities.size());

        mongoUserRepository.deleteById(admin.getId());
        mongoUserRepository.deleteById(user.getId());
        initialUserAuthorities.forEach(authority -> mongoAuthorityRepository.deleteById(authority.getId()));
    }

    @Test
    void shouldDeleteUserAuthoritiesBeforeUserDeleting() {
        User user = mockUser.mock();
        Authority authority = new MockAuthority().mock();
        UserAuthority userAuthority = new UserAuthority(user.getId(), authority);
        mongoUserRepository.save(user);
        mongoAuthorityRepository.save(authority);
        mongoUserAuthorityRepository.save(userAuthority);

        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isTrue();
        assertThat(mongoAuthorityRepository.existsByName(authority.getName()))
                .isTrue();
        assertThat(mongoUserAuthorityRepository.findAllByUserId(userAuthority.getUserId(), Pageable.unpaged()))
                .contains(userAuthority)
                .hasSize(1);

        mongoUserRepository.deleteById(user.getId());

        assertThat(mongoUserRepository.existsByEmail(user.getEmail().getAddress()))
                .isFalse();
        assertThat(mongoAuthorityRepository.existsByName(authority.getName()))
                .isTrue();
        assertThat(mongoUserAuthorityRepository.findAllByUserId(userAuthority.getUserId(), Pageable.unpaged()))
                .isEmpty();

        mongoUserRepository.deleteById(user.getId());
        mongoAuthorityRepository.deleteById(authority.getId());
    }
}
