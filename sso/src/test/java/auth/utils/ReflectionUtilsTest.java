package auth.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReflectionUtilsTest {

    private static final String TEXT = "content";

    record ReflectMe(String text) {
    }

    @Test
    void shouldGetDeclaredFieldByName() {
        ReflectMe reflectMe = new ReflectMe(TEXT);
        Field field = ReflectionUtils.getDeclaredField(reflectMe, "text");
        assertThat(field.canAccess(reflectMe)).isTrue();
    }

    @Test
    void shouldThrowErrorWhenObjectDoesNotFieldBySpecifiedName() {
        ReflectMe reflectMe = new ReflectMe(TEXT);
        assertThatThrownBy(() -> ReflectionUtils.getDeclaredField(reflectMe, "nonExistenceField"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldGetDeclaredFieldValueByNameWhenFieldIsAccessible() {
        ReflectMe reflectMe = new ReflectMe(TEXT);
        Object value = ReflectionUtils.getDeclaredFieldValue(reflectMe, "text");
        assertThat(value).isEqualTo(TEXT);
    }
}
