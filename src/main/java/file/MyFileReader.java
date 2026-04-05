package file;

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
    private int numberOfLines = 0;
    private static final Logger log = LogManager.getLogger(MyFileReader.class);

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
                //log.info(line);
                String adress = line.substring(0, 4).replaceAll("\\s", "");
                String code = line.substring(5, 9).replaceAll("\\s", "");
                if (!adress.isEmpty()) {
                    PIC16F84.writeProgramstore(Integer.parseInt(adress, 16), Integer.parseInt(code, 16));
                    PIC16F84.decode(Integer.parseInt(code, 16));
                }
                JCheckBox checkbox = createCheckbox(line);
                breakpoint_panel.add(checkbox);
                this.numberOfLines += 1;
            }

        } catch (IOException e) {
            log.error("Error reading file: " + e.getMessage());
        }
        return breakpoint_panel;
    }

    public Dimension getDimension() {
        return new Dimension(600, this.numberOfLines * 20);
    }

}