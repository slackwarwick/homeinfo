package view;

import app.Actions;
import view.events.Ui;

public class BaseView {
    protected final Actions actions;
    protected final Ui ui;
    public BaseView(Ui ui, Actions actions) {
        this.actions = actions;
        this.ui = ui;
    }
}
