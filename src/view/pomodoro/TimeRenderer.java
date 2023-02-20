package view.pomodoro;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeRenderer extends DefaultTableCellRenderer {

    public static final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    public TimeRenderer() {
        super();
    }

    @Override
    public void setValue(Object value) {
        setText((value == null) ? "" : format.format(value));
    }
}