package io.github.bluelhf.reflectors.statics;

import io.github.bluelhf.reflectors.Reflectors;
import io.github.bluelhf.reflectors.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class BaseFieldReference<T> {
    private final Field inner;

    public BaseFieldReference(Field field) {
        this.inner = field;
    }

    public Result<T> get() {
        if (!Modifier.isStatic(inner.getModifiers())) {
            Reflectors.getLogger().warning("Could not access " + inner.toString() + " because target was not given.");
            return new Result<>(new IllegalAccessException("Cannot access non-static field " + inner.toString() + " without target."));
        }
        return getUnsafe(null);
    }
    public Result<T> get(@NotNull Object target) {
        return getUnsafe(target);
    }

    public void set(T value) {
        if (!Modifier.isStatic(inner.getModifiers())) {
            Reflectors.getLogger().warning("Could not access " + inner.toString() + " because target was not given.");
            return;
        }
        setUnsafe(null, value);
    }

    public void set(@NotNull Object target, T value) {
        setUnsafe(target, value);
    }

    protected void setUnsafe(@Nullable Object target, T value) {
        boolean wasAccessible = inner.canAccess(Modifier.isStatic(inner.getModifiers()) ? null : this);
        try {
            inner.setAccessible(true);
            inner.set(target, value);
            inner.setAccessible(wasAccessible);
        } catch (Throwable e) {
            Reflectors.getLogger().warning("Got " + e.getClass().getSimpleName() + " when setting " + inner.getDeclaringClass().getCanonicalName() + "#" + inner.getName() + " of " + target + " to " + value);
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEachOrdered(s -> Reflectors.getLogger().warning("  " + s));
            inner.setAccessible(wasAccessible);
        }
    }

    protected Result<T> getUnsafe(@Nullable Object target) {
        boolean wasAccessible = inner.canAccess(Modifier.isStatic(inner.getModifiers()) ? null : this);
        try {
            inner.setAccessible(true);
            //noinspection unchecked - Just throw ClassCastException
            Result<T> result = new Result<>((T) inner.get(target));
            inner.setAccessible(wasAccessible);
            return result;
        } catch (Throwable e) {
            Reflectors.getLogger().warning("Got " + e.getClass().getSimpleName() + " when getting " + inner.getDeclaringClass().getCanonicalName() + "#" + inner.getName() + " from " + target);
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEachOrdered(s -> Reflectors.getLogger().warning("  " + s));
            inner.setAccessible(wasAccessible);
            return new Result<>(e);
        }
    }

    public Field getInner() {
        return inner;
    }
}
