package app;

import domain.usecase.pomodoro.*;
import persistence.PomodoroStoreCsv;

import java.sql.Timestamp;

// simple dependency injection
public class Actions {
    public final Pomodoro pomodoro = new Pomodoro();

    public Actions() {
    }

    public static class Pomodoro {
        private final PomodoroStore store = new PomodoroStoreCsv();

        public StartTask startTask(Timestamp start, String text, String project) {
            return new StartTaskImpl(start, text, project)
                    .withStore(store);
        }
        public StopTask stopTask(String taskId, Long duration) {
            return new StopTaskImpl(taskId, duration)
                    .withStore(store);
        }
        public GetTasks getTasks() {
            return new GetTasksImpl()
                    .withStore(store);
        }
    }
}
