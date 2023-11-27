package auth.errors;

import auth.utils.MessageSourceUtils;
import jakarta.mail.internet.AddressException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorsHandlerRest {

    private final MessageSource messageSource;

    private final MultipartProperties multipartProperties;

    public String localize(ResourceBundleRuntimeException e) {
        return MessageSourceUtils.localize(messageSource, e);
    }

    @ExceptionHandler(AccessException.class)
    ResponseEntity<ErrorResponse> handle(AccessException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(UsernameNotFoundException e) {
        NotExistsException notExistsException = Errors.userNotExistsByEmail(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(localize(notExistsException)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotExistsException.class)
    ResponseEntity<ErrorResponse> handle(NotExistsException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    ResponseEntity<ErrorResponse> handle(AlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConfirmationException.class)
    ResponseEntity<ErrorResponse> handle(ConfirmationException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidationException.class)
    ResponseEntity<ErrorResponse> handle(InvalidationException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        Stream<Map<String, String>> reason = e.getFieldErrors().stream()
                .map(f -> Map.of(f.getField(), Objects.requireNonNull(f.getDefaultMessage())));
        return new ResponseEntity<>(new ErrorResponse(reason), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
        Stream<Map<String, String>> reason = e.getConstraintViolations().stream()
                .map(c -> Map.of(c.getPropertyPath().toString(), c.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(reason), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ErrorResponse> handleSizeLimitExceededException() {
        int megabytes = Math.toIntExact(multipartProperties.getMaxFileSize().toMegabytes());
        FileSizeLimitExceededException e = Errors.fileSizeLimitExceeded(megabytes);
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(FileNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileExtensionException.class)
    ResponseEntity<ErrorResponse> handle(FileExtensionException e) {
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailSendException.class)
    ResponseEntity<ErrorResponse> handleMailSendException() {
        InvalidationException e = Errors.emailAddressInvalid();
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressException.class)
    ResponseEntity<ErrorResponse> handleMailAddressException() {
        InvalidationException e = Errors.emailAddressInvalid();
        return new ResponseEntity<>(new ErrorResponse(localize(e)), HttpStatus.BAD_REQUEST);
    }
}
