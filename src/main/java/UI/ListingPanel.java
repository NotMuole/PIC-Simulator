package UI;

import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static UI.Checkbox.createCheckbox;

public class ListingPanel {
    private static final Dimension LISTING_PANEL = new Dimension(600, 700);
    private static final Logger log = LogManager.getLogger(ListingPanel.class);

    public static JScrollPane createListing() {
        MyFileReader reader = new MyFileReader();
        JPanel breakpoint_panel = reader.createFilePanel(new File("C:\\_work\\studium\\DHBW\\PIC-Simulator\\PIC-Simulator\\src\\main\\resources\\Test1.LST"));

        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        return new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
    }
