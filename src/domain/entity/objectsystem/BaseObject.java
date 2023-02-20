package domain.entity.objectsystem;

public abstract class BaseObject {
    private final BaseObject parent;

    protected BaseObject(BaseObject parent) {
        this.parent = parent;
    }
}
