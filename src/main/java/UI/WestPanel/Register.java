package UI.WestPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Register {
    private static final Logger log = LogManager.getLogger(WestPanel.class);
    private static final Dimension registerPanel = new Dimension(300, 620);

    public static JPanel createRegisterField() {
        JPanel inner = new JPanel();
        inner.add(RegisterList.createVisibleList());
        inner.add(RegisterList.createInvisibleList());
        inner.add(RegisterList.createStackList());
        inner.add(RegisterList.createPortAList());
        inner.add(RegisterList.createPortBList());
        inner.add(RegisterList.createFlagList());
        inner.add(RegisterList.createINTCONList());
        inner.add(ClockRate.createTimeList());
        inner.add(ClockRate.createClockRateField());
        inner.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Spezialfunktionsregister"
        ));
        inner.setMaximumSize(registerPanel);
        return inner;
    }

}

