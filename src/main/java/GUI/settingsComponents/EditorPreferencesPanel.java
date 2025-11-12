package GUI.settingsComponents;

import javax.swing.*;
import java.awt.*;

public class EditorPreferencesPanel extends JPanel {

    public EditorPreferencesPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Editor Preferences"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create components for editor preferences
        JCheckBox lineWrappingCheckBox = new JCheckBox("Line Wrapping", true);
        JCheckBox highlightCurrentLineCheckBox = new JCheckBox("Highlight Current Line", true);
        JLabel numberTypeLabel = new JLabel("Number Type:");
        JComboBox<String> numberTypeComboBox = new JComboBox<>(new String[]{"Decimal", "Hexadecimal"});

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lineWrappingCheckBox, gbc);

        gbc.gridy = 1;
        add(highlightCurrentLineCheckBox, gbc);

        gbc.gridy = 2;
        add(numberTypeLabel, gbc);

        gbc.gridx = 1;
        add(numberTypeComboBox, gbc);
    }
}
