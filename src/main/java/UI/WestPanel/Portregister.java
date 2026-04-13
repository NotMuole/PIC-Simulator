package UI.WestPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Portregister {
    private static final Logger log = LogManager.getLogger(WestPanel.class);

    public static JPanel createBitPanel() {
        JPanel p = new JPanel(new GridLayout(1, 9, 2, 2));
        p.setPreferredSize(new Dimension(250, 50));

        // linke "Header"-Zelle
        p.add(new JLabel("Pin", SwingConstants.CENTER));

        // 8 Bits: 7 .. 0

        JToggleButton[] bits = new JToggleButton[8];
        for (int bit = 7; bit >= 0; bit--) {
            JToggleButton b = new JToggleButton("0");
            b.setPreferredSize(new Dimension(5, 5));
            b.setFocusPainted(false);
            int finalBit = bit;
            b.addActionListener(e -> {
                b.setText(b.isSelected() ? "1" : "0");

                // optional: aktuellen Wert ausgeben
                int value = 0;
                for (int i = 0; i < 8; i++) {
                    if (bits[i] != null && bits[i].isSelected()) value |= (1 << i);
                }
                System.out.println("Wert: " + value + " (geklickt bit " + finalBit + ")");
            });

            bits[bit] = b;      // Index = Bitnummer
            p.add(b);
        }
        return p;
    }
}