package domain.entity.pomodoro;

import java.sql.Timestamp;

public class PomodoroTask extends Pomodoro {
    public PomodoroTask(Timestamp start, Duration duration, String name, String project) {
        super(start, duration, name, project);
    }

    public PomodoroTask(String name, String project) {
        this(new Timestamp(System.currentTimeMillis()), new Duration.Empty(), name, project);
    }
}
