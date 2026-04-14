package UI.WestPanel;

import PIC.PIC16F84;
import UI.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class RegisterList {
    private static final Logger log = LogManager.getLogger(RegisterList.class);

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
        model.addElement("IRP RP RP0 TO PD Z DC C");
        model.addElement(String.format("%01d   %01d  %01d   %01d  %01d  %01d %01d  %01d", irp, rp, rp0, to, pd, zeroFlag, digitCarryFlag, carryFlag));
        FlagList.setFont(new Font("Monospaced", Font.PLAIN, 14));
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
        boolean watchdogEnabled = PIC16F84.watchdogEnabled;
        float watchdogTimer = PIC16F84.watchdogTimer;
        model.addElement(String.format("PC     %04X", programCounter));
        model.addElement(String.format("SP %d", stackPointer));
        model.addElement(String.format("VT     %02X", vorteiler));
        if (watchdogEnabled) {
            model.addElement("WDT aktiv");
        } else {
            model.addElement("WDT inaktiv");
        }
        model.addElement(String.format("WDT   %.1f ms", watchdogTimer));

        JList<String> InvisibleList = new JList<>(model);
        InvisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "versteckt"
        ));
        InvisibleList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            String selected = InvisibleList.getSelectedValue();
            if (selected == null) {
                return;
            } else if (selected.equals("WDT aktiv") || (selected.equals("WDT inaktiv"))) {
                log.info("change Watchdog from " + watchdogEnabled + " to " + !watchdogEnabled);
                PIC16F84.watchdogEnabled = !watchdogEnabled;
                log.info (PIC16F84.watchdogEnabled);
            }
            InvisibleList.clearSelection();
            InvisibleList.setFocusable(false);
            MainFrame.paintWestPanel();

        });
        InvisibleList.setPreferredSize(new Dimension(100, 110));
        return InvisibleList;
    }

    public static JList<String> createPortAList() {
        DefaultListModel<String> model = new DefaultListModel<>();

        //get PortA and set Bits
        int portA = PIC16F84.getRAM(5);
        int vRA4 = (portA & 16) >> 4;
        int vRA3 = (portA & 8) >> 3;
        int vRA2 = (portA & 4) >> 2;
        int vRA1 = (portA & 2) >> 1;
        int vRA0 = (portA & 1);

        //get TrisA and set Text
        int trisA = PIC16F84.getRAM(133);
        int dRA4 = (trisA & 16) >> 4;
        int dRA3 = (trisA & 8) >> 3;
        int dRA2 = (trisA & 4) >> 2;
        int dRA1 = (trisA & 2) >> 1;
        int dRA0 = (trisA & 1);
        String sRA4 = (dRA4 == 1) ? "I" : "O";
        String sRA3 = (dRA3 == 1) ? "I" : "O";
        String sRA2 = (dRA2 == 1) ? "I" : "O";
        String sRA1 = (dRA1 == 1) ? "I" : "O";
        String sRA0 = (dRA0 == 1) ? "I" : "O";

        //add text elements to model
        model.addElement("RA0   " + sRA0 + "    " + vRA0);
        model.addElement("RA1   " + sRA1 + "    " + vRA1);
        model.addElement("RA2   " + sRA2 + "    " + vRA2);
        model.addElement("RA3   " + sRA3 + "    " + vRA3);
        model.addElement("RA4   " + sRA4 + "    " + vRA4);
        model.addElement("RA5   I   0");
        model.addElement("RA6   I   0");
        model.addElement("RA7   I   0");

        JList<String> InvisibleList = new JList<>(model);
        InvisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Port A"
        ));

        InvisibleList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            String selected = InvisibleList.getSelectedValue();
            if (selected == null) {
                return;
            }

            if (selected.startsWith("RA0")) {
                if (vRA0 == 1) {
                    PIC16F84.writeRAM(5, portA&254);
                } else {
                    PIC16F84.writeRAM(5, portA+1);
                }
            } else if (selected.startsWith("RA1")) {
                if (vRA1 == 1) {
                    PIC16F84.writeRAM(5, portA&253);
                } else {
                    PIC16F84.writeRAM(5, portA+2);
                }
            } else if (selected.startsWith("RA2")) {
                if (vRA2 == 1) {
                    PIC16F84.writeRAM(5, portA&251);
                } else {
                    PIC16F84.writeRAM(5, portA+4);
                }
            } else if (selected.startsWith("RA3")) {
                if (vRA3 == 1) {
                    PIC16F84.writeRAM(5, portA&247);
                } else {
                    PIC16F84.writeRAM(5, portA+8);
                }
            } else if (selected.startsWith("RA4")) {
                if (vRA4 == 1) {
                    PIC16F84.writeRAM(5, portA&239);
                } else {
                    PIC16F84.writeRAM(5, portA+16);
                }
            }
            InvisibleList.clearSelection();
            InvisibleList.setFocusable(false);
            MainFrame.paintWestPanel();

        });
        InvisibleList.setPreferredSize(new Dimension(100, 170));
        return InvisibleList;
    }

    public static JList<String> createPortBList() {
        DefaultListModel<String> model = new DefaultListModel<>();

        //get PortB and set Bits
        int portB = PIC16F84.getRAM(6);
        int vRB7 = (portB & 128) >> 7;
        int vRB6 = (portB & 64) >> 6;
        int vRB5 = (portB & 32) >> 5;
        int vRB4 = (portB & 16) >> 4;
        int vRB3 = (portB & 8) >> 3;
        int vRB2 = (portB & 4) >> 2;
        int vRB1 = (portB & 2) >> 1;
        int vRB0 = (portB & 1);

        //get TrisB and set Text
        int trisB = PIC16F84.getRAM(134);
        int dRB7 = (trisB & 128) >> 7;
        int dRB6 = (trisB & 64) >> 6;
        int dRB5 = (trisB & 32) >> 5;
        int dRB4 = (trisB & 16) >> 4;
        int dRB3 = (trisB & 8) >> 3;
        int dRB2 = (trisB & 4) >> 2;
        int dRB1 = (trisB & 2) >> 1;
        int dRB0 = (trisB & 1);
        String sRB7 = (dRB7 == 1) ? "I" : "O";
        String sRB6 = (dRB6 == 1) ? "I" : "O";
        String sRB5 = (dRB5 == 1) ? "I" : "O";
        String sRB4 = (dRB4 == 1) ? "I" : "O";
        String sRB3 = (dRB3 == 1) ? "I" : "O";
        String sRB2 = (dRB2 == 1) ? "I" : "O";
        String sRB1 = (dRB1 == 1) ? "I" : "O";
        String sRB0 = (dRB0 == 1) ? "I" : "O";

        //add text elements to model
        model.addElement("RB0   " + sRB0 + "    " + vRB0);
        model.addElement("RB1   " + sRB1 + "    " + vRB1);
        model.addElement("RB2   " + sRB2 + "    " + vRB2);
        model.addElement("RB3   " + sRB3 + "    " + vRB3);
        model.addElement("RB4   " + sRB4 + "    " + vRB4);
        model.addElement("RB5   " + sRB5 + "    " + vRB5);
        model.addElement("RB6   " + sRB6 + "    " + vRB6);
        model.addElement("RB7   " + sRB7 + "    " + vRB7);

        JList<String> InvisibleList = new JList<>(model);
        InvisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Port B"
        ));

        InvisibleList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            String selected = InvisibleList.getSelectedValue();
            if (selected == null) {
                return;
            }

            if (selected.startsWith("RB0")) {
                if (vRB0 == 1) {
                    PIC16F84.writeRAM(6, portB&254);
                } else {
                    PIC16F84.writeRAM(6, portB+1);
                }
            } else if (selected.startsWith("RB1")) {
                if (vRB1 == 1) {
                    PIC16F84.writeRAM(6, portB&253);
                } else {
                    PIC16F84.writeRAM(6, portB+2);
                }
            } else if (selected.startsWith("RB2")) {
                if (vRB2 == 1) {
                    PIC16F84.writeRAM(6, portB&251);
                } else {
                    PIC16F84.writeRAM(6, portB+4);
                }
            } else if (selected.startsWith("RB3")) {
                if (vRB3 == 1) {
                    PIC16F84.writeRAM(6, portB&247);
                } else {
                    PIC16F84.writeRAM(6, portB+8);
                }
            } else if (selected.startsWith("RB4")) {
                if (vRB4 == 1) {
                    PIC16F84.writeRAM(6, portB&239);
                } else {
                    PIC16F84.writeRAM(6, portB+16);
                }
            } else if (selected.startsWith("RB5")) {
                if (vRB5 == 1) {
                    PIC16F84.writeRAM(6, portB & 223);
                } else {
                    PIC16F84.writeRAM(6, portB + 32);
                }
            } else if (selected.startsWith("RB6")) {
                if (vRB6 == 1) {
                    PIC16F84.writeRAM(6, portB & 191);
                } else {
                    PIC16F84.writeRAM(6, portB + 64);
                }
            } else if (selected.startsWith("RB7")) {
                if (vRB7 == 1) {
                    PIC16F84.writeRAM(6, portB & 127);
                } else {
                    PIC16F84.writeRAM(6, portB + 128);
                }
            }
            InvisibleList.clearSelection();
            InvisibleList.setFocusable(false);
            MainFrame.paintWestPanel();

        });
        InvisibleList.setPreferredSize(new Dimension(100, 170));
        return InvisibleList;
    }

    private static JList<String> disableSelection(JList<String> list) {
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { /* no-op */ }
            @Override public void addSelectionInterval(int index0, int index1) { /* no-op */ }
        });
        return list;
    }
}
