package UI.NorthPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import javax.swing.*;
import java.awt.Desktop;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

public class AboutButton {
    private static final Logger log = LogManager.getLogger(AboutButton.class);

    public static JButton createAboutButton() {
        JButton aboutButton = new JButton("Help ?");
        aboutButton.setPreferredSize(new Dimension(1920/2, 40));
        aboutButton.setMaximumSize(new Dimension(1920/2, 40));
        aboutButton.setMinimumSize(new Dimension(1920/2, 40));

        aboutButton.addActionListener(e -> {
            try (InputStream in = AboutButton.class.getResourceAsStream("/PicSimu_Dokumentation.pdf")) {

                if (in == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "PDF nicht gefunden.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (!Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Desktop-Integration wird nicht unterstützt.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Path tempFile = Files.createTempFile("PicSimu_Dokumentation-", ".pdf");
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                tempFile.toFile().deleteOnExit();
                Desktop.getDesktop().open(tempFile.toFile());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "PDF konnte nicht geöffnet werden:\n" + ex.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        return aboutButton;
    }

}