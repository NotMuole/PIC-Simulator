package UI;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.ProcessIdPatternConverter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FieldWest {
    private static final Logger log = LogManager.getLogger(FieldWest.class);


    public static JPanel createFieldWEST() {
        JPanel outer = new JPanel();
        outer.setPreferredSize(new Dimension(300, 500));
        outer.add(createVisibleList());
        outer.add(createInvisibleList());
        outer.add(createStackList());
        outer.add(createFlagList());
        outer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Spezialfunktionsregister"
        ));
        return outer;
    }

    public static JList<String> createStackList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int[] stack = PIC16F84.getStack();
        for (int element : stack) {
            model.addElement(String.format("%04X", element));
        }
        JList<String> StackList = new JList<>(model);
        StackList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Stack"
        ));
        StackList.setPreferredSize(new Dimension(50, 170));
        return StackList;
    }

    public static JList<String> createFlagList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> FlagList = new JList<>(model);
        FlagList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        model.addElement(String.format("%-20s %s", "IRP RP  RP0 TO PD Z DC C", ""));
        model.addElement(String.format("%-20s %s", "0   0   0   0  0  0 0  0", ""));

        FlagList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Flags"
        ));
        FlagList.setPreferredSize(new Dimension(215, 70));
        return FlagList;
    }

    public static JList<String> createVisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int test = 0;
        model.addElement(String.format("W-Reg  %02d", test));
        model.addElement(String.format("FSR    %02d", test));
        model.addElement(String.format("PCL    %02d", test));
        model.addElement(String.format("PCLATH %02d", test));
        model.addElement(String.format("Status %02d", test));

        JList<String> VisibleList = new JList<>(model);
        VisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "sichtbar"
        ));
        VisibleList.setPreferredSize(new Dimension(80, 110));
        return VisibleList;
    }

    public static JList<String> createInvisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int test = 0;
        model.addElement(String.format("PC     %04d", test));
        model.addElement(String.format("Stackpointer %d", test));
        model.addElement(String.format("VT     %02X", test));
        model.addElement("WDT aktiv");
        model.addElement("WDT 0.0ms");

        JList<String> VisibleList = new JList<>(model);
        VisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "versteckt"
        ));
        VisibleList.setPreferredSize(new Dimension(80, 110));
        return VisibleList;
    }

}
