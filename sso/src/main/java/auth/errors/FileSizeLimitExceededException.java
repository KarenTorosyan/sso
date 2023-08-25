package auth.errors;

public class FileSizeLimitExceededException extends ResourceBundleRuntimeException {

    public FileSizeLimitExceededException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
