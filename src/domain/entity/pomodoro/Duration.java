package domain.entity.pomodoro;

import java.util.Objects;

public class Duration {
    public static class Empty extends Duration { public Empty() { super(0L); } }
    public static class Full extends Duration { public Full() { super(25 * 60_000L); } }

    private final Long ms;
    public Duration(Long ms) {
        if (ms < 0)
            throw new IllegalArgumentException("Duration: ms < 0");
        this.ms = ms;
    }
    public Long asMs() { return ms; }
    public boolean empty() { return ms == 0; }
    public Double asMin() {
        return (double) asMs() / 1000 / 60;
    }
    public Double asHours() {
        return asMin() / 60;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!this.getClass().isAssignableFrom(other.getClass())) return false;

        Duration that = (Duration) other;
        return Objects.equals(this.ms, that.ms);
    }
}
