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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ErrorResponseAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = ObjectMapperUtils.configuredObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            String message = objectMapper.writeValueAsString(new ErrorResponse(Errors.forbidden().getMessage()));
            response.getWriter().write(message);
            log.error("Client haven't enough access for {} send response {}", request.getRequestURI(), message);
        } catch (IOException e) {
            throw Errors.responseContentWriting(e);
        }
    }
}
