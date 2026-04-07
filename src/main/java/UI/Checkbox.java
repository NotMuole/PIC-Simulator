package UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Checkbox {
    private static final Dimension CHECKBOX_DIMENSION = new Dimension(0, 0);
    private static final Logger log = LogManager.getLogger(Checkbox.class);
    public static final String[] program = new String[1024];

    public static JCheckBox createCheckbox(String text, boolean is_command, boolean is_background) {
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
        if (is_background) {
            checkbox.setBackground(Color.lightGray);
        }
        return checkbox;
    }

    public static void output() {
        for (String wert : program) {
            log.info(wert);
        }
    }
}