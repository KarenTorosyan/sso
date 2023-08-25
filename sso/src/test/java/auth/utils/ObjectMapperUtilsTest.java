package auth.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ObjectMapperUtilsTest {

    private static final String TEXT = "content";
    private static final Instant DATETIME = Instant.MIN;

    record SerializeMe(@JsonProperty("text") String text,
                       @JsonProperty("datetime") Instant datetime) {
    }

    @Test
    void shouldConfigureObjectMapperForApplication() throws JsonProcessingException {
        String json = ObjectMapperUtils.configuredObjectMapper()
                .writeValueAsString(new SerializeMe(TEXT, DATETIME));
        assertThat(json).isEqualTo(String.format(
                "{\"text\":\"%s\",\"datetime\":\"%s\"}", TEXT, DATETIME));
    }
}
