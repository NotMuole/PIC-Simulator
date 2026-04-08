package UI;

import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ListingPanel {
    private static final Logger log = LogManager.getLogger(ListingPanel.class);
    private static final MyFileReader reader = new MyFileReader();

    public static JScrollPane createListing(String file_path) {
        JPanel breakpoint_panel = reader.createFilePanel(new File(file_path));
        Dimension LISTING_PANEL = reader.getDimension();
        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        return new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public static JScrollPane updateListing() {
        JPanel breakpoint_panel = reader.updateFilePanel();
        Dimension LISTING_PANEL = reader.getDimension();
        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        JScrollPane scrollPane = new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        int newValue = MyFileReader.getNumberOfCurrentLine()*20-150;
        int maxValue = MyFileReader.getNumberOfLines()*20;

        SwingUtilities.invokeLater(() -> {
            //log.info("max: " + maxValue + "new: " + newValue);
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            if (newValue < maxValue) {
                bar.setMaximum(maxValue);
                bar.setValue(newValue);
            }
        });

        return scrollPane;
    }
}
