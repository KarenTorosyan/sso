package auth.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestUtilsTest {

    @Test
    void shouldGetRequestUrlFromHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/uri");
        assertThat(RequestUtils.getRequestUrl(request))
                .isEqualTo("http://localhost");
    }
}
