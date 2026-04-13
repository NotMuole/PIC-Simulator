package UI.WestPanel;

import PIC.PIC16F84;
import UI.MainFrame;
import UI.NorthPanel.FileSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.time.Clock;

public class ControlPanel {
    private static final Logger log = LogManager.getLogger(ControlPanel.class);
    private static Component currentStartButton;
    private static Component currentStepButton;
    private static Component currentResetButton;
    private static Component currentTimeButton;
    private static final Dimension controlPanel = new Dimension(260, 100);
    private static boolean fileUploaded;

    public static JPanel createControlPanel() {
        fileUploaded = FileSelector.getFileUploaded();
        JPanel inner2 = new JPanel();
        if (currentStartButton == null) {
            currentStartButton = createStartButton();
            currentStepButton = createStepButton();
            currentResetButton = createResetButton();
            currentTimeButton = createSelectClockRateButton();
        }
        inner2.add(currentStartButton);
        inner2.add(currentStepButton);
        inner2.add(currentResetButton);
        inner2.add(currentTimeButton);
        inner2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Steuerpult"
        ));
        inner2.setMaximumSize(controlPanel);
        return inner2;
    }

    private static JButton createStartButton() {
        JButton button = new JButton("Start/Pause ⏯");
        button.addActionListener(e -> {
            if (fileUploaded) {
                PIC16F84.toggleIsPaused();
                Thread executionThread = new Thread(PIC16F84::runProgram);
                executionThread.start();
            }
        });
        return button;
    }

    private static JButton createStepButton() {
        JButton button = new JButton("next Step ⏭");
        button.addActionListener(e -> {
            if (PIC16F84.getIsPaused() && fileUploaded) {
                PIC16F84.stepProgram();
            }
        });
        return button;
    }

    private static JButton createResetButton() {
        JButton button = new JButton("Reset ↻");
        button.addActionListener(e -> {
            if (fileUploaded) {
                PIC16F84.setPaused(true);
                PIC16F84.resetProgram();
            }
        });
        return button;
    }

    private static JButton createSelectClockRateButton() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createMenuItem(0.5f, false));
        menu.add(createMenuItem(1f, false));
        menu.add(createMenuItem(2f, false));
        menu.add(createMenuItem(4f, false));
        menu.add(createMenuItem(8f, false));
        menu.add(createMenuItem(80f, false));
        menu.add(createMenuItem(1f, true));

        JButton button = new JButton("Set Clockrate ⏱");
        button.addActionListener(e -> menu.show(button, 0, button.getHeight()));
        return button;
    }

    private static JMenuItem createMenuItem(float value, boolean editable) {
        String text;
        if (!editable) {
            text = String.format("%.1f MHz", value);
        } else {
            text = "benutzerdefiniert";
        }

        JMenuItem element = new JMenuItem(text);
        element.addActionListener(e -> {
            ClockRate.setIsEditable(editable);
            PIC16F84.setClockRate(value);
            MainFrame.paintWestPanel();
        });

        return element;
    }
}
