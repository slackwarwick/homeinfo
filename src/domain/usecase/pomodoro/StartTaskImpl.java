package domain.usecase.pomodoro;

import domain.entity.pomodoro.Pomodoro;
import domain.entity.pomodoro.PomodoroTask;

import java.sql.Timestamp;

public class StartTaskImpl extends StartTask {
    private final Timestamp start;
    private final String text;
    private final String project;

    public StartTaskImpl(Timestamp start, String text, String project) {
        super();
        this.start = start;
        this.text = text;
        this.project = project;
    }

    @Override
    public Pomodoro doExecute() {
        PomodoroTask task = new PomodoroTask(text, project);
        store.saveTask(task);
        return task;
    }
}
