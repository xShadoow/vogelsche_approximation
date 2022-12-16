package main.gui;

import main.Store;
import main.VogelscheApproximation;
import main.Warehouse;
import main.gui.components.VogelscheApproximationTableCell;
import main.gui.components.VogelscheApproximationTableCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Enumeration;
import java.util.Map;

public class VogelscheApproximationPanel extends JPanel {

    public static final Font TITLE_FONT = new Font("Sans Serif", Font.PLAIN, 40);
    public static final Font CONTENT_FONT = new Font("Sans Serif", Font.PLAIN, 25);
    public static Font CONTENT_FONT_STRIKETHROUGH = new Font("Sans Serif", Font.PLAIN, 25);

    static {
        Map attributes = CONTENT_FONT_STRIKETHROUGH.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        CONTENT_FONT_STRIKETHROUGH = new Font(attributes);
    }

    private final VogelscheApproximation vogelscheApproximation;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JButton stepButton;
    private final JButton solveToEndButton;

    public VogelscheApproximationPanel(VogelscheApproximation vogelscheApproximation) {
        this.vogelscheApproximation = vogelscheApproximation;
        this.setLayout(new BorderLayout(5, 5));
        JLabel title = new JLabel("Vogelsche Approximation");
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);
        this.tableModel = new DefaultTableModel(this.vogelscheApproximation.getWarehouses().size()+3, this.vogelscheApproximation.getStores().size()+3) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return VogelscheApproximationTableCell.class;
            }
        };
        this.table = new JTable(this.tableModel);
        this.table.setAutoCreateColumnsFromModel(false);
        this.table.setCellSelectionEnabled(false);
        this.table.setDefaultRenderer(VogelscheApproximationTableCell.class, new VogelscheApproximationTableCellRenderer());
        this.table.setTableHeader(null);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(this.table);
        table.setFillsViewportHeight(true);
        this.add(scrollPane, BorderLayout.CENTER);
        this.solveToEndButton = new JButton("Direkte Lösung");
        this.solveToEndButton.setFont(CONTENT_FONT);
        this.stepButton = new JButton("Nächster Schritt");
        this.stepButton.setFont(CONTENT_FONT);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(this.solveToEndButton);
        buttonPanel.add(this.stepButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.fillTable();
        this.addButtonListeners();
    }

    private void addButtonListeners() {
        this.solveToEndButton.addActionListener(e -> {
            this.vogelscheApproximation.solveToEnd();
        });

        this.stepButton.addActionListener(e -> {
            this.vogelscheApproximation.step();
        });
    }

    private void fillTable() {

        this.table.setRowHeight(50);
        for(Enumeration<TableColumn> columns = this.table.getColumnModel().getColumns(); columns.hasMoreElements();) {
            columns.nextElement().setPreferredWidth(160);
        }

        int index = 1;

        for(Store store : this.vogelscheApproximation.getStores()) {
            VogelscheApproximationTableCell cell = new VogelscheApproximationTableCell("Filiale " + store.getId());
            this.tableModel.setValueAt(cell, 0, index++);
        }

        this.tableModel.setValueAt(new VogelscheApproximationTableCell("Kapazität"), 0, index++);
        this.tableModel.setValueAt(new VogelscheApproximationTableCell("Differenz"), 0, index);

        index = 1;

        for(Warehouse warehouse : this.vogelscheApproximation.getWarehouses()) {
            VogelscheApproximationTableCell cell = new VogelscheApproximationTableCell("Lager  " + warehouse.getId());
            this.tableModel.setValueAt(cell, index, 0);
            this.tableModel.setValueAt(new VogelscheApproximationTableCell(String.valueOf(warehouse.getCapacity()), ""), index++, this.vogelscheApproximation.getStores().size()+1);
        }

        this.tableModel.setValueAt(new VogelscheApproximationTableCell("Bedarf"), index++, 0);
        this.tableModel.setValueAt(new VogelscheApproximationTableCell("Differenz"), index, 0);

        index = 1;

        for(Store store : this.vogelscheApproximation.getStores()) {
            int i;
            for(i = 0; i < this.vogelscheApproximation.getWarehouses().size(); i++) {
                VogelscheApproximationTableCell cell = new VogelscheApproximationTableCell(String.valueOf(store.getEffortUnits()[i]), "");
                this.tableModel.setValueAt(cell, i+1, index);
            }
            VogelscheApproximationTableCell cell = new VogelscheApproximationTableCell(String.valueOf(store.getDemand()), "");
            this.tableModel.setValueAt(cell, ++i, index);
            index++;
        }

        this.vogelscheApproximation.setupEffortArray();

    }

    public DefaultTableModel getTableModel() {
        return this.tableModel;
    }

    public JButton getSolveToEndButton() {
        return this.solveToEndButton;
    }

    public JButton getStepButton() {
        return this.stepButton;
    }

}
