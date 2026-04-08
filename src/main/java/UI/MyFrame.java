package UI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyFrame extends JFrame {
    private static final String TITLE = "PIC Simulator";
    private static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    private static final String ICON_PATH = "/processor_icon.png";
    private static final Logger log = LogManager.getLogger(MyFrame.class);
    private static JFrame frame;
    public static Component currentListing;
    private static Component currentFieldWEST;
    private static Component currentRAM;
    public static String uploaded_file_path;
    public static boolean uploaded_file = false;

    public MyFrame() {
        log.info("PIC-Simulator gestartet");
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_SIZE);
        setIcon(frame);
        frame.add(FileSelector.createFileUploader(), BorderLayout.NORTH);
        currentFieldWEST = FieldWest.createFieldWEST();
        frame.add(currentFieldWEST, BorderLayout.WEST);
        currentRAM = RAM.createFieldEAST();
        frame.add(currentRAM, BorderLayout.EAST);
        frame.setSize(DEFAULT_SIZE);
        frame.setVisible(true);
    }

    public static void createListing(String file_path) {
        uploaded_file_path = file_path;
        uploaded_file = true;
        JScrollPane new_listing = ListingPanel.createListing(file_path);
        if (currentListing != null) {
            frame.remove(currentListing);
        }
        currentListing = new_listing;
        frame.add(currentListing, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public static void updateListing() {
        JScrollPane new_listing = ListingPanel.updateListing();
        frame.remove(currentListing);
        currentListing = new_listing;
        frame.add(currentListing);
        frame.revalidate();
        frame.repaint();
    }

        private static void setIcon(JFrame frame) {
            URL iconUrl = MyFrame.class.getResource(ICON_PATH);
            if (iconUrl == null) {
                log.error(ICON_PATH + "konnte nicht geladen werden");
                return;
            }
            Image icon = new ImageIcon(iconUrl).getImage();
            frame.setIconImage(icon);
        }

        public static void updateFieldWEST() {
            frame.remove(currentFieldWEST);
            currentFieldWEST = FieldWest.createFieldWEST();
            frame.add(currentFieldWEST, BorderLayout.WEST);
            frame.revalidate();
            frame.repaint();
        }

    public static void updateFieldEAST() {
        if (currentRAM != null) {
            frame.remove(currentRAM);
        }
        currentRAM = RAM.createFieldEAST();
        frame.add(currentRAM, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }
}