package io.github.bluelhf.reflectors.dynamics;

import io.github.bluelhf.reflectors.Result;
import io.github.bluelhf.reflectors.statics.BaseMethodReference;

import java.lang.reflect.Method;

public class InstanceMethodReference<T> extends BaseMethodReference<T> {
    Object target;
    public InstanceMethodReference(Object target, Method method) {
        super(method);
        this.target = target;
    }

    @Override
    public Result<T> invoke(Object... params) {
        return super.invoke(target, params);
    }
}
