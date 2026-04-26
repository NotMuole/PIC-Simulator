package UI.NorthPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class NorthPanel {
    private static final Logger log = LogManager.getLogger(NorthPanel.class);
    private static final Dimension parentFieldDim = new Dimension(1920, 40);

    public static JPanel createNorthPanel() {
        JPanel outer = new JPanel(new GridLayout(1, 2, 0, 0));
        outer.add(FileSelector.createFileUploader());
        outer.add(AboutButton.createAboutButton());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }

}
