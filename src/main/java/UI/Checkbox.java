package UI;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Checkbox {
    private static final Dimension CHECKBOX_DIMENSION = new Dimension(0, 0);
    private static final Logger log = LogManager.getLogger(Checkbox.class);
    public static final String[] program = new String[1024];
    public static final boolean[] breakpoints = new boolean[10];
    private static final Color normalColor = Color.BLACK;
    private static final Color selectedColor = Color.RED;

    public static JCheckBox createCheckbox(String text, boolean is_command, boolean is_background, int number) {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setPreferredSize(new Dimension(CHECKBOX_DIMENSION));
        checkbox.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        checkbox.setText(text);

        if (is_background) {
            checkbox.setBackground(Color.lightGray);
        }

        if (is_command) {
            if (breakpoints[number]) {
                checkbox.setSelected(true);
                checkbox.setForeground(selectedColor);
        }
            checkbox.addActionListener(e -> {
                if (checkbox.isSelected()) {
                    checkbox.setForeground(selectedColor);
                    breakpoints[number] = true;
                } else {
                    checkbox.setForeground(normalColor);
                    breakpoints[number] = false;
                }
            });
        } else {
            checkbox.setEnabled(false);
        }

        return checkbox;
    }

    public static boolean getBreakpoint(int programCounter) {
        return breakpoints[programCounter];
    }
}