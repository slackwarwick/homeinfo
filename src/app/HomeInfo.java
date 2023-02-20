package app;

import view.events.Ui;
import view.MainView;
import view.moneyflow.MoneyFlowView;
import view.pomodoro.PomodoroView;

public class HomeInfo {
    MainView mainView;
    PomodoroView pomodoroView;
    MoneyFlowView moneyFlowView;

    private HomeInfo() {
        Actions actions = new Actions();
        Ui ui = new Ui();
        mainView = new MainView(ui, actions);
        pomodoroView = new PomodoroView(ui, actions);
        mainView.addTab(pomodoroView);
        moneyFlowView = new MoneyFlowView(ui, actions);
        mainView.addTab(moneyFlowView);
        mainView.show();
    }

    public static void main(String[] args) {
        new HomeInfo();
    }
}
