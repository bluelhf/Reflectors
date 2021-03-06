package io.github.bluelhf.reflectors.dynamics;

import io.github.bluelhf.reflectors.Reflectors;
import io.github.bluelhf.reflectors.statics.ClassReference;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceReference extends ClassReference {
    Object instance;
    public InstanceReference(Object instance) {
        super(instance.getClass());
        this.instance = instance;
    }
    public InstanceReference(Object instance, Class<?> clazz) {
        super(clazz);
        this.instance = instance;
    }

    public InstanceReference(Object instance, String classpath) {
        super(classpath);
        this.instance = instance;
    }

    @Nullable
    public Object cast() {
        try {
            return getInner().cast(instance);
        } catch (ClassCastException e) {
            Reflectors.getLogger().warning("Could not cast " + instance + " to class " + getInner().getCanonicalName());
            return null;
        }
    }

    public Optional<InstanceFieldReference<Object>> field(String name) {
        Optional<InstanceFieldReference<Object>> declared = getDeclaredField(name);
        if (declared.isPresent()) return declared;
        return getPublicField(name);
    }

    public Collection<InstanceFieldReference<Object>> getDeclaredFields() {
        return Arrays.stream(getInner().getDeclaredFields()).map(f -> new InstanceFieldReference<>(instance, f)).collect(Collectors.toList());
    }

    public Optional<InstanceFieldReference<Object>> getDeclaredField(String name) {
        try {
            return Optional.of(new InstanceFieldReference<>(instance, getInner().getDeclaredField(name)));
        } catch (NoSuchFieldException e) {
            Reflectors.getLogger().warning("Could not find declared field " + getInner().getCanonicalName() + "#" + name);
            return Optional.empty();
        }
    }

    public Collection<InstanceFieldReference<Object>> getPublicFields() {
        return Arrays.stream(getInner().getFields()).map(f -> new InstanceFieldReference<>(instance, f)).collect(Collectors.toList());
    }

    public Optional<InstanceFieldReference<Object>> getPublicField(String name) {
        try {
            return Optional.of(new InstanceFieldReference<>(instance, getInner().getField(name)));
        } catch (NoSuchFieldException e) {
            Reflectors.getLogger().warning("Could not find public field " + getInner().getCanonicalName() + "#" + name);
            return Optional.empty();
        }
    }

    public Optional<InstanceMethodReference<Object>> method(String name, Class<?>... parameterTypes) {
        Optional<InstanceMethodReference<Object>> declared = getDeclaredMethod(name, parameterTypes);
        if (declared.isPresent()) return declared;
        return getPublicMethod(name, parameterTypes);
    }

    public Collection<InstanceMethodReference<Object>> getDeclaredMethods() {
        return Arrays.stream(getInner().getDeclaredMethods()).map((m) -> new InstanceMethodReference<>(instance, m)).collect(Collectors.toList());
    }

    public Optional<InstanceMethodReference<Object>> getDeclaredMethod(String name, Class<?>... parameterTypes) {
        try {
            return Optional.of(new InstanceMethodReference<>(instance, getInner().getDeclaredMethod(name, parameterTypes)));
        } catch (NoSuchMethodException e) {
            Reflectors.getLogger().warning("Could not find declared method " + getInner().getCanonicalName() + "." + name + "(" + Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            return Optional.empty();
        }
    }

    public Collection<InstanceMethodReference<Object>> getPublicMethods() {
        return Arrays.stream(getInner().getMethods()).map((m) -> new InstanceMethodReference<>(instance, m)).collect(Collectors.toList());
    }

    public Optional<InstanceMethodReference<Object>> getPublicMethod(String name, Class<?>... parameterTypes) {
        try {
            return Optional.of(new InstanceMethodReference<>(instance, getInner().getMethod(name, parameterTypes)));
        } catch (NoSuchMethodException e) {
            Reflectors.getLogger().warning("Could not find public method " + getInner().getCanonicalName() + "." + name + "(" + Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            return Optional.empty();
        }
    }

}
