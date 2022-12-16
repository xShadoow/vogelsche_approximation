package main.gui;

import main.Store;
import main.VogelscheApproximation;
import main.Warehouse;
import main.gui.components.EffortPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddStorePanel extends JPanel {

    public static final Font TITLE_FONT = new Font("Sans Serif", Font.PLAIN, 40);
    public static final Font CONTENT_FONT = new Font("Sans Serif", Font.PLAIN, 25);

    private final VogelscheApproximation vogelscheApproximation;
    private final JPanel uiPanel;
    private final JSpinner demandSpinner;
    private final List<EffortPanel> effortPanels;
    private final JButton addStoreButton;
    private final JButton continueToCalculationButton;

    public AddStorePanel(final VogelscheApproximation vogelscheApproximation) {
        this.vogelscheApproximation = vogelscheApproximation;
        this.setLayout(new BorderLayout(5, 5));
        JLabel title = new JLabel("Filiale hinzufügen");
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
        this.uiPanel = new JPanel();
        this.uiPanel.setLayout(new GridLayout(this.vogelscheApproximation.getWarehouses().size() + 1, 1));
        JPanel demandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel demandLabel = new JLabel("Bedarf:");
        demandLabel.setFont(CONTENT_FONT);
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 1);
        this.demandSpinner = new JSpinner(spinnerNumberModel);
        this.demandSpinner.setFont(CONTENT_FONT);
        demandPanel.add(demandLabel);
        demandPanel.add(this.demandSpinner);
        this.uiPanel.add(demandPanel);
        this.effortPanels = new ArrayList<>();
        for(Warehouse warehouse : this.vogelscheApproximation.getWarehouses()) {
            EffortPanel effortPanel = new EffortPanel(warehouse);
            this.effortPanels.add(effortPanel);
            this.uiPanel.add(effortPanel);
        }
        JScrollPane scrollPane = new JScrollPane(this.uiPanel);
        this.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        this.addStoreButton = new JButton("Filiale hinzufügen");
        this.addStoreButton.setFont(CONTENT_FONT);
        this.continueToCalculationButton = new JButton("Aufwand berechnen");
        this.continueToCalculationButton.setFont(CONTENT_FONT);
        if(this.vogelscheApproximation.getStores().size() == 0) {
            this.continueToCalculationButton.setEnabled(false);
        }
        buttonPanel.add(this.addStoreButton);
        buttonPanel.add(this.continueToCalculationButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.addButtonListeners();
    }

    private void addButtonListeners() {

        this.addStoreButton.addActionListener(e -> {
            int[] effortUnits = new int[this.vogelscheApproximation.getWarehouses().size()];
            for(int i = 0; i < effortUnits.length; i++) {
                effortUnits[i] = (int) this.effortPanels.get(i).getSpinner().getValue();
            }
            Store store = new Store((int) this.demandSpinner.getValue(), effortUnits);
            this.vogelscheApproximation.getStores().add(store);
            JOptionPane.showMessageDialog(this, "Filiale '" + store.getId() + "' mit '" + store.getDemand() + "' Bedarf hinzugefügt!\nAnzahl an hinzugefügten Filialen: " + this.vogelscheApproximation.getStores().size());
            this.vogelscheApproximation.getMainFrame().setContentPane(new AddStorePanel(this.vogelscheApproximation));
        });

        this.continueToCalculationButton.addActionListener(e -> {
            this.vogelscheApproximation.openVogelscheApproximationPanel();
        });

    }

}
