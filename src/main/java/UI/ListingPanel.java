package UI;

import javax.swing.*;
import java.awt.*;

import static UI.Checkbox.createCheckbox;

public class ListingPanel {
    private static final Dimension LISTING_PANEL = new Dimension(400, 1200);

    public static JScrollPane createListing() {
        JPanel breakpoint_panel = new JPanel();
        breakpoint_panel.setLayout(new BoxLayout(breakpoint_panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 25; i++) {
            JCheckBox checkbox = createCheckbox("Zeile: " + i);
            breakpoint_panel.add(checkbox);
        }
        breakpoint_panel.setPreferredSize(LISTING_PANEL);
        breakpoint_panel.setMaximumSize(LISTING_PANEL);
        breakpoint_panel.setMinimumSize(LISTING_PANEL);

        return new JScrollPane(
                breakpoint_panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
    }
