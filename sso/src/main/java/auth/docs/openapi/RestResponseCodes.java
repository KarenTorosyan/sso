package auth.docs.openapi;

public class RestResponseCodes {

    public static final String NOT_FOUND = "404";
    public static final String NOT_FOUND_DESCRIPTION = "Entity not exists!";

    public static final String BAD_REQUEST = "400";
    public static final String BAD_REQUEST_DESCRIPTION = "Request data invalid!";

    public static final String CONFLICT = "409";
    public static final String CONFLICT_DESCRIPTION = "Entity already exists!";

    public static final String NO_CONTENT = "204";
    public static final String NO_CONTENT_DESCRIPTION = "Success without response body!";

    public static final String OK = "200";
    public static final String OK_DESCRIPTION = "Success with response body!";

    public static final String CREATED = "201";
    public static final String CREATED_DESCRIPTION = "Success without response body!";

    public static final String UNAUTHORIZED = "401";
    public static final String UNAUTHORIZED_DESCRIPTION = "Unauthorized!";

    public static final String FORBIDDEN = "403";
    public static final String FORBIDDEN_DESCRIPTION = "Forbidden!";
}
