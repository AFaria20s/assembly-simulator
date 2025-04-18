package GUI.windowComponents;

import Utils.HexCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RegistersTable extends JTable {
    private final DefaultTableModel tableModel;
    private static final String[] COLUMN_NAMES = {"Register", "Value"};
    private static final Object[][] INITIAL_DATA = {
            {"R0", 0},
            {"R1", 0},
            {"R2", 0},
            {"R3", 0},
            {"R4", 0},
            {"R5", 0},
            {"R6", 0},
            {"R7", 0},
    };

    public RegistersTable() {
        tableModel = new DefaultTableModel(INITIAL_DATA, COLUMN_NAMES);
        setModel(tableModel);
        setCellSelectionEnabled(false);
        setPreferredScrollableViewportSize(new Dimension(400, 200));
        setFillsViewportHeight(true);
        getColumnModel().getColumn(1).setCellRenderer(new HexCellRenderer(8)); // Use your custom renderer
    }

    public void updateRegisterValues(int[] registerValues) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < registerValues.length; i++) {
                tableModel.setValueAt(registerValues[i], i, 1);
            }
            tableModel.fireTableDataChanged();
            revalidate();
            repaint();
        });
    }
}
