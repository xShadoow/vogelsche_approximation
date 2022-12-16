package main.gui.components;

import main.Warehouse;
import main.gui.AddStorePanel;

import javax.swing.*;
import java.awt.*;

public class EffortPanel extends JPanel {

    private static int CURRENT_ID = 0;

    private static int NEXT_ID() {
        return CURRENT_ID++;
    }

    private final int id;
    private final Warehouse warehouse;
    private final JSpinner spinner;

    public EffortPanel(Warehouse warehouse) {
        this.id = NEXT_ID();
        this.warehouse = warehouse;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel label = new JLabel("Lager " + this.warehouse.getId() + " Aufwand:");
        label.setFont(AddStorePanel.CONTENT_FONT);
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1);
        this.spinner = new JSpinner(spinnerNumberModel);
        this.spinner.setFont(AddStorePanel.CONTENT_FONT);
        this.add(label);
        this.add(this.spinner);
    }

    public int getId() {
        return this.id;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public JSpinner getSpinner() {
        return this.spinner;
    }

}
