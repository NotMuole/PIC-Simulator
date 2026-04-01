package file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static UI.Checkbox.createCheckbox;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MyFileReader {
    private static final Logger log = LogManager.getLogger(MyFileReader.class);

    public JPanel createFilePanel(File file) {
        JPanel breakpoint_panel = new JPanel();
        breakpoint_panel.setLayout(new BoxLayout(breakpoint_panel, BoxLayout.Y_AXIS));

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file), Charset.forName("ISO-8859-1")
                )
            )

        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                //log.info(line);
                JCheckBox checkbox = createCheckbox(line);
                breakpoint_panel.add(checkbox);
            }

        } catch (IOException e) {
            log.error("Error reading file: " + e.getMessage());
        }
        return breakpoint_panel;
    }
}

Int[] programmspeicher = new Int[1024];