package GUI.windowComponents;

import javax.swing.*;
import java.awt.*;

public class FlagsPanel extends JPanel {
    private JCheckBox zeroFlag;
    private JCheckBox signFlag;
    private JCheckBox carryFlag;
    private JCheckBox overflowFlag;
    private JCheckBox negativeFlag;

    public FlagsPanel() {
        setLayout(new GridLayout(5, 1));  // Adjusted for 4 flags
        setBorder(BorderFactory.createTitledBorder("Flags"));

        zeroFlag = new JCheckBox("Zero Flag");
        zeroFlag.setEnabled(false);
        signFlag = new JCheckBox("Sign Flag");
        signFlag.setEnabled(false);
        carryFlag = new JCheckBox("Carry Flag");
        carryFlag.setEnabled(false);
        negativeFlag = new JCheckBox("Negative Flag");
        negativeFlag.setEnabled(false);
        overflowFlag = new JCheckBox("Overflow Flag");
        overflowFlag.setEnabled(false);

        add(zeroFlag);
        add(signFlag);
        add(carryFlag);
        add(negativeFlag);
        add(overflowFlag);
    }

    public void updateFlags(int result, boolean carry, boolean overflow, boolean negative) {
        zeroFlag.setSelected(result == 0);
        signFlag.setSelected(result < 0);
        carryFlag.setSelected(carry);
        negativeFlag.setSelected(negative);
        overflowFlag.setSelected(overflow);

        this.revalidate();
        this.repaint();
    }

    public void resetFlags() {
        zeroFlag.setSelected(false);
        signFlag.setSelected(false);
        carryFlag.setSelected(false);
        overflowFlag.setSelected(false);

        this.revalidate();
        this.repaint();
    }
}
