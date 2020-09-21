package io.github.bluelhf.reflectors.statics;

import io.github.bluelhf.reflectors.Result;
import io.github.bluelhf.reflectors.Reflectors;
import io.github.bluelhf.reflectors.dynamics.InstanceMethodReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class BaseMethodReference<T> {
    private final Method inner;
    public BaseMethodReference(Method method) {
        this.inner = method;
    }

    public Result<T> invoke(Object... params) {
        if (!Modifier.isStatic(inner.getModifiers())) {
            Reflectors.getLogger().warning("Could not access " + inner.toString() + " because target was not given.");
            return new Result<>(new IllegalAccessException("Cannot access non-static method " + inner.toString() + " without target."));
        }
        return invokeOnUnsafe(null, params);
    }

    public Result<T> invokeOn(@NotNull Object target, Object... params) {
        return invokeOnUnsafe(target, params);
    }

    @Nullable
    public InstanceMethodReference<Object> getAsInstanceMethod(Object target) {
        if (Modifier.isStatic(inner.getModifiers())) {
            Reflectors.getLogger().warning("Could not convert " + this.getDeclaration() + " to instance method reference because it is static.");
            return null;
        }
        return new InstanceMethodReference<>(target, inner);
    }


    @SuppressWarnings("SameParameterValue")
    protected Result<T> invokeOnUnsafe(@Nullable Object target, Object... params) {
        boolean wasAccessible;
        try {
            wasAccessible = inner.canAccess(Modifier.isStatic(inner.getModifiers()) ? null : this);
        } catch (Throwable e) {
            return new Result<>(e);
        }

        try {
            inner.setAccessible(true);
            //noinspection unchecked - Just throw ClassCastException
            Result<T> result = new Result<>((T) inner.invoke(target, params));
            inner.setAccessible(wasAccessible);
            return result;
        } catch (Throwable e) {
            Reflectors.getLogger().warning("Got " + e.getClass().getSimpleName() + " when invoking " + getDeclaration() + " on " + target + " with args " + Arrays.toString(params) + ": ");
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEachOrdered(s -> Reflectors.getLogger().warning("  " + s));
            inner.setAccessible(wasAccessible);
            return new Result<>(e);
        }
    }

    public String getDeclaration() {
        return Modifier.toString(inner.getModifiers()) + " " + inner.getReturnType().getSimpleName() + " " + inner.getName() + "(" + Arrays.stream(inner.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")";
    }

    public Method getInner() {
        return inner;
    }
}
