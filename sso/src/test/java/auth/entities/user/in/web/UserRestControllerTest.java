package auth.entities.user.in.web;

import auth.Endpoints;
import auth.IntegrateSecurityConfig;
import auth.IntegrateWebConfig;
import auth.configs.security.InitialAuthorities;
import auth.configs.verification.VerificationProperties;
import auth.entities.authority.Authority;
import auth.entities.authority.AuthorityService;
import auth.entities.authority.in.web.AuthorityProjection;
import auth.entities.user.*;
import auth.errors.Errors;
import auth.intergrations.filesystem.FileService;
import auth.mock.MockAuthority;
import auth.mock.MockPrincipal;
import auth.mock.MockUser;
import auth.mock.MockUserAuthority;
import auth.utils.ObjectMapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.Instant;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@IntegrateWebConfig
@IntegrateSecurityConfig
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailVerifier emailVerifier;

    @MockBean
    private FileService fileService;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private VerificationProperties verificationProperties;

    private final MockUser mockUser = new MockUser();

    private final MockUserAuthority mockUserAuthority = new MockUserAuthority();

    private final MockAuthority mockAuthority = new MockAuthority();

    @Test
    void shouldGetUsersPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3)
                .withSort(Sort.by("name").ascending());
        Page<User> users = mockUser.mockPage(pageable);
        given(userService.getAll(pageable))
                .willReturn(users);
        mockMvc.perform(get(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(users.map(UserProjection::from))));
        verify(userService).getAll(pageable);
    }

    @Test
    void shouldSearchAndReturnUsersPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3)
                .withSort(Sort.by("name").ascending());
        Page<User> users = mockUser.mockPage(pageable);
        String search = "text";
        given(userService.search(search, pageable))
                .willReturn(users);
        mockMvc.perform(get(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .param("term", search)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(users.map(UserProjection::from))));
        verify(userService).search(search, pageable);
    }

    @Test
    void shouldGetUsersByIds() throws Exception {
        Set<String> ids = Set.of("1", "2");
        Pageable pageable = PageRequest.of(0, 3)
                .withSort(Sort.by("name").ascending());
        Page<User> users = mockUser.mockPage(pageable);
        given(userService.getAllById(ids, pageable))
                .willReturn(users);
        mockMvc.perform(get(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .param("id", String.join(",", ids))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(users.map(UserProjection::from))));
        verify(userService).getAllById(ids, pageable);
    }

    @Test
    void shouldGetUsersByEmails() throws Exception {
        Set<String> emails = Set.of("one@email.com", "two@email.com");
        Pageable pageable = PageRequest.of(0, 3)
                .withSort(Sort.by("name").ascending());
        Page<User> users = mockUser.mockPage(pageable);
        given(userService.getAllByEmail(emails, pageable))
                .willReturn(users);
        mockMvc.perform(get(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .param("email", String.join(",", emails))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(users.map(UserProjection::from))));
        verify(userService).getAllByEmail(emails, pageable);
    }

    @Test
    void shouldGetUserByIdWhenUserByIdExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        mockMvc.perform(get(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.anonymous())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(UserProjection.from(user))));
        verify(userService).getById(user.getId());
    }

    @Test
    void shouldSendErrorResponseWhenGetUserByIdWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(get(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.anonymous())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService).getById(user.getId());
    }

    @Test
    void shouldGetUserByEmailWhenUserByEmailExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress()))
                .willReturn(user);
        mockMvc.perform(get(Endpoints.USERS + "/email/{email}", user.getEmail().getAddress())
                        .with(MockPrincipal.anonymous())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(UserProjection.from(user))));
        verify(userService).getByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldSendErrorResponseWhenGetUserByEmailWhenUserByEmailNotExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getByEmail(user.getEmail().getAddress()))
                .willThrow(Errors.userNotExistsByEmail(user.getEmail().getAddress()));
        mockMvc.perform(get(Endpoints.USERS + "/email/{email}", user.getEmail().getAddress())
                        .with(MockPrincipal.anonymous())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService).getByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldRegisterUserWhenRequestDataValidAndUserByEmailNotExists() throws Exception {
        User user = mockUser.mock();
        given(verificationProperties.isSendEmail())
                .willReturn(false);
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.create(user))
                .willReturn(user);
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        mockMvc.perform(post(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", Matchers.endsWith(UriComponentsBuilder.newInstance()
                        .path(Endpoints.USERS).path("/{id}").build(user.getId()).toString())));
        verify(verificationProperties).isSendEmail();
        verify(passwordEncoder).encode(user.getPassword().getValue());
        verify(userService).create(user);
    }

    @Test
    void shouldRegisterUserWhenRequestDataValidAndUserByEmailNotExistsAndUseEmailVerificationMechanism() throws Exception {
        User user = mockUser.mock();
        given(verificationProperties.isSendEmail())
                .willReturn(true);
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.existsByEmail(user.getEmail().getAddress()))
                .willReturn(false);
        given(userService.create(user))
                .willReturn(user);
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        mockMvc.perform(post(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", Matchers.endsWith(UriComponentsBuilder.newInstance()
                        .path(Endpoints.USERS).path("/{id}").build(user.getId()).toString())));
        verify(verificationProperties).isSendEmail();
        verify(userService).existsByEmail(user.getEmail().getAddress());
        verify(emailVerifier).verify(any(HttpServletRequest.class), any(Email.class));
        verify(passwordEncoder).encode(user.getPassword().getValue());
        verify(userService).create(user);
    }

    @Test
    void shouldSendErrorResponseWhenRegisterUserWhenRequestDataInvalid() throws Exception {
        User user = mockUser.mock()
                .withEmail(new Email("email"));
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        mockMvc.perform(post(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorResponseWhenRegisterUserWhenUserByEmailAlreadyExists() throws Exception {
        User user = mockUser.mock();
        given(verificationProperties.isSendEmail())
                .willReturn(true);
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.existsByEmail(user.getEmail().getAddress()))
                .willReturn(true);
        given(userService.create(user))
                .willThrow(Errors.userAlreadyExistsByEmail(user.getEmail().getAddress()));
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        mockMvc.perform(post(Endpoints.USERS)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userCreateRequest)))
                .andExpect(status().isConflict());
        verify(userService).existsByEmail(user.getEmail().getAddress());
    }

    @Test
    void shouldRegisterUserWithUploadPictureLoadAsMultipartData() throws Exception {
        User user = mockUser.mock();
        MockMultipartFile picturePart = new MockMultipartFile("picture", "picture.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[]{});
        given(verificationProperties.isSendEmail())
                .willReturn(false);
        given(fileService.upload(picturePart))
                .willReturn("pictureUrl");
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.create(user))
                .willReturn(user);
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        MockMultipartFile userPart = new MockMultipartFile("user", "user.json",
                MediaType.APPLICATION_JSON_VALUE, new ObjectMapper()
                .writeValueAsBytes(userCreateRequest));
        mockMvc.perform(multipart(Endpoints.USERS + "/multipart")
                        .file(userPart)
                        .file(picturePart)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", Matchers.endsWith(UriComponentsBuilder.newInstance()
                        .path(Endpoints.USERS).path("/{id}").build(user.getId()).toString())));
        verify(passwordEncoder).encode(user.getPassword().getValue());
        verify(fileService).validateImageExtension(picturePart.getOriginalFilename());
        verify(fileService).upload(picturePart);
        verify(userService).create(user);
    }

    @Test
    void shouldRegisterUserWithUploadPictureLoadAsMultipartDataAndUseEmailVerificationMechanism() throws Exception {
        User user = mockUser.mock();
        MockMultipartFile picturePart = new MockMultipartFile("picture", "picture.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[]{});
        given(verificationProperties.isSendEmail())
                .willReturn(true);
        given(userService.existsByEmail(user.getEmail().getAddress()))
                .willReturn(false);
        given(fileService.upload(picturePart))
                .willReturn("pictureUrl");
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.create(user))
                .willReturn(user);
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName(user.getName());
        userCreateRequest.setFamilyName(user.getFamilyName());
        userCreateRequest.setEmail(user.getEmail().getAddress());
        userCreateRequest.setPassword(user.getPassword().getValue());
        userCreateRequest.setPasswordConfirm(user.getPassword().getValue());
        MockMultipartFile userPart = new MockMultipartFile("user", "user.json",
                MediaType.APPLICATION_JSON_VALUE, new ObjectMapper()
                .writeValueAsBytes(userCreateRequest));
        mockMvc.perform(multipart(Endpoints.USERS + "/multipart")
                        .file(userPart)
                        .file(picturePart)
                        .with(MockPrincipal.anonymous())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", Matchers.endsWith(UriComponentsBuilder.newInstance()
                        .path(Endpoints.USERS).path("/{id}").build(user.getId()).toString())));
        verify(emailVerifier).verify(any(HttpServletRequest.class), any(Email.class));
        verify(passwordEncoder).encode(user.getPassword().getValue());
        verify(fileService).validateImageExtension(picturePart.getOriginalFilename());
        verify(fileService).upload(picturePart);
        verify(userService).create(user);
    }

    @Test
    void shouldEditUserWhenRequestDataValidAndUserExists() throws Exception {
        User user = mockUser.mock();
        UserEditRequest userEditRequest = new UserEditRequest();
        userEditRequest.setName(user.getName());
        userEditRequest.setFamilyName(user.getFamilyName());
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(userService.edit(userEditRequest.getModifiedUser(user)))
                .willReturn(user);
        mockMvc.perform(put(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userEditRequest)))
                .andExpect(status().isNoContent());
        verify(userService).getById(user.getId());
        verify(userService).edit(userEditRequest.getModifiedUser(user));
    }

    @Test
    void shouldSendErrorResponseWhenEditUserWhenRequestDataInvalid() throws Exception {
        User user = mockUser.mock()
                .withName("name has been limited maximum 20 characters");
        UserEditRequest userEditRequest = new UserEditRequest();
        userEditRequest.setName(user.getName());
        userEditRequest.setFamilyName(user.getFamilyName());
        mockMvc.perform(put(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userEditRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorResponseEditUserWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        UserEditRequest userEditRequest = new UserEditRequest();
        userEditRequest.setName(user.getName());
        userEditRequest.setFamilyName(user.getFamilyName());
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(put(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(userEditRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenEditUserWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        mockMvc.perform(put(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldDeleteUserByIdWhenUserByIdExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        mockMvc.perform(delete(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userService).deleteById(user.getId());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteUserWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(delete(Endpoints.USERS + "/{id}", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteUserWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        URI uri = UriComponentsBuilder.fromUriString(Endpoints.USERS).path("/{id}").build(user.getId());
        mockMvc.perform(delete(uri)
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUploadUserPictureLoadAsMultipartData() throws Exception {
        User user = mockUser.mock();
        MockMultipartFile picturePart = new MockMultipartFile("picture", "picture.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[]{});
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(fileService.upload(picturePart))
                .willReturn("pictureUrl");
        given(userService.edit(user))
                .willReturn(user);
        mockMvc.perform(multipart(Endpoints.USERS + "/{id}/picture", user.getId())
                        .file(picturePart)
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNoContent());
        verify(userService).getById(user.getId());
        verify(fileService).validateImageExtension(picturePart.getOriginalFilename());
        verify(fileService).upload(picturePart);
        verify(userService).edit(user);
    }

    @Test
    void shouldSendErrorMessageWhenUploadUserPictureWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        MockMultipartFile picturePart = new MockMultipartFile("picture", "picture.jpeg",
                MediaType.IMAGE_JPEG_VALUE, new byte[]{});
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(multipart(Endpoints.USERS + "/{id}/picture", user.getId())
                        .file(picturePart)
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenUploadUserPictureWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        mockMvc.perform(multipart(Endpoints.USERS + "/{id}/picture", user.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldChangeUserPasswordWhenUserByIdExistsAndPasswordValidAndConfirmed() throws Exception {
        String password = "password";
        String newPassword = "newPassword";
        User user = mockUser.mock()
                .withPassword(new Password(password));
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(passwordEncoder.matches(user.getPassword().getValue(), user.getPassword().getValue()))
                .willReturn(true);
        given(passwordEncoder.encode(user.getPassword().getValue()))
                .willReturn(user.getPassword().getValue());
        given(userService.edit(user))
                .willReturn(user);
        PasswordChangeRequest passwordChangeRequest =
                new PasswordChangeRequest(user.getPassword().getValue(), newPassword, newPassword);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/password", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isNoContent());
        verify(userService).getById(user.getId());
        verify(passwordEncoder).matches(password, password);
        verify(passwordEncoder).encode(newPassword);
        verify(userService).edit(user);
    }

    @Test
    void shouldSendErrorResponseWhenChangeUserPasswordWhenUserByIdNotExists() throws Exception {
        String newPassword = "newPassword";
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        PasswordChangeRequest passwordChangeRequest =
                new PasswordChangeRequest(user.getPassword().getValue(), newPassword, newPassword);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/password", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenChangeUserPasswordWhenPasswordInvalid() throws Exception {
        String password = "password";
        String newPassword = "newPassword";
        User user = mockUser.mock()
                .withPassword(new Password(password));
        given(userService.getById(user.getId()))
                .willReturn(user);
        PasswordChangeRequest passwordChangeRequest =
                new PasswordChangeRequest(user.getPassword().getValue(),
                        "password has been limited maximum 20 characters", newPassword);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/password", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorResponseWhenChangeUserPasswordWhenPasswordConfirmed() throws Exception {
        String password = "password";
        String newPassword = "newPassword";
        User user = mockUser.mock()
                .withPassword(new Password(password));
        given(userService.getById(user.getId()))
                .willReturn(user);
        PasswordChangeRequest passwordChangeRequest =
                new PasswordChangeRequest(user.getPassword().getValue(), newPassword,
                        "notConfirmedPassword");
        mockMvc.perform(put(Endpoints.USERS + "/{id}/password", user.getId())
                        .with(MockPrincipal.authorization(user))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorResponseWhenChangeUserPasswordWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        mockMvc.perform(put(Endpoints.USERS + "/{id}/password", user.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetUserAccountSettingsWhenUserByIdExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        mockMvc.perform(get(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.anonymous()))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(AccountProjection.from(user.getAccount()))));
        verify(userService).getById(user.getId());
    }

    @Test
    void shouldSendErrorResponseWhenGetUserAccountSettingsWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(get(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.anonymous()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEditUserAccountSettingsWhenUserByIdExistsAndRequestParametersValid() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(userService.edit(user))
                .willReturn(user);
        String instant = Instant.now().toString();
        AccountAccessChangeRequest accountAccessChangeRequest =
                new AccountAccessChangeRequest(instant, instant, instant, instant);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(accountAccessChangeRequest)))
                .andExpect(status().isNoContent());
        verify(userService).getById(user.getId());
        verify(userService).edit(user);
    }

    @Test
    void shouldSendErrorMessageWhenEditUserAccountSettingsWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        String instant = Instant.now().toString();
        AccountAccessChangeRequest accountAccessChangeRequest =
                new AccountAccessChangeRequest(instant, instant, instant, instant);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(accountAccessChangeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorMessageWhenEditUserAccountSettingsWhenRequestParametersInvalid() throws Exception {
        User user = mockUser.mock();
        String instant = Instant.now().toString();
        AccountAccessChangeRequest accountAccessChangeRequest =
                new AccountAccessChangeRequest("isn't ISO8601 but required", instant, instant, instant);
        mockMvc.perform(put(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.configuredObjectMapper()
                                .writeValueAsString(accountAccessChangeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSendErrorMessageWhenEditUserAccountSettingsWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        mockMvc.perform(put(Endpoints.USERS + "/{id}/account", user.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetUserAuthoritiesPageByUserIdWhenUserByIdExists() throws Exception {
        User user = mockUser.mock();
        Pageable pageable = PageRequest.of(0, 3).withSort(Sort.by("name").descending());
        Page<UserAuthority> userAuthorities = mockUserAuthority.mockPage(pageable);
        given(userService.getAuthorities(user.getId(), pageable))
                .willReturn(userAuthorities);
        mockMvc.perform(get(Endpoints.USERS + "/{id}/authorities", user.getId())
                        .with(MockPrincipal.anonymous())
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperUtils.configuredObjectMapper()
                        .writeValueAsString(userAuthorities.map(userAuthority ->
                                AuthorityProjection.from(userAuthority.getAuthority())))));
        verify(userService).getAuthorities(user.getId(), pageable);
    }

    @Test
    void shouldAddAuthorityToUserWhenUserByIdExistsAndAuthorityByIdExists() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(authorityService.getById(authority.getId()))
                .willReturn(authority);
        mockMvc.perform(post(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userService).getById(user.getId());
        verify(authorityService).getById(user.getId());
        verify(userService).addAuthority(new UserAuthority(user.getId(), authority));
    }

    @Test
    void shouldSendErrorResponseWhenAddAuthorityToUserWhenUserByIdNotExists() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        given(userService.getById(user.getId()))
                .willThrow(Errors.userNotExistsById(user.getId()));
        mockMvc.perform(post(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenAddAuthorityToUserWhenAuthorityByIdNotExists() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        given(userService.getById(user.getId()))
                .willReturn(user);
        given(authorityService.getById(authority.getId()))
                .willThrow(Errors.authorityNotExistsById(authority.getId()));
        mockMvc.perform(post(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(userService).getById(user.getId());
    }

    @Test
    void shouldSendErrorResponseWhenAddAuthorityToUserWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        mockMvc.perform(post(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenAddAuthorityToUserWhenClientNotEnoughPermissions() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        mockMvc.perform(post(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteUserAuthorityWhenUserByIdExistsAndAuthorityByIdExists() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willReturn(authority);
        mockMvc.perform(delete(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(authorityService).getById(authority.getId());
        verify(userService).deleteAuthority(new UserAuthority(user.getId(), authority));
    }

    @Test
    void shouldSendErrorResponseWhenDeleteUserAuthorityWhenAuthorityByIdNotExists() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        given(authorityService.getById(authority.getId()))
                .willThrow(Errors.authorityNotExistsById(authority.getId()));
        mockMvc.perform(delete(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization(InitialAuthorities.admin()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteUserAuthorityWhenClientUnauthorized() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        mockMvc.perform(delete(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.anonymous())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSendErrorResponseWhenDeleteUserAuthorityWhenClientNotEnoughPermissions() throws Exception {
        User user = mockUser.mock();
        Authority authority = mockAuthority.mock();
        mockMvc.perform(delete(Endpoints.USERS + "/{id}/authorities/{authorityId}", user.getId(), authority.getId())
                        .with(MockPrincipal.authorization())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
