package domain.usecase;

public abstract class Reaction<T> {
    public abstract void success(T result);
    public abstract void error(Throwable t);
}
