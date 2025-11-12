package GUI.settingsComponents;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ThemeSettingsPanel extends JPanel {

    private JComboBox<String> themeComboBox;
    private JSpinner fontSizeSpinner;

    public ThemeSettingsPanel(String mainTheme, int mainFontSize) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Theme Settings"));

        themeComboBox = new JComboBox<>(new String[]{"Dark", "Light"});
        themeComboBox.setSelectedItem(Objects.toString(mainTheme));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel themeLabel = new JLabel("Theme:");
        JLabel fontSizeLabel = new JLabel("Font Size:");
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(mainFontSize, 8, 64, 1));

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(themeLabel, gbc);
        gbc.gridx = 1;
        add(themeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(fontSizeLabel, gbc);
        gbc.gridx = 1;
        add(fontSizeSpinner, gbc);
    }

    public String getSelectedTheme() {
        return Objects.requireNonNull(themeComboBox.getSelectedItem()).toString();
    }

    public int getSelectedFontSize() {
        return Objects.requireNonNull(Integer.valueOf(fontSizeSpinner.getValue().toString()));
    }
}
