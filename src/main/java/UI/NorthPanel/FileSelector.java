package UI.NorthPanel;

import PIC.PIC16F84;
import UI.CenterPanel.Checkbox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class FileSelector {
    private static final Logger log = LogManager.getLogger(FileSelector.class);
    private static boolean fileUploaded = false;
    private static String filePath;
    private static boolean newFileUploaded;

    public static JButton createFileUploader() {
        JButton upload_button = new JButton("Upload file");
        upload_button.addActionListener(e -> {
            JFileChooser file_upload = new JFileChooser();
            int response = file_upload.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {
                Checkbox.resetBreakpoints();
                String file_path = file_upload.getSelectedFile().getAbsolutePath();
                fileUploaded = true;
                newFileUploaded = true;
                filePath = file_path;
                log.info("folgende Datei wurde hochgeladen: " + file_path);
                PIC16F84.resetProgramtore();
                PIC16F84.resetProgram();
            }
        });
        return upload_button;
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