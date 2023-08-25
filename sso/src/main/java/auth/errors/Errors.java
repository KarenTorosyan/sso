package auth.errors;

import java.time.Instant;
import java.util.Set;

public class Errors {

    public static RuntimeException unauthorized() {
        return new RuntimeException("Unauthorized!");
    }

    public static RuntimeException forbidden() {
        return new RuntimeException("Forbidden!");
    }

    public static NotExistsException userNotExistsById(String id) {
        return new NotExistsException("The user by id '" + id + "' not exists!", "user_not_exists_by_id", id);
    }

    public static NotExistsException userNotExistsByEmail(String email) {
        return new NotExistsException("The user '" + email + "' not exists!", "user_not_exists_by_email", email);
    }

    public static AlreadyExistsException userAlreadyExistsById(String id) {
        return new AlreadyExistsException("The user by id '" + id + "' already exists!", "user_already_exists_by_id", id);
    }

    public static AlreadyExistsException userAlreadyExistsByEmail(String email) {
        return new AlreadyExistsException("The user '" + email + "' already exists!", "user_already_exists_by_email", email);
    }

    public static RestrictionException accountLocked(String email, Instant lockedIn) {
        return new RestrictionException("Your account locked in " + lockedIn, "account_locked", lockedIn, email);
    }

    public static RestrictionException accountExpired(String email, Instant expiredIn) {
        return new RestrictionException("Your account expired in " + expiredIn, "account_expired", expiredIn, email);
    }

    public static RestrictionException userDisabled(String email, Instant disabledIn) {
        return new RestrictionException("You are disabled in " + disabledIn, "user_disabled", disabledIn, email);
    }

    public static RestrictionException credentialsExpired(String email, Instant expiresIn) {
        return new RestrictionException("Your credentials expired in " + expiresIn, "credentials_expired", expiresIn, email);
    }

    public static NotExistsException authorityNotExistsById(String id) {
        return new NotExistsException("The authority by id '" + id + "' not exists!", "authority_not_exists_by_id", id);
    }

    public static NotExistsException authorityNotExistsByName(String name) {
        return new NotExistsException("The authority '" + name + "' not exists!", "authority_not_exists_by_name", name);
    }

    public static AlreadyExistsException authorityAlreadyExistsById(String id) {
        return new AlreadyExistsException("The authority by id '" + id + "' already exists!", "authority_already_exists_by_id", id);
    }

    public static AlreadyExistsException authorityAlreadyExistsByName(String name) {
        return new AlreadyExistsException("The authority '" + name + "' already exists!", "authority_already_exists_by_name", name);
    }

    public static NotExistsException userHasNotAuthority(String email, String authority) {
        return new NotExistsException("You haven't authority '" + authority + "'", "user_has_not_authority", authority, email);
    }

    public static AlreadyExistsException userAlreadyHasAuthority(String userId, String authority) {
        return new AlreadyExistsException("You already have authority '" + authority + "'", "user_already_has_authority", authority, userId);
    }

    public static RuntimeException cannotReceiveMongoDocumentId(String idAttribute) {
        return new RuntimeException("Cannot receive mongo document " + idAttribute);
    }

    public static ConfirmationException passwordNotConfirmed() {
        return new ConfirmationException("The password not confirmed!", "password_not_confirmed");
    }

    public static InvalidationException passwordInvalid() {
        return new InvalidationException("The password invalid!", "password_invalid");
    }

    public static RuntimeException responseContentWriting(Exception e) {
        return new RuntimeException("Error on write response content!", e);
    }

    public static FileSizeLimitExceededException fileSizeLimitExceeded(int megabytes) {
        return new FileSizeLimitExceededException("The file size limit " + megabytes + "MB exceeded!", "file_size_limit_exceeded_error", megabytes);
    }

    public static InvalidationException emailVerificationCodeInvalid(String code) {
        return new InvalidationException("The email verification code invalid!", "invalid_email_verification_code", code);
    }

    public static RuntimeException fileTransfer(Exception e, String destination) {
        return new RuntimeException("Error on transfer file to destination " + destination, e);
    }

    public static RuntimeException directoryCreation(Exception e, String directory) {
        return new RuntimeException("Error on create directory " + directory, e);
    }

    public static RuntimeException mailSenderRequired() {
        return new RuntimeException("The mail sender required!");
    }

    public static FileNotFoundException fileNotFound(String url) {
        return new FileNotFoundException("The file not found in url '" + url + "'", "file_not_found", url);
    }

    public static FileExtensionException fileExtensionNotSupported(Set<String> supportedOnly) {
        return new FileExtensionException("Supported file extensions " + supportedOnly, "file_extension_unsupported", supportedOnly);
    }

    public static InvalidationException emailAddressInvalid() {
        return new InvalidationException("The email invalid!", "user_email_invalid");
    }

    public static NotExistsException sessionNotFound(String sessionId) {
        return new NotExistsException("Session not found by session id '" + sessionId + "'", "session_not_found", sessionId);
    }
}
