package auth.errors;

import auth.utils.MessageSourceUtils;
import org.springframework.context.MessageSource;

import java.time.Instant;

public class LocalizedErrors {

    private final MessageSource messageSource;

    public LocalizedErrors(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String localize(ResourceBundleRuntimeException e) {
        return MessageSourceUtils.localize(messageSource, e);
    }

    public NotExistsException userNotExists(String username) {
        NotExistsException e = Errors.userNotExistsByEmail(username);
        return new NotExistsException(localize(e), e.getCode(), e.getArguments());
    }

    public RestrictionException accountLocked(String username, Instant lockedIn) {
        RestrictionException e = Errors.accountLocked(username, lockedIn);
        return new RestrictionException(localize(e), e.getCode(), e.getArguments());
    }

    public RestrictionException accountExpired(String username, Instant expiredIn) {
        RestrictionException e = Errors.accountExpired(username, expiredIn);
        return new RestrictionException(localize(e), e.getCode(), e.getArguments());
    }

    public RestrictionException userDisabled(String username, Instant disabledIn) {
        RestrictionException e = Errors.userDisabled(username, disabledIn);
        return new RestrictionException(localize(e), e.getCode(), e.getArguments());
    }

    public RestrictionException credentialsExpired(String username, Instant expiredIn) {
        RestrictionException e = Errors.credentialsExpired(username, expiredIn);
        return new RestrictionException(localize(e), e.getCode(), e.getArguments());
    }
}
