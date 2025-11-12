package MainClasses;

import GUI.Window;
import Utils.ConfigManager;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static ConfigManager configManager;

    public static void generateDefaultProgramDirectory(File programFolder, File projectsFolder, File dataFile, File tempFile) throws IOException {
        createDirectoryIfNotExists(programFolder);
        createDirectoryIfNotExists(projectsFolder);
        createFileIfNotExists(tempFile);
        createDataJsonIfNotExists(dataFile);
    }

    // Helper method to create directories if they do not exist
    private static void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Directory already exists: " + directory.getAbsolutePath());
        }
    }

    // Helper method to create files if they do not exist
    private static void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("");  // Create an empty file
            } catch (IOException e) {
                System.err.println("Failed to create file: " + file.getAbsolutePath());
                throw e;
            }
        }
    }

    // Create default `data.json` if it does not exist
    private static void createDataJsonIfNotExists(File dataFile) throws IOException {
        if (!dataFile.exists()) {
            try (FileWriter writer = new FileWriter(dataFile)) {
                String defaultJsonContent = """
                        {
                            "lastOpen": [""],
                            "settings": {
                                "theme": "dark",
                                "fontSize": 16,
                                "autoSaveInterval": 1
                            },
                            "editorPreferences": {
                                "numberType": "hex",
                                "lineWrapping": true,
                                "highlightCurrentLine": true
                            },
                            "defaultDirectory": "",
                            "shortcuts": {
                                "openFile": "Ctrl+O",
                                "saveFile": "Ctrl+S"
                            },
                            "programExecution": {
                                "instructionDelay": 500
                            }
                        }
                        """;
                writer.write(defaultJsonContent);
                System.out.println("data.json created with default content: " + dataFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to create data.json: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("data.json already exists: " + dataFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        // Initialize user home and directories
        String userHome = System.getProperty("user.home");
        File programFolder = new File(userHome, "Assembly Simulator");
        File projectsFolder = new File(programFolder, "Projects");
        File dataFile = new File(programFolder, "data.json");
        File tempFile = new File(programFolder, "temp.asm");

        try {
            // Generate directories and create the data.json file if it doesn't exist
            generateDefaultProgramDirectory(programFolder, projectsFolder, dataFile, tempFile);

            // Initialize the Window class and ConfigManager
            Window window = new Window(dataFile);
            configManager = new ConfigManager(dataFile);

            // Load the last open file from ConfigManager
            String lastOpenFilePath = configManager.getLastOpenFilePath();
            if (lastOpenFilePath != null && !lastOpenFilePath.isEmpty()) {
                window.openLastFile(lastOpenFilePath);
            }

            // Refresh UI components
            window.repaint();
            window.revalidate();

        } catch (IOException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.exit(1); // Exit the program if there's a failure
        }
    }
}
