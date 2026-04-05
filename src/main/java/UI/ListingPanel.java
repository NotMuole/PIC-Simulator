package UI;

import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ListingPanel {
    private static final Logger log = LogManager.getLogger(ListingPanel.class);

    public static JScrollPane createListing(String file_path) {
        MyFileReader reader = new MyFileReader();
        JPanel breakpoint_panel = reader.createFilePanel(new File(file_path));
        Dimension LISTING_PANEL = reader.getDimension();
        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        return new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
    }
