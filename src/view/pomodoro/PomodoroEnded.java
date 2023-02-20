package view.pomodoro;

import view.events.Event;

public class PomodoroEnded extends Event {
    public final boolean showPopup;
    public PomodoroEnded(boolean showPopup) {
        super();
        this.showPopup = showPopup;
    }
}
