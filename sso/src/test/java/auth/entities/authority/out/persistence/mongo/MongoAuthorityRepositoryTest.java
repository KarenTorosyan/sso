package auth.entities.authority.out.persistence.mongo;

import auth.IntegrateTestContainers;
import auth.entities.authority.Authority;
import auth.mock.MockAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoAuthorityRepository.class)
public class MongoAuthorityRepositoryTest extends IntegrateTestContainers.Mongo {

    @Autowired
    private MongoAuthorityRepository mongoAuthorityRepository;

    private final MockAuthority mockAuthority = new MockAuthority();

    @Test
    void shouldSaveAuthority() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        mongoAuthorityRepository.save(authority);
        assertThat(savedAuthority).isEqualTo(authority);
        assertThat(mongoAuthorityRepository.findById(authority.getId()))
                .contains(authority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldFindAuthorityByIdWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.findById(authority.getId()))
                .contains(authority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldFindAuthorityByNameWhenAuthorityByNameExists() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.findByName(authority.getName()))
                .contains(authority);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldReturnEmptyWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        assertThat(mongoAuthorityRepository.findById(authority.getId())).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenAuthorityByNameNotExists() {
        Authority authority = mockAuthority.mock();
        assertThat(mongoAuthorityRepository.findByName(authority.getName())).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.existsById(authority.getId())).isTrue();
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldReturnFalseWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        assertThat(mongoAuthorityRepository.existsById(authority.getId())).isFalse();
    }

    @Test
    void shouldReturnTrueWhenAuthorityByNameExists() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.existsByName(authority.getName())).isTrue();
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldReturnFalseWhenAuthorityByNameNotExists() {
        Authority authority = mockAuthority.mock();
        assertThat(mongoAuthorityRepository.existsByName(authority.getName())).isFalse();
    }

    @Test
    void shouldFindAuthoritiesCount() {
        assertThat(mongoAuthorityRepository.count()).isEqualTo(0);
        Authority savedAuthority = mongoAuthorityRepository.save(mockAuthority.mock());
        assertThat(mongoAuthorityRepository.count()).isEqualTo(1);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldFindAllAndReturnAuthoritiesPage() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.findAll(Pageable.unpaged()))
                .contains(authority)
                .hasSize(1);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldSearchAndReturnAuthoritiesPage() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.search(authority.getName(), Pageable.unpaged()))
                .contains(authority)
                .hasSize(1);
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
    }

    @Test
    void shouldDeleteAuthorityByIdWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        Authority savedAuthority = mongoAuthorityRepository.save(authority);
        assertThat(mongoAuthorityRepository.existsById(authority.getId())).isTrue();
        mongoAuthorityRepository.deleteById(savedAuthority.getId());
        assertThat(mongoAuthorityRepository.existsById(authority.getId())).isFalse();
    }

    @Test
    void shouldBeNoActionWhenDeleteAuthorityWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        assertThat(mongoAuthorityRepository.existsById(authority.getId())).isFalse();
        mongoAuthorityRepository.deleteById(authority.getId());
    }
}
