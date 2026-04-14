package UI.WestPanel;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ClockRate {
    private static final Logger log = LogManager.getLogger(ClockRate.class);
    private static boolean isEditable = false;

    public static JList<String> createTimeList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> TimeList = new JList<>(model);
        double timePassed = PIC16F84.getTimePassed();
        TimeList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        if (timePassed >= 1000) {
            model.addElement(String.format("%.1f ms", timePassed/1000));
        } else {
            model.addElement(String.format("%.1f µs", timePassed));
        }
        TimeList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Laufzeit"
        ));
        TimeList.setPreferredSize(new Dimension(80, 50));
        TimeList = disableSelection(TimeList);
        return TimeList;
    }

    public static JTextField createClockRateField() {
        double clockRate = PIC16F84.getClockRate();
        double microSecsPerCycle = PIC16F84.getTimePerCycleUs();

        String textField;
        if (clockRate > 10) {
            log.info("größer");
            textField = String.format("%.1fMHz (%dns)", clockRate, (int)(microSecsPerCycle*1000));
        } else {
            textField = String.format("%.1fMHz (%.1fµs)", clockRate, microSecsPerCycle);
        }
        JTextField clockRateField = new JTextField(textField);
        clockRateField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Quarzfrequenz"
        ));
        clockRateField.setPreferredSize(new Dimension(150, 50));

        if (!isEditable) {
            clockRateField.setEditable(false);
            clockRateField.setFocusable(false);
        } else {
            clockRateField.setEditable(true);
            clockRateField.setFocusable(true);
            clockRateField.addActionListener(e -> {
                try {
                    String eingabe = clockRateField.getText();
                    float Quarzfrequenz = Float.parseFloat(eingabe);
                    if (Quarzfrequenz > 0) {
                        PIC16F84.setClockRate(Quarzfrequenz);
                        clockRateField.setText(String.format("%.1fMHz (%.1fµs)", PIC16F84.getClockRate(), PIC16F84.getTimePerCycleUs()));
                    } else {
                        log.error("fehlerhafte Eingabe (Quarzfrequenz <= 0)");
                        clockRateField.setText("Fehler");
                        PIC16F84.setClockRate(4);
                    }
                } catch (Exception e2) {
                    log.error("fehlerhafte Eingabe (kein Float)");
                    clockRateField.setText("Fehler");
                    PIC16F84.setClockRate(4);
                }
            });
        }
        return clockRateField;
    }

    public static void setIsEditable(boolean value) {
        isEditable = value;
    }

    private static JList<String> disableSelection(JList<String> list) {
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { /* no-op */ }
            @Override public void addSelectionInterval(int index0, int index1) { /* no-op */ }
        });
        return list;
    }

}
