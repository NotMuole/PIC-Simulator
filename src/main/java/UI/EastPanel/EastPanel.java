package UI.EastPanel;

import PIC.PIC16F84;
import UI.MainFrame;
import UI.WestPanel.ClockRate;
import com.sun.tools.javac.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class EastPanel {
    private static final Logger log = LogManager.getLogger(EastPanel.class);
    private static final Dimension parentFieldDim = new Dimension(400, 500);
    private static final Dimension subFieldDim = new Dimension(374, 240);

    public static JPanel createEastPanel() {
        JPanel outer = new JPanel();
        outer.add(createSubEastPanel());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }

    private static JPanel createSubEastPanel() {
        JPanel inner1 = new JPanel(new BorderLayout());
        JTable table = createRAMPanel();
        inner1.add(table, BorderLayout.CENTER);
        inner1.add(table.getTableHeader(), BorderLayout.NORTH);
        inner1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "RAM"
        ));
        inner1.setMaximumSize(subFieldDim);
        return inner1;
    }

//    private static JTable createRAM() {
//        DefaultTableModel model = new DefaultTableModel(0, 17) {
//            @Override public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        Object header[] = {"", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//        model.addRow(header);
//        String[][] table = new String[16][17];
//        for (int i=0; i<16; i++) {
//            table[i][0] = String.format("%01X", i);
//            for (int j=1; j<17; j++) {
//                table[i][j] = String.format("%02X", PIC16F84.getVisualizedRAM(16*i+(j-1)));
//            }
//            model.addRow(table[i]);
//        }
//        JTable RAMList = new JTable(model);
//        RAMList.setTableHeader(null);
//        RAMList.setFont(new Font("Monospaced", Font.PLAIN, 10));
//        RAMList.setPreferredSize(new Dimension(subFieldDim));
//        RAMList.setRowHeight(15);
//        RAMList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        for (int c = 0; c < RAMList.getColumnCount(); c++) {
//            TableColumn col = RAMList.getColumnModel().getColumn(c);
//            col.setMinWidth(22);
//            col.setMaxWidth(22);
//            col.setPreferredWidth(22);
//        }
//        RAMList.setCellSelectionEnabled(false);
//        RAMList.setFocusable(false);
//        RAMList.setColumnSelectionAllowed(false);
//        RAMList.setRowSelectionAllowed(false);
//        return RAMList;
//    }

    private static JTable createRAMPanel() {
        String[] cols = {"", "0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col > 0; // linke Adressspalte nicht editierbar
            }
        };

        for (int r = 0; r < 16; r++) {
            Object[] row = new Object[17];
            row[0] = String.format("%X", r);
            for (int c = 1; c < 17; c++) {
                row[c] = String.format("%02X", PIC16F84.getVisualizedRAM(r * 16 + (c - 1)));
            }
            model.addRow(row);
        }

        final boolean[] internalUpdate = {false};

        model.addTableModelListener(e -> {
            if (internalUpdate[0]) return;
            if (e.getType() != javax.swing.event.TableModelEvent.UPDATE) return;

            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row < 0 || col <= 0) return;

            String text = String.valueOf(model.getValueAt(row, col)).trim();
            int addr = row * 16 + (col - 1);

            try {
                int value = Integer.parseInt(text, 16) & 0xFF;
                PIC16F84.writeRAM(addr, value);
                if (addr==2 || addr==130) {
                    PIC16F84.setProgramCounter(value);
                    log.info("set Programcounter");
                }
                MainFrame.paintWestPanel();
                String normalized = String.format("%02X", value);
                if (!normalized.equalsIgnoreCase(text)) {
                    internalUpdate[0] = true;
                    model.setValueAt(normalized, row, col);
                    internalUpdate[0] = false;
                }
            } catch (NumberFormatException ex) {
                internalUpdate[0] = true;
                model.setValueAt(String.format("%02X", PIC16F84.getVisualizedRAM(addr)), row, col);
                internalUpdate[0] = false;
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        });

        JTable RAMList = new JTable(model);
        RAMList.putClientProperty("terminateEditOnFocusLost", true);
        //RAMList.setTableHeader(null);
        RAMList.setFont(new Font("Monospaced", Font.PLAIN, 10));
        RAMList.setPreferredSize(new Dimension(subFieldDim));
        RAMList.setRowHeight(15);
        RAMList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int c = 0; c < RAMList.getColumnCount(); c++) {
            TableColumn col = RAMList.getColumnModel().getColumn(c);
            col.setMinWidth(22);
            col.setMaxWidth(22);
            col.setPreferredWidth(22);
        }
        RAMList.setCellSelectionEnabled(false);
        RAMList.setFocusable(false);
        RAMList.setColumnSelectionAllowed(false);
        RAMList.setRowSelectionAllowed(false);
        return RAMList;
    }

}
