package domain.usecase;

public abstract class BaseAction<D extends BaseAction, R, S extends BaseStore> {
    protected S store;

    public D withStore(S store) {
        this.store = store;
        return (D) this;
    }

    public void execute(Reaction<R> reaction) {
        try {
            R result = doExecute();
            reaction.success(result);
        } catch (Throwable e) {
            reaction.error(e);
        }
    }

    protected abstract R doExecute();
}
