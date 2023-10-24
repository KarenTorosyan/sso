package auth.entities.user.in.web;

import auth.Endpoints;
import auth.docs.openapi.NotDocumentedSchema;
import auth.docs.openapi.RestDocumentedPostMappingRequireAuthorization;
import auth.entities.user.EmailVerifier;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.utils.RequestUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(Endpoints.EMAIL_VERIFY)
@RequiredArgsConstructor
@Tag(name = "Verification")
public class SentEmailVerificationController {

    private final UserService userService;

    private final EmailVerifier emailVerifier;

    @PostMapping
    @RestDocumentedPostMappingRequireAuthorization
    ResponseEntity<Void> sentEmail(@RequestParam String email,
                                   @NotDocumentedSchema HttpServletRequest request) {

        User user = userService.getByEmail(email);

        if (user.getEmail().isVerified()) {
            throw Errors.emailAlreadyVerified(user.getEmail().getAddress());
        }

        user.setEmail(user.getEmail()
                .withVerificationCode(UUID.randomUUID().toString())
                .withVerificationCodeExpiresIn(null));

        userService.edit(user);

        CompletableFuture.runAsync(() ->
                emailVerifier.verify(RequestUtils.getRequestUrl(request), user.getEmail()));

        return ResponseEntity.noContent().build();
    }
}
