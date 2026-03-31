package UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Checkbox {
    private static final String CHECKBOX_ICON_PATH = "/checkbox_icon.png";
    private static final String SELECTED_ICON_PATH = "/breakpoint_icon.png";
    private static final Dimension CHECKBOX_DIMENSION = new Dimension(20, 20);
    private static final Logger log = LogManager.getLogger(Checkbox.class);

    public static JCheckBox init() {
        JCheckBox checkbox = new JCheckBox();
        //URL checkboxIconUrl = Checkbox.class.getResource(CHECKBOX_ICON_PATH);
        //URL selectedIconUrl = Checkbox.class.getResource(SELECTED_ICON_PATH);
        //checkbox.setSelectedIcon(new ImageIcon(selectedIconUrl));
        //checkbox.setIcon(new ImageIcon(checkboxIconUrl));
        checkbox.setPreferredSize(new Dimension(CHECKBOX_DIMENSION));
        return checkbox;
    }
}