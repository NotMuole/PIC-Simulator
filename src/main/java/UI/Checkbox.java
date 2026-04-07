package UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Checkbox {
    private static final Dimension CHECKBOX_DIMENSION = new Dimension(0, 0);
    private static final Logger log = LogManager.getLogger(Checkbox.class);

    public static JCheckBox createCheckbox(String text, boolean is_command) {
        JCheckBox checkbox = new JCheckBox();
        if (!is_command) {
            checkbox.setEnabled(false);
        }
        checkbox.setPreferredSize(new Dimension(CHECKBOX_DIMENSION));
        Color normalColor = Color.BLACK;
        Color selectedColor = Color.RED;
        checkbox.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        checkbox.addActionListener(e -> {
            checkbox.setForeground(checkbox.isSelected() ? selectedColor : normalColor);
        });
        checkbox.setText(text);
        return checkbox;
    }
}