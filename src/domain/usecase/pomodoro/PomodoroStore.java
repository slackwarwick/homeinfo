package domain.usecase.pomodoro;

import domain.entity.pomodoro.Pomodoro;
import domain.entity.pomodoro.PomodoroTask;
import domain.usecase.BaseStore;

import java.util.List;

public abstract class PomodoroStore extends BaseStore {
    public abstract void saveTask(PomodoroTask task);
    public abstract List<Pomodoro> getTasks();
    public abstract void updateTask(String taskId, Long time);
}
