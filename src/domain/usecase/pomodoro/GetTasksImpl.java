package domain.usecase.pomodoro;

import domain.entity.pomodoro.Pomodoro;

import java.util.List;

public class GetTasksImpl extends GetTasks {
    @Override
    public List<Pomodoro> doExecute() {
        return store.getTasks();
    }
}
