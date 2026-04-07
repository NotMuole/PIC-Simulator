package UI;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class RAM {
    private static final Logger log = LogManager.getLogger(RAM.class);
    private static final Dimension parentFieldDim = new Dimension(310, 500);
    private static final Dimension subFieldDim = new Dimension(310, 400);

    public static JPanel createFieldEAST() {
        JPanel outer = new JPanel();
        outer.add(createSubFieldEAST());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }

    private static JPanel createSubFieldEAST() {
        JPanel inner1 = new JPanel();
        inner1.add(createRAM());
        inner1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "RAM"
        ));
        inner1.setMaximumSize(subFieldDim);
        return inner1;
    }

    private static JList<String> createRAM() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int test = 0;
        int wReg = PIC16F84.getWReg();
        int PCL = PIC16F84.getProgramCounter();
        int Status = PIC16F84.getRAM(3);
        int counter = 0;
        model.addElement("     0   1   2    3   4   5   6    7   8   9   A   B   C   D   E   F");
        for (int i=0; i<255; i += 16) {
            model.addElement(String.format("%01X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X",
                    counter,
                    PIC16F84.getRAM(i),
                    PIC16F84.getRAM(i+1),
                    PIC16F84.getRAM(i+2),
                    PIC16F84.getRAM(i+3),
                    PIC16F84.getRAM(i+4),
                    PIC16F84.getRAM(i+5),
                    PIC16F84.getRAM(i+6),
                    PIC16F84.getRAM(i+7),
                    PIC16F84.getRAM(i+8),
                    PIC16F84.getRAM(i+9),
                    PIC16F84.getRAM(i+10),
                    PIC16F84.getRAM(i+11),
                    PIC16F84.getRAM(i+12),
                    PIC16F84.getRAM(i+13),
                    PIC16F84.getRAM(i+14),
                    PIC16F84.getRAM(i+15)
                    ));
            counter ++;
        }

        JList<String> RAMList = new JList<>(model);
        RAMList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "RAM"
        ));
        RAMList.setPreferredSize(new Dimension(310, 330));
        RAMList.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { /* no-op */ }
            @Override public void addSelectionInterval(int index0, int index1) { /* no-op */ }
        });
        return RAMList;
    }

}
