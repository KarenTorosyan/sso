package auth.entities.user.in.web;

import auth.Endpoints;
import auth.configs.verification.VerificationProperties;
import auth.docs.openapi.*;
import auth.entities.authority.Authority;
import auth.entities.authority.AuthorityService;
import auth.entities.authority.in.web.AuthorityProjection;
import auth.entities.picture.Picture;
import auth.entities.user.*;
import auth.errors.Errors;
import auth.intergrations.filesystem.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(Endpoints.USERS)
@Tag(name = "User")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerifier emailVerifier;
    private final FileService fileService;
    private final AuthorityService authorityService;

    private final VerificationProperties verificationProperties;

    @GetMapping
    @RestDocumentedGetMappingResponsePage
    ResponseEntity<Page<UserProjection>> get(@NotDocumentedSchema Pageable pageable,
                                             @RequestParam(required = false) String term,
                                             @RequestParam(required = false) Set<String> id,
                                             @RequestParam(required = false) Set<String> email) {
        Page<User> usersPage = Optional.ofNullable(term)
                .map(param -> userService.search(param, pageable))
                .orElse(Optional.ofNullable(id)
                        .map(ids -> userService.getAllById(ids, pageable))
                        .orElse(Optional.ofNullable(email)
                                .map(emails -> userService.getAllByEmail(emails, pageable))
                                .orElse(userService.getAll(pageable))));
        return ResponseEntity.ok(usersPage.map(UserProjection::from));
    }

    @GetMapping("/{id}")
    @RestDocumentedGetMapping
    ResponseEntity<UserProjection> getById(@PathVariable String id) {
        return ResponseEntity.ok(UserProjection.from(userService.getById(id)));
    }

    @GetMapping("/email/{email}")
    @RestDocumentedGetMapping
    ResponseEntity<UserProjection> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserProjection.from(userService.getByEmail(email)));
    }

    @PostMapping
    @RestDocumentedPostMapping
    ResponseEntity<Void> register(@RequestBody @Validated UserCreateRequest userCreateRequest,
                                  @NotDocumentedSchema HttpServletRequest request) {
        User user = userCreateRequest.getUser();
        emailVerification(request, user);
        user.withPassword(new Password(passwordEncoder.encode(user.getPassword().getValue())));
        User createdUser = userService.create(user);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").build(createdUser.getId())).build();
    }

    private void emailVerification(HttpServletRequest request, User user) {
        if (verificationProperties.isSendEmail() &&
                !userService.existsByEmail(user.getEmail().getAddress())) {
            emailVerifier.verify(request, user.getEmail());
        }
    }

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RestDocumentedPostMapping
    ResponseEntity<Void> register(@RequestPart(name = "user") @Validated UserCreateRequest userCreateRequest,
                                  @RequestPart(name = "picture", required = false) MultipartFile multipartFile,
                                  @NotDocumentedSchema HttpServletRequest request) {
        User user = userCreateRequest.getUser();
        emailVerification(request, user);
        user.withPassword(new Password(passwordEncoder.encode(user.getPassword().getValue())));
        if (multipartFile != null) {
            fileService.validateImageExtension(multipartFile.getOriginalFilename());
            String pictureUrl = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .replacePath(Endpoints.UPLOADS)
                    .path("/" + fileService.upload(multipartFile)).toUriString();
            user.withPicture(new Picture(pictureUrl));
        }
        User createdUser = userService.create(user);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath(Endpoints.USERS).path("/{id}").build(createdUser.getId())).build();
    }

    @PutMapping("/{id}")
    @RestDocumentedPutMappingRequireAuthentication
    ResponseEntity<Void> edit(@PathVariable String id,
                              @RequestBody @Validated UserEditRequest userEditRequest) {
        User user = userService.getById(id);
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!authentication.getName().equals(user.getEmail().getAddress())) {
            throw Errors.noEnoughAccess(authentication.getName());
        }
        userService.edit(userEditRequest.getModifiedUser(user));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @RestDocumentedDeleteMappingRequireAuthentication
    ResponseEntity<Void> delete(@PathVariable String id) {
        User user = userService.getById(id);
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!authentication.getName().equals(user.getEmail().getAddress())) {
            throw Errors.noEnoughAccess(authentication.getName());
        }
        userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RestDocumentedPutMappingRequireAuthentication
    ResponseEntity<Void> changePicture(@PathVariable String id,
                                       @RequestPart(name = "picture") MultipartFile multipartFile) {
        User user = userService.getById(id);
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!authentication.getName().equals(user.getEmail().getAddress())) {
            throw Errors.noEnoughAccess(authentication.getName());
        }
        fileService.validateImageExtension(multipartFile.getOriginalFilename());
        String pictureUrl = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath(Endpoints.UPLOADS)
                .path("/" + fileService.upload(multipartFile)).toUriString();
        userService.edit(user.withPicture(new Picture(pictureUrl)));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    @RestDocumentedPutMappingRequireAuthentication
    ResponseEntity<Void> changePassword(@PathVariable String id,
                                        @RequestBody @Validated PasswordChangeRequest request) {
        User user = userService.getById(id);
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!authentication.getName().equals(user.getEmail().getAddress())) {
            throw Errors.noEnoughAccess(authentication.getName());
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword().getValue())) {
            throw Errors.passwordInvalid();
        }
        userService.edit(user.withPassword(new Password(passwordEncoder.encode(request.getNewPassword()))));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/account")
    @RestDocumentedGetMapping
    ResponseEntity<AccountProjection> getAccount(@PathVariable String id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(AccountProjection.from(user.getAccount()));
    }

    @PutMapping("/{id}/account")
    @RestDocumentedPutMappingRequireAuthorization
    ResponseEntity<Void> editAccount(@PathVariable String id,
                                     @RequestBody @Validated AccountAccessChangeRequest request) {
        User user = userService.getById(id);
        userService.edit(user.withAccount(request.getAccount()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/authorities")
    @RestDocumentedGetMappingResponsePage
    ResponseEntity<Page<AuthorityProjection>> getAuthorities(@PathVariable String id,
                                                             @NotDocumentedSchema Pageable pageable) {
        return ResponseEntity.ok(userService.getAuthorities(id, pageable)
                .map(userAuthority -> AuthorityProjection.from(userAuthority.getAuthority())));
    }

    @PostMapping("/{id}/authorities/{authorityId}")
    @RestDocumentedPostMappingRequireAuthorization
    ResponseEntity<Void> addAuthority(@PathVariable String id, @PathVariable String authorityId) {
        User user = userService.getById(id);
        Authority authority = authorityService.getById(authorityId);
        userService.addAuthority(new UserAuthority(user.getId(), authority));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/authorities/{authorityId}")
    @RestDocumentedDeleteMappingRequireAuthorization
    ResponseEntity<Void> deleteAuthority(@PathVariable String id, @PathVariable String authorityId) {
        Authority authority = authorityService.getById(authorityId);
        userService.deleteAuthority(new UserAuthority(id, authority));
        return ResponseEntity.noContent().build();
    }
}
