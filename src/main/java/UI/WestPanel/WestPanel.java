package UI.WestPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class WestPanel {
    private static final Logger log = LogManager.getLogger(WestPanel.class);
    private static final Dimension parentFieldDim = new Dimension(300, 680);

    public static JPanel createFieldWEST() {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.add(Register.createRegisterField());
        outer.add(ControlPanel.createControlPanel());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }
}
