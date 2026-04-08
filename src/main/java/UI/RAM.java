package UI;

import PIC.PIC16F84;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

public class RAM {
    private static final Logger log = LogManager.getLogger(RAM.class);
    private static final Dimension parentFieldDim = new Dimension(400, 500);
    private static final Dimension subFieldDim = new Dimension(374, 255);

    public static JPanel createFieldEAST() {
        JPanel outer = new JPanel();
        outer.add(createSubFieldEAST());
        outer.setPreferredSize(parentFieldDim);
        return outer;
    }

    private static JPanel createSubFieldEAST() {
        JPanel inner1 = new JPanel(new BorderLayout());
        JTable table = createRAM();
        inner1.add(table, BorderLayout.CENTER);
        inner1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "RAM"
        ));
        inner1.setMaximumSize(subFieldDim);
        return inner1;
    }

    private static JTable createRAM() {
        DefaultTableModel model = new DefaultTableModel(0, 17) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object header[] = {"", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        model.addRow(header);
        String[][] table = new String[16][17];
        for (int i=0; i<16; i++) {
            table[i][0] = String.format("%01X", i);
           for (int j=1; j<17; j++) {
               table[i][j] = String.format("%02X", PIC16F84.getRAM(16*i+(j-1)));
           }
            model.addRow(table[i]);
        }
        JTable RAMList = new JTable(model);
        RAMList.setTableHeader(null);
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
