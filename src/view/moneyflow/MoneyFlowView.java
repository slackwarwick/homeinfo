package view.moneyflow;

import app.Actions;
import view.TabView;
import view.events.Ui;

import javax.swing.*;

public class MoneyFlowView extends TabView {
    private JPanel tabPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;

    public MoneyFlowView(Ui ui, Actions actions) {
        super(ui, actions);
        initTab(new Tab(tabPanel, "Money Flow"));
    }
}
