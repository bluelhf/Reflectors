package io.github.bluelhf.reflectors;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class Result<T> {
    private @Nullable Throwable throwable = null;
    private @Nullable T result = null;

    public Result(T result) {
        this.result = result;
    }
    public Result(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean hasThrowable() {
        return throwable != null;
    }

    public boolean hasResult() {
        return !hasThrowable() && result != null;
    }

    public @Nullable T getResult() {
        return result;
    }

    public @Nullable Throwable getThrowable() {
        return throwable;
    }

    public <R> Result<R> map(Function<T, R> mapper) {
        return hasThrowable() ? new Result<>(throwable) : new Result<>(mapper.apply(result));
    }

    public Object resultOr(Object other) {
        return hasResult() ? result : other;
    }
    public Object throwableOr(Object other) {
        return hasThrowable() ? throwable : other;
    }

    public void consume(Consumer<T> resultConsumer) {
        if (throwable == null) resultConsumer.accept(result);
    }

    public void consume(Consumer<T> resultConsumer, Consumer<Throwable> throwableConsumer) {
        if (throwable != null) {
            throwableConsumer.accept(throwable);
            return;
        }
        resultConsumer.accept(result);
    }
}
