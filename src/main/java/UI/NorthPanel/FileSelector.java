package UI.NorthPanel;

import PIC.PIC16F84;
import UI.CenterPanel.Checkbox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class FileSelector {
    private static final Logger log = LogManager.getLogger(FileSelector.class);
    private static boolean fileUploaded = false;
    private static String filePath;
    private static boolean newFileUploaded;

    public static JButton createFileUploader() {
        JButton uploadButton = new JButton("Upload file ⭱");
        uploadButton.setPreferredSize(new Dimension(1920/2, 40));
        uploadButton.setMaximumSize(new Dimension(1920/2, 40));
        uploadButton.setMinimumSize(new Dimension(1920/2, 40));
        uploadButton.addActionListener(e -> {
            JFileChooser file_upload = new JFileChooser();
            int response = file_upload.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {
                Checkbox.resetBreakpoints();
                String file_path = file_upload.getSelectedFile().getAbsolutePath();
                PIC16F84.resetProgramtore();
                fileUploaded = true;
                newFileUploaded = true;
                filePath = file_path;
                log.info("folgende Datei wurde hochgeladen: " + file_path);
                PIC16F84.resetProgram();
            }
        });
        return uploadButton;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static boolean getFileUploaded() {
        return fileUploaded;
    }

    public static boolean getNewFileUploaded() {
        return newFileUploaded;
    }

    public static void setNewFileUploaded(boolean bool) {
        newFileUploaded = bool;
    }
}