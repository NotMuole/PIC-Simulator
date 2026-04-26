package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

import PIC.PIC16F84;
import UI.CenterPanel.Listing;
import UI.NorthPanel.AboutButton;
import UI.NorthPanel.FileSelector;
import UI.EastPanel.EastPanel;
import UI.NorthPanel.NorthPanel;
import UI.WestPanel.WestPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainFrame extends JFrame {
    private static final String TITLE = "PIC Simulator";
    private static final Dimension DEFAULT_SIZE = new Dimension(1000, 600);
    private static final String ICON_PATH = "/processor_icon.png";
    private static final Logger log = LogManager.getLogger(MainFrame.class);
    private static JFrame frame;
    public static Component currentListing;
    private static Component currentFieldWEST;
    private static Component currentRAM;

    public MainFrame() {
        log.info("PIC-Simulator gestartet");
        setConfigs();
        addPanels();
    }

    private static void setConfigs() {
        log.info("Konfigurationen des MainFrames geladen");
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_SIZE);
        setIcon(frame);
        frame.setSize(DEFAULT_SIZE);
        frame.setVisible(true);
    }

    private static void addPanels() {
        frame.add(NorthPanel.createNorthPanel(), BorderLayout.NORTH);
        currentFieldWEST = WestPanel.createFieldWEST();
        frame.add(currentFieldWEST, BorderLayout.WEST);
        currentRAM = EastPanel.createEastPanel();
        frame.add(currentRAM, BorderLayout.EAST);
    }

    private static void setIcon(JFrame frame) {
        URL iconUrl = MainFrame.class.getResource(ICON_PATH);
        if (iconUrl == null) {
            log.error(ICON_PATH + "konnte nicht geladen werden");
            return;
        }
        Image icon = new ImageIcon(iconUrl).getImage();
        frame.setIconImage(icon);
    }

    public static void paintListing( ) {
        JScrollPane new_listing = Listing.createListing();
        if (currentListing != null) {
            frame.remove(currentListing);
        }
        currentListing = new_listing;
        frame.add(currentListing, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public static void paintWestPanel() {
        JPanel new_panel = WestPanel.createFieldWEST();
        frame.remove(currentFieldWEST);
        currentFieldWEST = new_panel;
        frame.add(currentFieldWEST, BorderLayout.WEST);
        frame.revalidate();
        frame.repaint();
    }

    public static void paintEastPanel() {
        JPanel new_panel = EastPanel.createEastPanel();
        frame.remove(currentRAM);
        currentRAM = new_panel;
        frame.add(currentRAM, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    public static void createPopUp() {
        JDialog dialog = new JDialog(frame, "Info", true); // true = modal
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Der Watchdog-Timer hat das Limit erreicht. Das Programm wurde zurückgesetzt.");
        label.setBorder(new EmptyBorder(12, 12, 12, 12));

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            PIC16F84.resetProgram();
            dialog.dispose();
        });

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.add(label, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.add(ok);
        content.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }
}