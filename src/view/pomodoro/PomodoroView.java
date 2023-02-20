package view.pomodoro;

import app.Actions;
import view.events.*;
import domain.entity.pomodoro.Duration;
import domain.entity.pomodoro.Pomodoro;
import domain.usecase.Reaction;
import view.TabView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PomodoroView extends TabView {
    private String currentTaskId;
    private long timeLeft = 0L;
    private final PomodoroTableModel tasksModel;
    private JButton startStopButton;
    private JTable tasksTable;
    private JPanel tabPanel;
    private JTextField contentField;
    private JLabel timerLabel;
    private JComboBox projectField;
    private JLabel dateLabel;
    private JLabel totalLabel;
    private JLabel projectLabel;
    private JLabel taskLabel;
    private final Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeLeft = timeLeft - 1000;
            if (timeLeft <= 0L)
                timeLeft = 0L;
            String time = formattedTime(timeLeft);
            timerLabel.setText(time);
            if (timeLeft > 0L)
                ui.notify(new PomodoroContinued(time));
            else
                stop();
        }
    });

    public PomodoroView(Ui ui, Actions actions) {
        super(ui, actions);
        initTab(new Tab(tabPanel, "Pomodoro"));
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running()) stop(); else start();
            }
        });
        contentField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !contentField.getText().isEmpty()) {
                    start();
                }
            }
        });
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tasksTable.getSelectedRow();
                Timestamp date = (Timestamp) tasksTable.getModel().getValueAt(selectedRow, PomodoroTableModel.COL_DATE);
                String text = (String) tasksTable.getModel().getValueAt(selectedRow, PomodoroTableModel.COL_TEXT);
                String project = (String) tasksTable.getModel().getValueAt(selectedRow, PomodoroTableModel.COL_PROJECT);
                if (!running())
                    updateFields(text, project);
                updateStatistics(date, text, project);
            }
        });
        tasksModel = new PomodoroTableModel();
        tasksTable.setModel(tasksModel);
        tasksTable.getColumnModel().getColumn(PomodoroTableModel.COL_DATE).setCellRenderer(new DateRenderer());
        tasksTable.getColumnModel().getColumn(PomodoroTableModel.COL_START).setCellRenderer(new TimeRenderer());
        tasksTable.getColumnModel().getColumn(PomodoroTableModel.COL_END).setCellRenderer(new TimeRenderer());
        bindUi();
        actions.pomodoro.getTasks().execute(new Reaction<List<Pomodoro>>() {
            @Override
            public void success(List<Pomodoro> result) {
                tasksModel.updateAll(result);
            }
            @Override
            public void error(Throwable t) {
            }
        });
    }

    private void bindUi() {
        ui.bind(PomodoroStarted.class, new Consequence<PomodoroStarted>() {
            @Override
            protected void occured(PomodoroStarted event) {
                startStopButton.setText("Stop");
                contentField.setEnabled(false);
                projectField.setEnabled(false);
                timer.start();
                actions.pomodoro.getTasks().execute(new Reaction<List<Pomodoro>>() {
                    @Override
                    public void success(List<Pomodoro> result) {
                        tasksModel.addLast(result.get(0));
                    }
                    @Override
                    public void error(Throwable t) {
                    }
                });
            }
        });
        ui.bind(PomodoroEnded.class, new Consequence<PomodoroEnded>() {
            @Override
            protected void occured(PomodoroEnded event) {
                timer.stop();
                startStopButton.setText("Start");
                contentField.setEnabled(true);
                projectField.setEnabled(true);
                actions.pomodoro.getTasks().execute(new Reaction<List<Pomodoro>>() {
                    @Override
                    public void success(List<Pomodoro> result) {
                        tasksModel.updateLast(result.get(0));
                    }
                    @Override
                    public void error(Throwable t) {
                    }
                });
            }
        });
    }

    private void updateFields(String text, String project) {
        contentField.setText(text);
        projectField.setSelectedItem(project);
    }

    private void updateStatistics(Timestamp date, String text, String project) {
        dateLabel.setText("Date: " + DateRenderer.format.format(date));
        List<Pomodoro> tasks = tasksModel.getTasks(date);
        double timeTotal = 0.0, timeProject = 0.0, timeTask = 0.0;
        double timeBreak = 5.0 / 60;
        for (Pomodoro task : tasks) {
            timeTotal += task.duration().asHours() + timeBreak;
            if (Objects.equals(task.project(), project))
                timeProject += task.duration().asHours() + timeBreak;
            if (Objects.equals(task.text(), text))
                timeTask += task.duration().asHours() + timeBreak;
        }
        totalLabel.setText("Total: " + rounded(timeTotal));
        projectLabel.setText("Project: " + rounded(timeProject));
        taskLabel.setText("Task: " + rounded(timeTask));
    }

    private double rounded(double time) {
        return (double) Math.round(time * 10) / 10;
    }

    private boolean running() {
        return timer.isRunning();
    }

    private void stop() {
        actions.pomodoro.stopTask(
                    currentTaskId,
                    new Duration.Full().asMs() - timeLeft)
                .execute(new Reaction<>() {
            @Override
            public void success(Void result) {
                ui.notify(new PomodoroEnded(timeLeft == 0L));
            }
            @Override
            public void error(Throwable t) {
            }
        });
    }

    private void start() {
        actions.pomodoro.startTask(
                    new Timestamp(System.currentTimeMillis()),
                    contentField.getText(),
                    (String)projectField.getSelectedItem())
                .execute(new Reaction<>() {
            @Override
            public void success(Pomodoro task) {
                currentTaskId = task.getId();
                timeLeft = new Duration.Full().asMs();
                ui.notify(new PomodoroStarted());
            }
            @Override
            public void error(Throwable t) {
            }
        });
    }

    private String formattedTime(Long timeLeft) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft));
        StringBuilder b = new StringBuilder();
        b.append(minutes == 0 ? "00" : minutes < 10 ? ("0" + minutes) :
                String.valueOf(minutes));
        b.append(":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? ("0" + seconds) :
                String.valueOf(seconds));
        return b.toString();
    }
}
