package auth.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Field getDeclaredField(Object o, String name) {
        try {
            Field declaredField = o.getClass().getDeclaredField(name);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Class '" + o.getClass().getName() +
                    "' has no field '" + name + "'");
        }
    }

    @SneakyThrows
    public static Object getDeclaredFieldValue(Object o, String name) {
        return getDeclaredField(o, name).get(o);
    }
}
