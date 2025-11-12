package Utils;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame {
    private JProgressBar progressBar;

    public LoadingScreen() {
        setTitle("Loading...");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Please wait...", JLabel.CENTER);
        add(label, BorderLayout.NORTH);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar, BorderLayout.CENTER);
    }
}

