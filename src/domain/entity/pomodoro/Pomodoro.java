package domain.entity.pomodoro;

import domain.entity.Entity;

import java.sql.Timestamp;

public class Pomodoro extends Entity {
    private final Timestamp start;
    private final Duration duration;
    private final String name;
    private final String project;

    public Pomodoro(Timestamp start, Duration duration, String name, String project) {
        this.start = start;
        this.duration = duration;
        this.name = name;
        this.project = project;
    }

    public Timestamp start() {
        return start;
    }

    public Duration duration() { return duration; }

    public String text() { return name; }

    public String project() { return project; }
}
