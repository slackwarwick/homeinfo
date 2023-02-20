package view.pomodoro;

import domain.entity.pomodoro.Pomodoro;

import javax.swing.table.AbstractTableModel;
import java.sql.Timestamp;
import java.util.*;

public class PomodoroTableModel extends AbstractTableModel {
    public static final int COL_DATE = 0;
    public static final int COL_START = 1;
    public static final int COL_END = 2;
    public static final int COL_TEXT = 3;
    public static final int COL_PROJECT = 4;
    public String[] HEADER = new String[] {"Date", "Start", "End", "Task", "Project"};
    protected Class[] classes = new Class[] {
            Timestamp.class, Timestamp.class, Timestamp.class, String.class, String.class
    };

    private final LinkedList<Pomodoro> tasks = new LinkedList<>();

    public Pomodoro getLastTask() {
        if (tasks.isEmpty())
            throw new IllegalStateException("Empty tasks!");
        // last task is first task!
        return tasks.get(0);
    }

    public void updateAll(List<Pomodoro> tasks) {
        int size = this.tasks.size();
        this.tasks.clear();
        this.tasks.addAll(tasks);
        fireTableDataChanged();
    }

    public void addLast(Pomodoro pomodoro) {
        tasks.addFirst(pomodoro);
        fireTableRowsInserted(0, 0);
    }

    public void updateLast(Pomodoro pomodoro) {
        tasks.removeFirst();
        tasks.addFirst(pomodoro);
        fireTableCellUpdated(0, COL_END);
    }

    public List<Pomodoro> getTasks(Timestamp date) {
        Calendar sel = Calendar.getInstance();
        sel.setTime(date);
        List<Pomodoro> result = new ArrayList<>();
        for (Pomodoro task : tasks) {
            Calendar c = Calendar.getInstance();
            c.setTime(task.start());
            if (c.get(Calendar.YEAR) == sel.get(Calendar.YEAR)
                    && c.get(Calendar.DAY_OF_YEAR) == sel.get(Calendar.DAY_OF_YEAR))
                result.add(task);
        }
        return result;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Class getColumnClass(int col) {
        return classes[col];
    }

    @Override
    public String getColumnName(int column) {
        return HEADER[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pomodoro task = tasks.get(rowIndex);
        switch (columnIndex) {
            case COL_DATE, COL_START -> {
                return task.start();
            }
            case COL_END -> {
                if (task.duration().empty())
                    return null;
                else
                    return new Timestamp(task.start().getTime() + task.duration().asMs());
            }
            case COL_PROJECT -> {
                return task.project();
            }
            case COL_TEXT -> {
                return task.text();
            }
        }
        return null;
    }
}
