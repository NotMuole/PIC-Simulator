package UI.WestPanel;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Register {
    private static final Logger log = LogManager.getLogger(WestPanel.class);
    private static final Dimension registerPanel = new Dimension(250, 450);

    public static JPanel createRegisterField() {
        JPanel inner = new JPanel();
        inner.add(RegisterList.createVisibleList());
        inner.add(RegisterList.createInvisibleList());
        inner.add(RegisterList.createStackList());
        inner.add(RegisterList.createFlagList());
        inner.add(ClockRate.createTimeList());
        inner.add(ClockRate.createClockRateField());
        //inner.add(RegisterList.createIndList());
        inner.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Spezialfunktionsregister"
        ));
        inner.setMaximumSize(registerPanel);
        return inner;
    }

}

