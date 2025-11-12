package Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HexCellRenderer extends DefaultTableCellRenderer {
    private int times;
    public HexCellRenderer(int times) {
        this.times = times;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Integer) {
            setText(String.format("0x%0"+times+"X", (Integer) value));
        }
        return cell;
    }
}
