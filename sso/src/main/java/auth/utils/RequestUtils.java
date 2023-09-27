package auth.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL()
                .indexOf( "/", 7));
    }
}
