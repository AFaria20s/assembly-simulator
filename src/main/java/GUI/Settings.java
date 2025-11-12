package GUI;

import GUI.settingsComponents.*;
import GUI.windowComponents.EditorPanel;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings extends JFrame {
    private final ThemeSettingsPanel themeSettingsPanel;
    private final ProgramExecutionPanel programExecution;
    private final Window window;
    private final File dataFile;
    private final EditorPanel editorPanel;

    public Settings(Window window, File dataFile) throws IOException {
        this.window = window;
        this.dataFile = dataFile;
        editorPanel = window.getEditorPanel();

        setTitle("Settings");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        themeSettingsPanel = new ThemeSettingsPanel(loadThemeFromFile(dataFile), getFontSize());
        programExecution = new ProgramExecutionPanel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(themeSettingsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new EditorPreferencesPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new ShortcutsPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new AutoSavePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(programExecution);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                saveSettings();
            } catch (IOException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void saveSettings() throws IOException, UnsupportedLookAndFeelException {
        /*
            SAVE THEMES
         */
        String selectedTheme = themeSettingsPanel.getSelectedTheme();
        window.updateTheme(window, selectedTheme);
        window.updateTheme(this, selectedTheme);
        saveThemeToFile(selectedTheme);
        /*
            SAVE FONT SIZE
         */
        saveFontSizeToFile(themeSettingsPanel.getSelectedFontSize());
        editorPanel.updateFontSize(getFontSize());
        /*
            SAVE INSTRUCTION DELAY
         */
        programExecution.updateSettingsDelay(dataFile);

        dispose();
    }

    private int getFontSize() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        return Integer.parseInt(jsonObject.getJSONObject("settings").get("fontSize").toString());
    }

    private void saveFontSizeToFile(int fontSize) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        jsonObject.getJSONObject("settings").put("fontSize", fontSize);

        Files.write(Paths.get(dataFile.getAbsolutePath()), jsonObject.toString(4).getBytes());
    }

    private void saveThemeToFile(String theme) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        jsonObject.getJSONObject("settings").put("theme", theme);

        Files.write(Paths.get(dataFile.getAbsolutePath()), jsonObject.toString(4).getBytes());
    }

    public static String loadThemeFromFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONObject("settings").getString("theme");
    }
}
