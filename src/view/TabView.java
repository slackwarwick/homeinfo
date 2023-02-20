package view;

import app.Actions;
import view.events.Ui;

import javax.swing.*;

public abstract class TabView extends BaseView {
    public record Tab(JPanel panel, String title) {
    }

    private Tab tab;

    public TabView(Ui ui, Actions actions) {
        super(ui, actions);
    }

    protected void initTab(Tab tab) {
        this.tab = tab;
    }

    public Tab tab() { return tab; }
}
