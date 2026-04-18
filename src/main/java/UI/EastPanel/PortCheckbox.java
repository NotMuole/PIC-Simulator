package UI.EastPanel;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class PortCheckbox {
    private static final Logger log = LogManager.getLogger(PortCheckbox.class);
    private static final Icon selected = new ImageIcon(PortCheckbox.class.getResource("/selected.png"));
    private static final Icon notSelected = new ImageIcon(PortCheckbox.class.getResource("/unselected.png"));


    public static JCheckBox createCheckbox(int i) {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setIcon(notSelected);
        checkbox.setSelectedIcon(selected);
        checkbox.setPreferredSize(new Dimension(20, 20));
        int value = PIC16F84.getVisualizedRAM(6);
        int bitValue = (value >> i) & 1;
        if (bitValue == 1) {
            checkbox.setSelected(true);
        } else {
            checkbox.setSelected(false);
        }

        return checkbox;
    }
}
