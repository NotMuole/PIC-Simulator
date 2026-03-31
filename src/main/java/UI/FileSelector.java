package UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class FileSelector {
    private static final Logger log = LogManager.getLogger(FileSelector.class);

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
