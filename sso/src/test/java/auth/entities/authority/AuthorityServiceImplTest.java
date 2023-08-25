package auth.entities.authority;

import auth.entities.authority.out.persistence.AuthorityRepository;
import auth.errors.AlreadyExistsException;
import auth.errors.NotExistsException;
import auth.mock.MockAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = AuthorityServiceImpl.class)
public class AuthorityServiceImplTest {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @MockBean
    private AuthorityRepository authorityRepository;

    private final MockAuthority mockAuthority = new MockAuthority();

    @Test
    void shouldCreateAuthorityWhenNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsByName(authority.getName()))
                .willReturn(false);
        given(authorityRepository.save(authority))
                .willReturn(authority);
        Authority createdAuthority = authorityService.create(authority);
        assertThat(createdAuthority).isEqualTo(authority);
        verify(authorityRepository).existsByName(authority.getName());
        verify(authorityRepository).save(authority);
    }

    @Test
    void shouldThrowErrorWhenCreateAuthorityWhenAuthorityAlreadyExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsByName(authority.getName()))
                .willReturn(true);
        assertThatThrownBy(() -> authorityService.create(authority))
                .isInstanceOf(AlreadyExistsException.class);
        verify(authorityRepository).existsByName(authority.getName());
    }

    @Test
    void shouldEditAuthorityWhenAuthorityByIdExistsAndOtherAuthorityByNameNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsById(authority.getId()))
                .willReturn(true);
        given(authorityRepository.existsByName(authority.getName()))
                .willReturn(false);
        given(authorityRepository.save(authority))
                .willReturn(authority);
        Authority editedAuthority = authorityService.edit(authority);
        assertThat(editedAuthority).isEqualTo(authority);
        verify(authorityRepository).existsById(authority.getId());
        verify(authorityRepository).existsByName(authority.getName());
    }

    @Test
    void shouldThrowErrorWhenEditAuthorityWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsById(authority.getId()))
                .willReturn(false);
        assertThatThrownBy(() -> authorityService.edit(authority))
                .isInstanceOf(NotExistsException.class);
        verify(authorityRepository).existsById(authority.getId());
    }

    @Test
    void shouldThrowErrorWhenEditAuthorityWhenOtherAuthorityByNameExists() {
        Authority authority = mockAuthority.mock();
        Authority otherAuthority = mockAuthority.mock()
                .withName("other");
        given(authorityRepository.existsById(authority.getId()))
                .willReturn(true);
        given(authorityRepository.existsByName(authority.getName()))
                .willReturn(true);
        given(authorityRepository.findByName(authority.getName()))
                .willReturn(Optional.of(otherAuthority));
        assertThatThrownBy(() -> authorityService.edit(authority))
                .isInstanceOf(AlreadyExistsException.class);
        verify(authorityRepository).existsById(authority.getId());
        verify(authorityRepository).existsByName(authority.getName());
        verify(authorityRepository).findByName(authority.getName());
    }

    @Test
    void shouldGetAuthorityByIdWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findById(authority.getId()))
                .willReturn(Optional.of(authority));
        Authority existenceAuthority = authorityService.getById(authority.getId());
        assertThat(existenceAuthority).isEqualTo(authority);
        verify(authorityRepository).findById(authority.getId());
    }

    @Test
    void shouldThrowErrorWhenGetAuthorityByIdWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findById(authority.getId()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> authorityService.getById(authority.getId()))
                .isInstanceOf(NotExistsException.class);
        verify(authorityRepository).findById(authority.getId());
    }

    @Test
    void shouldGetAuthorityByNameWhenAuthorityByNameExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findByName(authority.getName()))
                .willReturn(Optional.of(authority));
        Authority existenceAuthority = authorityService.getByName(authority.getName());
        assertThat(existenceAuthority).isEqualTo(authority);
        verify(authorityRepository).findByName(authority.getName());
    }

    @Test
    void shouldThrowErrorWhenGetAuthorityByNameWhenAuthorityByNameNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findByName(authority.getName()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> authorityService.getByName(authority.getName()))
                .isInstanceOf(NotExistsException.class);
        verify(authorityRepository).findByName(authority.getName());
    }

    @Test
    void shouldReturnTrueWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsById(authority.getId())).willReturn(true);
        assertThat(authorityService.existsById(authority.getId())).isTrue();
        verify(authorityRepository).existsById(authority.getId());
    }

    @Test
    void shouldReturnFalseWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsById(authority.getId())).willReturn(false);
        assertThat(authorityService.existsById(authority.getId())).isFalse();
        verify(authorityRepository).existsById(authority.getId());
    }

    @Test
    void shouldReturnTrueWhenAuthorityByNameExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsByName(authority.getName())).willReturn(true);
        assertThat(authorityService.existsByName(authority.getName())).isTrue();
        verify(authorityRepository).existsByName(authority.getName());
    }

    @Test
    void shouldReturnFalseWhenAuthorityByNameNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.existsByName(authority.getName())).willReturn(false);
        assertThat(authorityService.existsByName(authority.getName())).isFalse();
        verify(authorityRepository).existsByName(authority.getName());
    }

    @Test
    void shouldGetAuthoritiesCount() {
        long expectedCount = 10;
        given(authorityRepository.count()).willReturn(expectedCount);
        assertThat(authorityService.getCount()).isEqualTo(expectedCount);
        verify(authorityRepository).count();
    }

    @Test
    void shouldGetAuthoritiesPage() {
        Page<Authority> authoritiesPage = new PageImpl<>(List.copyOf(mockAuthority.mockCollection()));
        Pageable pageable = Pageable.unpaged();
        given(authorityRepository.findAll(pageable))
                .willReturn(authoritiesPage);
        Page<Authority> existenceAuthoritiesPage = authorityService.getAll(pageable);
        assertThat(existenceAuthoritiesPage).isEqualTo(authoritiesPage);
        verify(authorityRepository).findAll(pageable);
    }

    @Test
    void shouldSearchAndReturnAuthoritiesPage() {
        Page<Authority> authoritiesPage = new PageImpl<>(List.copyOf(mockAuthority.mockCollection()));
        String search = "text";
        Pageable pageable = Pageable.unpaged();
        given(authorityRepository.search(search, pageable)).willReturn(authoritiesPage);
        Page<Authority> searchResult = authorityService.search(search, pageable);
        assertThat(searchResult).isEqualTo(authoritiesPage);
        verify(authorityRepository).search(search, pageable);
    }

    @Test
    void shouldDeleteAuthorityByIdWhenAuthorityByIdExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findById(authority.getId()))
                .willReturn(Optional.of(authority));
        authorityService.deleteById(authority.getId());
        verify(authorityRepository).findById(authority.getId());
        verify(authorityRepository).deleteById(authority.getId());
    }

    @Test
    void shouldThrowErrorWhenDeleteAuthorityByIdWhenAuthorityByIdNotExists() {
        Authority authority = mockAuthority.mock();
        given(authorityRepository.findById(authority.getId()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> authorityService.deleteById(authority.getId()))
                .isInstanceOf(NotExistsException.class);
        verify(authorityRepository).findById(authority.getId());
    }
}
