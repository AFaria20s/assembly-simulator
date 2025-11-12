package tests;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TestPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Registers Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            DefaultTableModel model = new DefaultTableModel(new Object[][]{
                    {"R0", 0},
                    {"R1", 0},
                    {"R2", 0},
                    {"R3", 0},
                    {"R4", 0},
                    {"R5", 0},
                    {"R6", 0},
                    {"R7", 0},
            }, new String[]{"Register", "Value"});

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);

            JButton updateButton = new JButton("Update");
            frame.add(updateButton, BorderLayout.SOUTH);

            updateButton.addActionListener(e -> {
                // Simulate register updates
                for (int i = 0; i < 8; i++) {
                    model.setValueAt((int) (Math.random() * 100), i, 1);
                }
            });

            frame.setVisible(true);
        });
    }
}
