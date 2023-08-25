package auth.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@Getter
@ToString
public class ErrorResponse {

    @JsonProperty("reason")
    private final Object reason;

    @JsonProperty("uri")
    private final String uri;

    @JsonProperty("datetime")
    private final Instant datetime;

    public ErrorResponse(Object reason, String uri, Instant datetime) {
        this.reason = reason;
        this.uri = uri;
        this.datetime = datetime;
    }

    public ErrorResponse(Object reason, String uri) {
        this(reason, uri, Instant.now());
    }

    public ErrorResponse(Object reason) {
        this(reason, ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString(), Instant.now());
    }
}
