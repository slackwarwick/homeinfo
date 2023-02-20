package view;

import app.Actions;
import view.events.Ui;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeView extends BaseView {
    private final List<BaseView> children = new ArrayList<>();

    public TreeView(Ui ui, Actions actions) {
        super(ui, actions);
    }

    public List<BaseView> children() {
        return children;
    }
}
