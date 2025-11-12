package GUI.windowComponents;

import javax.swing.*;
import java.awt.*;

public class RegistersPanel extends JPanel {
    private RegistersTable registersTable;

    public RegistersPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Registers"));

        registersTable = new RegistersTable();
        JScrollPane scrollPane = new JScrollPane(registersTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public RegistersTable getRegistersTable() {
        return registersTable;
    }
}
