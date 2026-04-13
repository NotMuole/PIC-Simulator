package UI.WestPanel;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class RegisterList {
    private static final Logger log = LogManager.getLogger(WestPanel.class);

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
        StackList = disableSelection(StackList);
        return StackList;
    }

    public static JList<String> createFlagList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> FlagList = new JList<>(model);
        int zeroFlag = PIC16F84.getZeroFlag();
        int digitCarryFlag = PIC16F84.getDigitcarryFlag();
        int carryFlag = PIC16F84.getCarryFlag();
        int irp = (PIC16F84.getRAM(3) & 128) >> 7;
        int rp = (PIC16F84.getRAM(3) & 64) >> 6;
        int rp0 = (PIC16F84.getRAM(3) & 32) >> 5;
        int to = (PIC16F84.getRAM(3) & 16) >> 4;
        int pd = (PIC16F84.getRAM(3) & 8) >> 3;
        FlagList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        model.addElement("IRP RP RP0 TO PD Z DC C");
        model.addElement(String.format("%01d   %01d  %01d   %01d  %01d  %01d %01d  %01d", irp, rp, rp0, to, pd, zeroFlag, digitCarryFlag, carryFlag));
        FlagList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Flags"
        ));
        FlagList.setPreferredSize(new Dimension(230, 70));
        FlagList = disableSelection(FlagList);
        return FlagList;
    }

    public static JList<String> createVisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int wReg = PIC16F84.getWReg();
        int PCL = PIC16F84.getProgramCounter();
        int Status = PIC16F84.getRAM(3);
        int FSR = PIC16F84.getRAM(4);
        model.addElement(String.format("W-Reg   %02X", wReg));
        model.addElement(String.format("FSR     %02X", FSR));
        model.addElement(String.format("PCL     %02X", PCL));
        model.addElement(String.format("PCLATH  %02X", 0));
        model.addElement(String.format("Status  %02X", Status));

        JList<String> VisibleList = new JList<>(model);
        VisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "sichtbar"
        ));
        VisibleList.setPreferredSize(new Dimension(90, 110));
        VisibleList = disableSelection(VisibleList);
        return VisibleList;
    }

    public static JList<String> createInvisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int programCounter = PIC16F84.getActualProgramCounter();
        int stackPointer = PIC16F84.getStackIndex();
        int vorteiler = PIC16F84.PSA0_2;
        model.addElement(String.format("PC     %04X", programCounter));
        model.addElement(String.format("SP %d", stackPointer));
        model.addElement(String.format("VT     %02X", vorteiler));
        model.addElement("WDT aktiv");
        model.addElement("WDT 0.0ms");

        JList<String> InvisibleList = new JList<>(model);
        InvisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "versteckt"
        ));
        InvisibleList.setPreferredSize(new Dimension(80, 110));
        InvisibleList = disableSelection(InvisibleList);
        return InvisibleList;
    }

    public static JList<String> createIndList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> TimeList = new JList<>(model);
        int indirect = PIC16F84.ind;
        TimeList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        model.addElement(String.format("%X", indirect));
        TimeList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Indirect"
        ));
        TimeList.setPreferredSize(new Dimension(80, 50));
        TimeList = disableSelection(TimeList);
        return TimeList;
    }

    private static JList<String> disableSelection(JList<String> list) {
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { /* no-op */ }
            @Override public void addSelectionInterval(int index0, int index1) { /* no-op */ }
        });
        return list;
    }
}
