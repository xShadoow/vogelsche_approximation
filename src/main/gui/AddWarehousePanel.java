package main.gui;

import main.VogelscheApproximation;
import main.Warehouse;

import javax.swing.*;
import java.awt.*;

public class AddWarehousePanel extends JPanel {

    public static final Font TITLE_FONT = new Font("Sans Serif", Font.PLAIN, 40);
    public static final Font CONTENT_FONT = new Font("Sans Serif", Font.PLAIN, 25);

    private final VogelscheApproximation vogelscheApproximation;
    private final JPanel uiPanel;
    private final JSpinner capacitySpinner;
    private final JButton addWarehouseButton;
    private final JButton continueToStoresButton;

    public AddWarehousePanel(final VogelscheApproximation vogelscheApproximation) {
        this.vogelscheApproximation = vogelscheApproximation;
        this.setLayout(new BorderLayout(5, 5));
        JLabel title = new JLabel("Lager hinzufügen");
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
        this.uiPanel = new JPanel();
        this.uiPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel capacityLabel = new JLabel("Kapazität:");
        capacityLabel.setFont(CONTENT_FONT);
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 1);
        this.capacitySpinner = new JSpinner(spinnerNumberModel);
        this.capacitySpinner.setFont(CONTENT_FONT);
        this.uiPanel.add(capacityLabel);
        this.uiPanel.add(this.capacitySpinner);
        this.add(this.uiPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        this.addWarehouseButton = new JButton("Lager hinzufügen");
        this.addWarehouseButton.setFont(CONTENT_FONT);
        this.continueToStoresButton = new JButton("Weiter zu Filialen");
        this.continueToStoresButton.setFont(CONTENT_FONT);
        if(this.vogelscheApproximation.getWarehouses().size() == 0) {
            this.continueToStoresButton.setEnabled(false);
        }
        buttonPanel.add(this.addWarehouseButton);
        buttonPanel.add(this.continueToStoresButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.addButtonListeners();
    }

    private void addButtonListeners() {

        this.addWarehouseButton.addActionListener(e -> {
            Warehouse warehouse = new Warehouse((int) this.capacitySpinner.getValue());
            this.vogelscheApproximation.getWarehouses().add(warehouse);
            JOptionPane.showMessageDialog(this, "Lager '" + warehouse.getId() + "' mit '" + warehouse.getCapacity() + "' Kapazität hinzugefügt!\nAnzahl an hinzugefügten Lagern: " + this.vogelscheApproximation.getWarehouses().size());
            this.vogelscheApproximation.getMainFrame().setContentPane(new AddWarehousePanel(this.vogelscheApproximation));
        });

        this.continueToStoresButton.addActionListener(e -> {
            this.vogelscheApproximation.getMainFrame().setContentPane(new AddStorePanel(this.vogelscheApproximation));
        });

    }

}
