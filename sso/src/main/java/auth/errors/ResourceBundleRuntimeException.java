package auth.errors;

import lombok.Getter;

@Getter
public class ResourceBundleRuntimeException extends RuntimeException {

    private String code;
    private Object[] arguments;

    public ResourceBundleRuntimeException() {
    }

    public ResourceBundleRuntimeException(String message, String code, Object... arguments) {
        super(message);
        this.code = code;
        this.arguments = arguments;
    }

    public ResourceBundleRuntimeException(String message, Throwable cause, String code, Object... arguments) {
        super(message, cause);
        this.code = code;
        this.arguments = arguments;
    }

    public ResourceBundleRuntimeException(Throwable cause, String code, Object... arguments) {
        super(cause);
        this.code = code;
        this.arguments = arguments;
    }

    public ResourceBundleRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Object... arguments) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.arguments = arguments;
    }
}
