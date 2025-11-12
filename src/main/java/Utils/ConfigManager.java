package Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {
    private final File dataFile;
    private JSONObject configData;

    public ConfigManager(File dataFile) {
        this.dataFile = dataFile;
        loadConfig();
    }

    private void loadConfig() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
            configData = new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastOpenFilePath() {
        return configData.getJSONArray("lastOpen").getString(0);
    }

    public void setLastOpenFilePath(String path) {
        configData.getJSONArray("lastOpen").put(0, path);
        saveConfig();
    }

    private void saveConfig() {
        try {
            Files.write(Paths.get(dataFile.getAbsolutePath()), configData.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProgramFolder() {
        return dataFile.getParent();
    }
}
