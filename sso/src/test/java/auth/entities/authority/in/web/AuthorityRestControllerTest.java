package auth.entities.authority.in.web;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.configs.security.InitialAuthorities;
import auth.entities.authority.Authority;
import auth.entities.authority.AuthorityService;
import auth.errors.Errors;
import auth.mock.MockAuthority;
import auth.mock.MockPrincipal;
import auth.utils.ObjectMapperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorityRestController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class AuthorityRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorityService authorityService;

    private final MockAuthority mockAuthority = new MockAuthority();

    @Test
    void shouldGetAuthoritiesPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<Authority> authoritiesPage = mockAuthority.mockPage(pageable);
        given(authorityService.getAll(pageable))
                .willReturn(authoritiesPage);
        mockMvc.perform(get(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(authoritiesPage.map(AuthorityProjection::from))));
        verify(authorityService).getAll(pageable);
    }

    @Test
    void shouldSearchAndReturnAuthoritiesPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3).withSort(Sort.by("name").ascending());
        Page<Authority> authoritiesPage = mockAuthority.mockPage(pageable);
        String search = "text";
        given(authorityService.search(search, pageable))
                .willReturn(authoritiesPage);
        mockMvc.perform(get(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .param("term", search)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(authoritiesPage.map(AuthorityProjection::from))));
        verify(authorityService).search(search, pageable);
    }

    @Test
    void shouldSendErrorResponseWhenGetAllOrSearchAuthoritiesWhenClientUnauthorized() throws Exception {
        mockMvc.perform(get(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenGetAllOrSearchAuthoritiesWhenClientNotEnoughPermissions() throws Exception {
        mockMvc.perform(get(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetAuthorityByIdWhenAuthorityByIdExists() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willReturn(authority);
        mockMvc.perform(get(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(AuthorityProjection.from(authority))));
        verify(authorityService).getById(authority.getId());
    }

    @Test
    void shouldSendErrorResponseWhenGetAuthorityByIdWhenAuthorityByNameNotExists() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willThrow(Errors.authorityNotExistsById(authority.getId()));
        mockMvc.perform(get(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenGeyAuthorityByIdWhenClientUnauthorized() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(get(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenGeyAuthorityByIdWhenClientNotEnoughAccess() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(get(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCreateAuthorityWhenRequestDataValidAndAuthorityByNameNotExists() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.create(authority))
                .willReturn(authority);
        AuthorityCreateRequest authorityCreateRequest = new AuthorityCreateRequest();
        authorityCreateRequest.setName(authority.getName());
        authorityCreateRequest.setDescription(authority.getDescription());
        mockMvc.perform(post(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(authorityCreateRequest)))
                .andExpect(status().isNoContent());
        verify(authorityService).create(authority);
    }

    @Test
    void shouldSendErrorResponseWhenCreateAuthorityWhenRequestDataInvalid() throws Exception {
        Authority authority = mockAuthority.mock()
                .withName("");
        AuthorityCreateRequest authorityCreateRequest = new AuthorityCreateRequest();
        authorityCreateRequest.setName(authority.getName());
        authorityCreateRequest.setDescription(authority.getDescription());
        mockMvc.perform(post(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(authorityCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorResponseWhenCreateAuthorityWhenAuthorityByNameAlreadyExist() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.create(authority))
                .willThrow(Errors.authorityAlreadyExistsByName(authority.getName()));
        AuthorityCreateRequest authorityCreateRequest = new AuthorityCreateRequest();
        authorityCreateRequest.setName(authority.getName());
        authorityCreateRequest.setDescription(authority.getDescription());
        mockMvc.perform(post(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(authorityCreateRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldSendErrorResponseWhenCreateAuthorityWhenClientUnauthorized() throws Exception {
        mockMvc.perform(post(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenCreateAuthorityWhenClientNotEnoughPermissions() throws Exception {
        mockMvc.perform(post(Endpoints.AUTHORITIES)
                        .with(MockPrincipal.authorization())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldEditAuthorityByIdWhenAuthorityByIdExists() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willReturn(authority);
        given(authorityService.edit(authority))
                .willReturn(authority);
        AuthorityEditRequest authorityEditRequest = new AuthorityEditRequest();
        authorityEditRequest.setName(authority.getName());
        authorityEditRequest.setDescription(authority.getDescription());
        mockMvc.perform(put(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(authorityEditRequest.getModifiedAuthority(authority))))
                .andExpect(status().isNoContent());
        verify(authorityService).getById(authority.getId());
        verify(authorityService).edit(authorityEditRequest.getModifiedAuthority(authority));
    }

    @Test
    void shouldSendErrorResponseWhenEditAuthorityByIdWhenAuthorityByIdNotExists() throws Exception {
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willThrow(Errors.authorityNotExistsById(authority.getId()));
        AuthorityEditRequest authorityEditRequest = new AuthorityEditRequest();
        authorityEditRequest.setName(authority.getName());
        authorityEditRequest.setDescription(authority.getDescription());
        mockMvc.perform(put(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(authorityEditRequest.getModifiedAuthority(authority))))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenEditAuthorityWhenClientUnauthorized() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(put(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenEditAuthorityWhenClientNotEnoughPermissions() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(put(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteAuthorityByIdWhenAuthorityByIdExists() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(delete(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(authorityService).deleteById(authority.getId());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteAuthorityByIdWhenAuthorityByIdNotExists() throws Exception {
        Authority authority = mockAuthority.mock();
        doThrow(Errors.authorityNotExistsById(authority.getId()))
                .when(authorityService).deleteById(authority.getId());
        mockMvc.perform(delete(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteAuthorityWhenClientUnauthorized() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(delete(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteAuthorityWhenClientNotEnoughPermissions() throws Exception {
        Authority authority = mockAuthority.mock();
        mockMvc.perform(delete(Endpoints.AUTHORITIES.concat("/{id}"), authority.getId())
                        .with(MockPrincipal.authorization())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
