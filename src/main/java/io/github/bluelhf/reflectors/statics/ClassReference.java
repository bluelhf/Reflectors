package io.github.bluelhf.reflectors.statics;

import io.github.bluelhf.reflectors.Reflectors;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassReference {
    Class<?> inner;

    public ClassReference(Class<?> clazz) {
        this.inner = clazz;
    }

    public ClassReference(String classpath) {
        try {
            this.inner = Class.forName(classpath);
        } catch (ClassNotFoundException e) {
            Reflectors.getLogger().warning("Could not find class " + classpath);
            this.inner = null;
        }
    }

    @Nullable
    public Object cast(Object target) {
        try {
            return inner.cast(target);
        } catch (ClassCastException e) {
            Reflectors.getLogger().warning("Could not cast " + target + " to class " + inner.getCanonicalName());
            return null;
        }
    }

    public Optional<? extends BaseFieldReference<Object>> field(String name) {
        Optional<? extends BaseFieldReference<Object>> declared = getDeclaredField(name);
        if (declared.isPresent()) return declared;
        return getPublicField(name);
    }

    public Collection<? extends BaseFieldReference<Object>> getDeclaredFields() {
        return Arrays.stream(inner.getDeclaredFields()).map(BaseFieldReference::new).collect(Collectors.toList());
    }

    public Optional<? extends BaseFieldReference<Object>> getDeclaredField(String name) {
        try {
            return Optional.of(new BaseFieldReference<>(inner.getDeclaredField(name)));
        } catch (NoSuchFieldException e) {
            Reflectors.getLogger().warning("Could not find declared field " + inner.getCanonicalName() + "#" + name);
            return Optional.empty();
        }
    }

    public Collection<? extends BaseFieldReference<Object>> getPublicFields() {
        return Arrays.stream(inner.getFields()).map(BaseFieldReference::new).collect(Collectors.toList());
    }

    public Optional<? extends BaseFieldReference<Object>> getPublicField(String name) {
        try {
            return Optional.of(new BaseFieldReference<>(inner.getField(name)));
        } catch (NoSuchFieldException e) {
            Reflectors.getLogger().warning("Could not find public field " + inner.getCanonicalName() + "#" + name);
            return Optional.empty();
        }
    }

    public Optional<? extends BaseMethodReference<Object>> method(String name, Class<?>... parameterTypes) {
        Optional<? extends BaseMethodReference<Object>> declared = getDeclaredMethod(name, parameterTypes);
        if (declared.isPresent()) return declared;
        return getPublicMethod(name, parameterTypes);
    }

    public Collection<? extends BaseMethodReference<Object>> getDeclaredMethods() {
        return Arrays.stream(inner.getDeclaredMethods()).map(BaseMethodReference::new).collect(Collectors.toList());
    }

    public Optional<? extends BaseMethodReference<Object>> getDeclaredMethod(String name, Class<?>... parameterTypes) {
        try {
            return Optional.of(new BaseMethodReference<>(inner.getDeclaredMethod(name, parameterTypes)));
        } catch (NoSuchMethodException e) {
            Reflectors.getLogger().warning("Could not find declared method " + inner.getCanonicalName() + "." + name + "(" + Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            return Optional.empty();
        }
    }

    public Collection<? extends BaseMethodReference<Object>> getPublicMethods() {
        return Arrays.stream(inner.getMethods()).map(BaseMethodReference::new).collect(Collectors.toList());
    }

    public Optional<? extends BaseMethodReference<Object>> getPublicMethod(String name, Class<?>... parameterTypes) {
        try {
            return Optional.of(new BaseMethodReference<>(inner.getMethod(name, parameterTypes)));
        } catch (NoSuchMethodException e) {
            Reflectors.getLogger().warning("Could not find public method " + inner.getCanonicalName() + "." + name + "(" + Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            return Optional.empty();
        }
    }

    public Class<?> getInner() {
        return inner;
    }

    public Collection<? extends BaseMethodReference<Object>> getAllMethods() {
        Collection<? extends BaseMethodReference<Object>> publicMethods = getPublicMethods();
        List<BaseMethodReference<Object>> full = getDeclaredMethods().stream().filter(m -> !publicMethods.contains(m)).collect(Collectors.toList());
        full.addAll(publicMethods);
        return full;
    }
}
