package UI;

import javax.swing.*;
import java.awt.*;

public class ListingPanel {
    private static final Dimension LST_FRAME_SIZE = new Dimension(400, 1200);
    private static final Dimension LEFT_PANEL = new Dimension(400, 1200);
    private static final Dimension LISTING_SIZE = new Dimension(20, 1200);

    public static JScrollPane createListing() {

        //breakpoint_bereich (linker Bereich)
        JPanel breakpoint_panel = new JPanel();
        breakpoint_panel.setLayout(new BoxLayout(breakpoint_panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 25; i++) {
            JCheckBox checkbox = Checkbox.init();
            breakpoint_panel.add(checkbox);
        }
        breakpoint_panel.setPreferredSize(LEFT_PANEL);
        breakpoint_panel.setMaximumSize(LEFT_PANEL);
        breakpoint_panel.setMinimumSize(LEFT_PANEL);

        //text_bereich (rechter bereich)
        JTextArea lstArea = new JTextArea();
        lstArea.setEditable(false);
        lstArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        lstArea.setLayout(new BoxLayout(lstArea, BoxLayout.Y_AXIS));
        //TODO: Insert lst-file
        lstArea.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25");
        lstArea.setCaretPosition(0);
        lstArea.setBackground(Color.green);
        lstArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

        lstArea.setPreferredSize(LST_FRAME_SIZE);
        lstArea.setMaximumSize(LST_FRAME_SIZE);
        lstArea.setMinimumSize(LST_FRAME_SIZE);

        //basis (gesamtes panel mit Scrollbar)
        JPanel listing = new JPanel();
        listing.setLayout(new BoxLayout(listing, BoxLayout.X_AXIS));
        listing.add(breakpoint_panel);
        listing.add(lstArea);
        listing.setPreferredSize(LISTING_SIZE);
        listing.setMinimumSize(LISTING_SIZE);
        listing.setMaximumSize(LISTING_SIZE);
        listing.setBackground(Color.red);

        return new JScrollPane(
                listing,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
    }
