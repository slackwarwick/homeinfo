package persistence;

import domain.entity.pomodoro.Duration;
import domain.entity.pomodoro.Pomodoro;
import domain.entity.pomodoro.PomodoroTask;
import domain.usecase.pomodoro.PomodoroStore;

import java.util.*;

public class PomodoroStoreMemory extends PomodoroStore {
    private final Map<String, Pomodoro> tasks = new HashMap<>();

    @Override
    public void saveTask(PomodoroTask task) {
        task.setId(UUID.randomUUID().toString());
        tasks.put(task.getId(), task);
    }

    @Override
    public List<Pomodoro> getTasks() {
        List<Pomodoro> values = new ArrayList<>(tasks.values());
        return values;
    }

    @Override
    public void updateTask(String taskId, Long ms) {
        Pomodoro task = tasks.get(taskId);
        if (task != null) {
            Pomodoro pomodoro = new Pomodoro(task.start(), new Duration(ms), task.text(), task.project());
            pomodoro.setId(taskId);
            tasks.put(taskId, pomodoro);
        }
    }
}
