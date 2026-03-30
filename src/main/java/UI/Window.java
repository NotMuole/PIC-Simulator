package UI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Window {
        private static final String TITLE = "PIC Simulator";
        private static final Dimension DEFAULT_SIZE = new Dimension(1000, 800);
        private static final String ICON_PATH = "/processor_icon.png";
        private static final Logger log = LogManager.getLogger(Window.class);

        private Window() {}

        public static void init() {
            log.info("PIC-Simulator gestartet");
            JFrame frame = createFrame();
            setIcon(frame);
            //frame.pack();
            frame.setVisible(true);
        }

        private static JFrame createFrame() {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(DEFAULT_SIZE);
            return frame;
        }

        private static void setIcon(JFrame frame) {
            URL iconUrl = Window.class.getResource(ICON_PATH);
            if (iconUrl == null) {
                log.error(ICON_PATH + "konnte nicht geladen werden");
                return;
            }
            Image icon = new ImageIcon(iconUrl).getImage();
            frame.setIconImage(icon);
        }
}