package auth.configs.security;

import auth.errors.ErrorResponse;
import auth.errors.Errors;
import auth.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ErrorResponseUnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = ObjectMapperUtils.configuredObjectMapper();

    private final Map<String, String> redirectionEndpoints = new LinkedHashMap<>(0);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {

        if (!redirectionEndpoints.isEmpty() && redirectionEndpoints.containsKey(request.getRequestURI())) {
            try {
                response.sendRedirect(redirectionEndpoints.get(request.getRequestURI()));
                log.error("Client unauthorized for {} redirected to {} exception {}", request.getRequestURI(),
                        redirectionEndpoints.get(request.getRequestURI()), authException.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            try {
                String message = objectMapper.writeValueAsString(new ErrorResponse(Errors.unauthorized().getMessage()));
                response.getWriter().write(message);
                log.error("Client unauthorized for {} send response {}", request.getRequestURI(), message);
            } catch (IOException e) {
                throw Errors.responseContentWriting(e);
            }
        }
    }

    public ErrorResponseUnauthorizedAuthenticationEntryPoint redirect(String endpoint, String redirectTo) {
        redirectionEndpoints.put(endpoint, redirectTo);
        return this;
    }
}