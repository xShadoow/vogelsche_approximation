package main.gui.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class VogelscheApproximationTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        VogelscheApproximationTableCell cell = (VogelscheApproximationTableCell) value;

        if(cell == null) {
            return new JLabel("");
        }

        if(cell.hasSubLabel()) {

            JPanel panel = new JPanel(new BorderLayout(2, 2));
            cell.getMainLabel().setHorizontalAlignment(SwingConstants.LEFT);
            cell.getSubLabel().setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(cell.getMainLabel(), BorderLayout.NORTH);
            panel.add(cell.getSubLabel(), BorderLayout.SOUTH);

            return panel;
        }

        return cell.getMainLabel();
    }

}
