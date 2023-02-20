package domain.usecase.pomodoro;

public class StopTaskImpl extends StopTask {

    private final String taskId;
    private final Long duration;

    public StopTaskImpl(String taskId, Long duration) {
        super();
        this.duration = duration;
        this.taskId = taskId;
    }

    @Override
    public Void doExecute() {
        store.updateTask(taskId, duration);
        return null;
    }
}
