package UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileSelector {
    private static final Logger log = LogManager.getLogger(FileSelector.class);
    private static final Dimension BUTTON = new Dimension(50, 20);

    public static JButton createFileUploader() {
        JButton upload_button = new JButton("Upload file");
        upload_button.addActionListener(e -> {
            JFileChooser file_upload = new JFileChooser();
            int response = file_upload.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {
                String file_path = file_upload.getSelectedFile().getAbsolutePath();
                log.info("FileSelector: " + file_path);
                MyFrame.setListing(file_path);
            }
        });
        return upload_button;
    }

}
