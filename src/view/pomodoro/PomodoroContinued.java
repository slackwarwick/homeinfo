package view.pomodoro;

import view.events.Event;

public class PomodoroContinued extends Event {
    public final String timeLeft;
    public PomodoroContinued(String timeLeft) {
        super();
        this.timeLeft = timeLeft;
    }
}
