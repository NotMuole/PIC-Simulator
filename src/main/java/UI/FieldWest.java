package UI;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class FieldWest {
    private static final Logger log = LogManager.getLogger(FieldWest.class);
    private static final Dimension parentFieldDim = new Dimension(250, 400);
    private static final Dimension subFieldDim = new Dimension(250, 300);
    private static final Dimension subField2Dim = new Dimension(250, 100);
    private static Component currentStartButton;
    private static Component currentStepButton;
    private static Component currentResetButton;

    public static JPanel createFieldWEST() {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.add(createSubField1WEST());
        outer.add(createSubField2WEST());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }

    private static JPanel createSubField1WEST() {
        JPanel inner1 = new JPanel();
        inner1.add(createVisibleList());
        inner1.add(createInvisibleList());
        inner1.add(createStackList());
        inner1.add(createFlagList());
        inner1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Spezialfunktionsregister"
        ));
        inner1.setMaximumSize(subFieldDim);
        return inner1;
    }

    private static JPanel createSubField2WEST() {
        JPanel inner2 = new JPanel();
        if (currentStartButton == null) {
            currentStartButton = createStartButton();
            currentStepButton = createStepButton();
            currentResetButton = createResetButton();
        }
        inner2.add(currentStartButton);
        inner2.add(currentStepButton);
        inner2.add(currentResetButton);
        inner2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Steuerpult"
        ));
        inner2.setMaximumSize(subField2Dim);
        return inner2;
    }

    private static JList<String> createStackList() {
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

    private static JList<String> createFlagList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> FlagList = new JList<>(model);
        int zeroFlag = PIC16F84.getZeroFlag();
        int digitCarryFlag = PIC16F84.getDigitcarryFlag();
        int carryFlag = PIC16F84.getCarryFlag();
        FlagList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        model.addElement("IRP RP RP0 TO PD Z DC C");
        model.addElement(String.format("0   0  0   0  0  %01d %01d  %01d", zeroFlag, digitCarryFlag, carryFlag));
        FlagList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Flags"
        ));
        FlagList.setPreferredSize(new Dimension(230, 70));
        FlagList = disableSelection(FlagList);
        return FlagList;
    }

    private static JList<String> createVisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int test = 0;
        int wReg = PIC16F84.getWReg();
        int PCL = PIC16F84.getProgramCounter();
        int Status = PIC16F84.getRAM(3);
        model.addElement(String.format("W-Reg   %02d", wReg));
        model.addElement(String.format("FSR     %02d", test));
        model.addElement(String.format("PCL     %02d", PCL));
        model.addElement(String.format("PCLATH  %02d", test));
        model.addElement(String.format("Status  %02d", Status));

        JList<String> VisibleList = new JList<>(model);
        VisibleList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "sichtbar"
        ));
        VisibleList.setPreferredSize(new Dimension(90, 110));
        VisibleList = disableSelection(VisibleList);
        return VisibleList;
    }

    private static JList<String> createInvisibleList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        int test = 0;
        int programCounter = PIC16F84.getActualProgramCounter();
        model.addElement(String.format("PC     %04d", programCounter));
        model.addElement(String.format("Stackpointer %d", test));
        model.addElement(String.format("VT     %02X", test));
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

    private static JList<String> disableSelection(JList<String> list) {
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { /* no-op */ }
            @Override public void addSelectionInterval(int index0, int index1) { /* no-op */ }
        });
        return list;
    }

    private static JButton createStartButton() {
        JButton button = new JButton("Start/Pause ⏯");
        button.addActionListener(e -> {
            if (MyFrame.uploaded_file) {
                PIC16F84.toggleIsPaused();
                Thread executionThread = new Thread(PIC16F84::runProgram);
                executionThread.start();
                log.info("Programm gestartet");
            }
        });
        return button;
    }

    private static JButton createStepButton() {
        JButton button = new JButton("next Step ⏭");
        button.addActionListener(e -> {
            if (PIC16F84.getIsPaused() && MyFrame.uploaded_file) {
                PIC16F84.stepProgram();
            }
        });
        return button;
    }

    private static JButton createResetButton() {
        JButton button = new JButton("Reset ↻");
        button.addActionListener(e -> {
            if (MyFrame.uploaded_file) {
                PIC16F84.resetProgram();
            }
        });
        return button;
    }

}
