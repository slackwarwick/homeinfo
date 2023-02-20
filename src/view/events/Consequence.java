package view.events;

public abstract class Consequence<T extends Event> {
    protected abstract void occured(T event);
}
