package auth.errors;

public class RestrictionException extends ResourceBundleRuntimeException {

    public RestrictionException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
