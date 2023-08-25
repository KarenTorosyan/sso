package auth.errors;

public class AlreadyExistsException extends ResourceBundleRuntimeException {

    public AlreadyExistsException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
