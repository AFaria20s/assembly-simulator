package GUI.settingsComponents;

import javax.swing.*;
import java.awt.*;

public class AutoSavePanel extends JPanel {

    public AutoSavePanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Auto-Save Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create components for auto-save settings
        JLabel autoSaveLabel = new JLabel("Auto-Save Interval (minutes):");
        JSpinner autoSaveSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(autoSaveLabel, gbc);

        gbc.gridx = 1;
        add(autoSaveSpinner, gbc);
    }
}
