package io.github.bluelhf.reflectors.dynamics;

import io.github.bluelhf.reflectors.Result;
import io.github.bluelhf.reflectors.statics.BaseFieldReference;

import java.lang.reflect.Field;

public class InstanceFieldReference<T> extends BaseFieldReference<T> {
    Object target;
    public InstanceFieldReference(Object target, Field field) {
        super(field);
        this.target = target;
    }

    @Override
    public Result<T> get() {
        return getUnsafe(target);
    }

    @Override
    public void set(T value) {
        setUnsafe(target, value);
    }
}
