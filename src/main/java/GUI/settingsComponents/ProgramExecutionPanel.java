package GUI.settingsComponents;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramExecutionPanel extends JPanel {

    private JSpinner delaySpinner;

    public ProgramExecutionPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Program Execution Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel delayLabel = new JLabel("Delay between instructions (ms):");

        delaySpinner = new JSpinner(new SpinnerNumberModel(500, 10, 5000, 10));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(delayLabel, gbc);

        gbc.gridx = 1;
        add(delaySpinner, gbc);
    }

    public int getSelectedDelay() {
        return Integer.parseInt(delaySpinner.getValue().toString());
    }

    public void updateSettingsDelay(File dataFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);

        jsonObject.getJSONObject("programExecution").put("instructionDelay", getSelectedDelay());

        Files.write(Paths.get(dataFile.getAbsolutePath()), jsonObject.toString(4).getBytes());
    }

    public static int loadDelayFromFile(File dataFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
        JSONObject jsonObject = new JSONObject(content);

        return jsonObject.getJSONObject("programExecution").getInt("instructionDelay");
    }
}
