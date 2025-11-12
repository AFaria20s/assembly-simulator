package GUI.windowComponents;

import Utils.HexCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemoryPanel extends JPanel {
    private JTable memoryTable;
    private DefaultTableModel tableModel;
    private static final String[] COLUMN_NAMES = {"Address", "Value"};

    public MemoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Memory"));

        // Initialize memory table with column names and empty data
        Object[][] data = new Object[256][2];
        for (int i = 0; i < 256; i++) {
            data[i][0] = i; // Address
            data[i][1] = 0; // Value
        }

        tableModel = new DefaultTableModel(data, COLUMN_NAMES);
        memoryTable = new JTable(tableModel);
        memoryTable.getColumnModel().getColumn(1).setCellRenderer(new HexCellRenderer(8));
        memoryTable.getColumnModel().getColumn(0).setCellRenderer(new HexCellRenderer(2));

        JScrollPane scrollPane = new JScrollPane(memoryTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateMemoryValues(int[] memory) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < memory.length; i++) {
                tableModel.setValueAt(memory[i], i, 1);
            }
            tableModel.fireTableDataChanged();
            revalidate();
            repaint();
        });
    }
}
