package GUI;

import GUI.windowComponents.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Window extends JFrame {
    private MenuPanel menuPanel;
    private RegistersPanel registersPanel;
    private MemoryPanel memoryPanel;
    private FlagsPanel flagsPanel;
    private EditorPanel editorPanel;

    public Window(File dataFile) throws IOException, UnsupportedLookAndFeelException {
        setTitle("Assembly Simulator");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Read the theme and font size from data.json
        String theme = loadThemeFromFile(dataFile);
        int fontSize = getFontSizeFromFile(dataFile);

        updateTheme(this, theme);

        // Create panels for registers, memory, flags, editor, and menu
        registersPanel = new RegistersPanel();
        memoryPanel = new MemoryPanel();
        flagsPanel = new FlagsPanel();
        editorPanel = new EditorPanel(registersPanel, memoryPanel, flagsPanel, fontSize);

        // Pass the dataFile to the MenuPanel
        menuPanel = new MenuPanel(editorPanel, this, dataFile);

        // Left panel (registers and memory)
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, registersPanel, memoryPanel);
        leftSplitPane.setDividerLocation(200);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(leftSplitPane, BorderLayout.CENTER);
        leftPanel.add(flagsPanel, BorderLayout.SOUTH);

        // Right panel (editor)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(editorPanel, BorderLayout.CENTER);

        // Main split pane to divide left and right panels
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        mainSplitPane.setDividerLocation(250);

        // Add the menu panel to the frame
        add(menuPanel, BorderLayout.NORTH);
        // Add main split pane to the frame
        add(mainSplitPane, BorderLayout.CENTER);

        // Handle window closing events (to save any unsaved work)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPanel.getFileHandler().saveFile();
            }
        });
    }

    // Method to get the font size from data.json
    private int getFontSizeFromFile(File dataFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONObject("settings").getInt("fontSize");
    }

    // Method to open the last file if available
    public void openLastFile(String lastOpenFilePath) {
        if (lastOpenFilePath != null && !lastOpenFilePath.isEmpty()) {
            menuPanel.getFileHandler().openFile(new File(lastOpenFilePath));
            System.out.println("Opened last file: " + lastOpenFilePath);
        }
    }

    // Update UI theme based on the user's selection (dark or light mode)
    public void updateTheme(JFrame frame, String theme) throws UnsupportedLookAndFeelException {
        // Update the Look and Feel
        if (theme.equalsIgnoreCase("dark")) {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } else if (theme.equalsIgnoreCase("light")) {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }

        // Rebuild the UI to reflect the new Look and Feel
        SwingUtilities.updateComponentTreeUI(frame);

        // Revalidate and repaint the frame
        this.revalidate();
        this.repaint();
    }

    // Method to load the theme from data.json
    private String loadThemeFromFile(File dataFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONObject("settings").getString("theme");
    }

    // Update window title
    public void updateTitle(String title) {
        this.setTitle("Assembly Simulator - " + title);
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }
}
