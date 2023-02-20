package view.pomodoro;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRenderer extends DefaultTableCellRenderer {
    public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public DateRenderer() {
        super();
    }

    @Override
    public void setValue(Object value) {
        setText((value == null) ? "" : format.format(value));
    }

}
