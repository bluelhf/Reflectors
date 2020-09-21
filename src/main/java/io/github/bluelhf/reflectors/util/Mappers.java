package io.github.bluelhf.reflectors.util;

import io.github.bluelhf.reflectors.Reflectors;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Mappers {
    public static <T, R> Function<T, R> castTo(Class<R> type) {
        return type::cast;
    }
    public static <T, @Nullable Object> Function<T, @Nullable Object> castTo(String classpath) {
        //noinspection unchecked
        return obj -> (Object) Reflectors.refer(classpath).cast(obj);
    }

}
