package view;

import app.Actions;
import view.events.Consequence;
import view.events.Ui;
import view.pomodoro.PomodoroContinued;
import view.pomodoro.PomodoroEnded;
import view.objectsystem.ObjectTreeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class MainView extends ObjectTreeView {
    private JFrame root;
    private JPanel panel;
    private JTabbedPane tabs;
    private JTree tree;
    private TrayIcon trayIcon;

    public MainView(Ui ui, Actions actions) {
        super(ui, actions);
        root = new JFrame("HomeInfo");
        root.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == Frame.ICONIFIED) {
                    root.setVisible(false);
                }
                if (e.getNewState() == 7) {
                    root.setVisible(false);
                }
            }
        });
        if (SystemTray.isSupported()) {
            URL url = getClass().getResource("/resources/information.png");
            Image image = Toolkit.getDefaultToolkit().createImage(url);
            trayIcon = new TrayIcon(image, "HomeInfo");
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        int state = root.getExtendedState();
                        state = state & ~Frame.ICONIFIED;
                        root.setExtendedState(state);
                        root.setVisible(true);
                        root.toFront();
                    }
                }
            });
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
        bindUi();
        root.setContentPane(panel);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.pack();
    }

    private void bindUi() {
        ui.bind(PomodoroEnded.class, new Consequence<PomodoroEnded>() {
            @Override
            protected void occured(PomodoroEnded event) {
                if (trayIcon != null) {
                    if (event.showPopup) {
                        // trayIcon.displayMessage("HomeInfo", "Pomodoro ended!", TrayIcon.MessageType.INFO);
                        JOptionPane.showMessageDialog(root, "Pomodoro ended!");
                    }
                    trayIcon.setToolTip("HomeInfo");
                }
            }
        });
        ui.bind(PomodoroContinued.class, new Consequence<PomodoroContinued>() {
            @Override
            protected void occured(PomodoroContinued event) {
                if (trayIcon != null)
                    trayIcon.setToolTip("Pomodoro: " + event.timeLeft);
            }
        });
    }

    public void addTab(TabView tabView) {
        children().add(tabView);
        tabs.add(tabView.tab().title(), tabView.tab().panel());
    }

    public void show() {
        root.setVisible(true);
    }
}
