package UI.CenterPanel;

import UI.NorthPanel.FileSelector;
import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Listing {
    private static final Logger log = LogManager.getLogger(Listing.class);
    private static final MyFileReader reader = new MyFileReader();

    public static JScrollPane createListing() {
        boolean newFileUploaded = FileSelector.getNewFileUploaded();
        if (newFileUploaded) {
            FileSelector.setNewFileUploaded(false);
            return createNewListing();
        } else {
            return createExistingListing();
        }
    }

    private static JScrollPane createNewListing() {
        String filePath = FileSelector.getFilePath();
        JPanel breakpoint_panel = reader.createFilePanel(new File(filePath));
        FileSelector.setNewFileUploaded(false);

        Dimension LISTING_PANEL = reader.getDimension();
        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        JScrollPane scrollPane = new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        return scrollPane;
    }

    public static JScrollPane createExistingListing() {
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
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            if (newValue < maxValue) {
                bar.setMaximum(maxValue);
                bar.setValue(newValue);
            }
        });

        return scrollPane;
    }
}
