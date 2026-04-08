package file;

import UI.Checkbox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import PIC.PIC16F84;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static UI.Checkbox.createCheckbox;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MyFileReader {
    private static int numberOfLines = 0;
    private static final Logger log = LogManager.getLogger(MyFileReader.class);
    private static String[] program = new String[1024];

    public JPanel createFilePanel(File file) {
        JPanel breakpoint_panel = new JPanel();
        breakpoint_panel.setLayout(new BoxLayout(breakpoint_panel, BoxLayout.Y_AXIS));

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file), Charset.forName("ISO_8859_1")
                )
            )

        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                boolean is_command;
                boolean is_background = false;
                int intAddress;
                String address = line.substring(0, 4).replaceAll("\\s", "");
                String code = line.substring(5, 9).replaceAll("\\s", "");
                if (!address.isEmpty()) {
                    PIC16F84.writeProgramstore(Integer.parseInt(address, 16), Integer.parseInt(code, 16));
                    is_command = true;
                    intAddress = Integer.parseInt(address, 16);
                    if (Integer.parseInt(address, 16) == PIC16F84.getActualProgramCounter()) {
                        is_background = true;
                    }
                } else {
                    is_command = false;
                    intAddress = -1;
                }
                JCheckBox checkbox = createCheckbox(line, is_command, is_background, intAddress);
                breakpoint_panel.add(checkbox);
                program[numberOfLines] = line;
                numberOfLines += 1;
                }
        } catch (IOException e) {
            log.error("Error reading file: " + e.getMessage());
        }
        return breakpoint_panel;
    }

    public static JPanel updateFilePanel() {
        JPanel breakpoint_panel = new JPanel();
        breakpoint_panel.setLayout(new BoxLayout(breakpoint_panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < program.length; i++) {
            if (program[i] == null) {
                continue;
            }
            boolean is_background = false;
            boolean is_command;
            int intAddress;
            String address = program[i].substring(0, 4).replaceAll("\\s", "");
            String code = program[i].substring(5, 9).replaceAll("\\s", "");
            if (!address.isEmpty()) {
                is_command = true;
                intAddress = Integer.parseInt(address, 16);
                if (Integer.parseInt(address, 16) == PIC16F84.getActualProgramCounter()) {
                    is_background = true;
                }
            } else {
                is_command = false;
                intAddress = -1;
            }
            JCheckBox checkbox = createCheckbox(program[i], is_command, is_background, intAddress);
            breakpoint_panel.add(checkbox);
        }
        return breakpoint_panel;
    }

    public Dimension getDimension () {
        return new Dimension(1000, numberOfLines * 20);
    }

    public static void resetProgram() {
        numberOfLines = 0;
        program = new String[1024];
    }
}