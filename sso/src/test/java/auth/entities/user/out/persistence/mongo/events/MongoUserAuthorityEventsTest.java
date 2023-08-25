package auth.entities.user.out.persistence.mongo.events;

import auth.entities.authority.out.persistence.mongo.MongoAuthorityRepository;
import auth.entities.user.UserAuthority;
import auth.entities.user.out.persistence.mongo.MongoUserAuthorityRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import auth.IntegrateTestContainers;
import auth.mock.MockUserAuthority;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({
        MongoUserAuthorityEvents.class,
        MongoAuthorityRepository.class,
        MongoUserAuthorityRepository.class
})
public class MongoUserAuthorityEventsTest extends IntegrateTestContainers.Mongo {

    @Autowired
    private MongoAuthorityRepository mongoAuthorityRepository;

    @Autowired
    private MongoUserAuthorityRepository mongoUserAuthorityRepository;

    private final MockUserAuthority mockUserAuthority = new MockUserAuthority();

    @Test
    void shouldDeleteUserAuthorityBeforeAuthorityDeleting() {
        UserAuthority userAuthority = mockUserAuthority.mock().withId("1");
        mongoAuthorityRepository.save(userAuthority.getAuthority());
        mongoUserAuthorityRepository.save(userAuthority);

        assertThat(mongoAuthorityRepository.existsById(userAuthority.getAuthority().getId()))
                .isTrue();
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isTrue();

        mongoAuthorityRepository.deleteById(userAuthority.getAuthority().getId());
        assertThat(mongoAuthorityRepository.existsById(userAuthority.getAuthority().getId()))
                .isFalse();
        assertThat(mongoUserAuthorityRepository.existsByUserIdAndAuthority(
                userAuthority.getUserId(), userAuthority.getAuthority()))
                .isFalse();
    }
}
