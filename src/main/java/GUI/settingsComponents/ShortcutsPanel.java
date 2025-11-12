package GUI.settingsComponents;

import javax.swing.*;
import java.awt.*;

public class ShortcutsPanel extends JPanel {

    public ShortcutsPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Shortcut Keys"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create components for shortcuts
        JLabel openFileLabel = new JLabel("Open File:");
        JTextField openFileShortcutField = new JTextField("Ctrl+O");
        JLabel saveFileLabel = new JLabel("Save File:");
        JTextField saveFileShortcutField = new JTextField("Ctrl+S");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(openFileLabel, gbc);

        gbc.gridx = 1;
        add(openFileShortcutField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(saveFileLabel, gbc);

        gbc.gridx = 1;
        add(saveFileShortcutField, gbc);
    }
}
