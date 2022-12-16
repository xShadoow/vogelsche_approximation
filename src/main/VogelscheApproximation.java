package main;

import main.gui.AddWarehousePanel;
import main.gui.MainFrame;
import main.gui.VogelscheApproximationPanel;
import main.gui.components.VogelscheApproximationTableCell;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VogelscheApproximation {

    private final List<Warehouse> warehouses;
    private final List<Store> stores;
    private MainFrame mainFrame;
    private VogelscheApproximationPanel vogelscheApproximationPanel;
    private int rows;
    private int cols;
    private int[][] effortArray;
    private int[] rowDiffs;
    private int[] colDiffs;
    private int totalEffort = 0;
    private String totalEffortEquation = "";
    private int currentStep = 0;

    public VogelscheApproximation() {
        this.warehouses = new ArrayList<>();
        this.stores = new ArrayList<>();
    }

    public void init() {
        this.openGui();
    }

    private void openGui() {
        this.mainFrame = new MainFrame();
        this.mainFrame.setContentPane(new AddWarehousePanel(this));
    }

    public void openVogelscheApproximationPanel() {
        this.vogelscheApproximationPanel = new VogelscheApproximationPanel(this);
        this.mainFrame.setContentPane(this.vogelscheApproximationPanel);
    }

    public void setupEffortArray() {
        this.rows = this.warehouses.size();
        this.cols = this.stores.size();
        this.effortArray = new int[cols][rows];
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                this.effortArray[i][j] = this.stores.get(i).getEffortUnits()[j];
            }
        }
    }

    public void step() {

        if(this.currentStep == 0) {
            this.step1();
            currentStep = 1;
        } else if(this.currentStep == 1) {
            this.step2();
            this.currentStep = 0;
            if(this.isDone()) {
                this.end();
            }
        }

    }

    public void solveToEnd() {
        while(!this.isDone()) {
            this.step();
        }
    }

    private void end() {
        for(int i = 0; i < this.cols; i++) {
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("/"), rows+2, i+1);
        }
        for(int i = 0; i < this.rows; i++) {
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("/"), i+1, cols+2);
        }
        this.vogelscheApproximationPanel.getSolveToEndButton().setEnabled(false);
        this.vogelscheApproximationPanel.getStepButton().setEnabled(false);
        JOptionPane.showMessageDialog(this.vogelscheApproximationPanel, "Fertig!\nGesamtaufwand: " + this.totalEffort + "\nRechnung = " + this.totalEffortEquation.replaceFirst(" + ", ""));
    }

    //step 1
    private void step1() {

        this.rowDiffs = new int[rows];
        this.colDiffs = new int[cols];

        //rows
        for(int i = 0; i < rows; i++) {
            int[] twoSmallest = this.getTwoSmallestValuesInRow(i);
            int diff = twoSmallest[1] - twoSmallest[0];
            if(twoSmallest[0] == -1 && twoSmallest[1] == -1) {
                diff = -1;
            } else if(twoSmallest[0] != -1 && twoSmallest[1] == -1) {
                diff = twoSmallest[0];
            }
            this.rowDiffs[i] = diff;
            //update ui
            String diffString = String.valueOf(diff);
            if(diff == -1) {
                diffString = "/";
            }
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(diffString), i+1, cols+2);
        }

        //cols
        for(int i = 0; i < cols; i++) {
            int[] twoSmallest = this.getTwoSmallestValuesInColumn(i);
            int diff = twoSmallest[1] - twoSmallest[0];
            if(twoSmallest[0] == -1 && twoSmallest[1] == -1) {
                diff = -1;
            } else if(twoSmallest[0] != -1 && twoSmallest[1] == -1) {
                diff = twoSmallest[0];
            }
            this.colDiffs[i] = diff;
            //update ui
            String diffString = String.valueOf(diff);
            if(diff == -1) {
                diffString = "/";
            }
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(diffString), rows+2, i+1);
        }

    }

    private int[] getTwoSmallestValuesInRow(int row) {

        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;
        boolean changeInSmallest = false;
        boolean changeInSecondSmallest = false;

        for(int i = 0; i < cols; i++) {
            int val = this.effortArray[i][row];
            if(val < smallest && val != -1) {
                smallest = val;
                changeInSmallest = true;
            }
        }

        for(int i = 0; i < cols; i++) {
            int val = this.effortArray[i][row];
            if(val < secondSmallest && val > smallest && val != -1) {
                secondSmallest = val;
                changeInSecondSmallest = true;
            }
        }

        if(!changeInSmallest) {
            return new int[] {-1, -1};
        }

        if(!changeInSecondSmallest) {
            return new int[] {smallest, -1};
        }

        return new int[] {smallest, secondSmallest};
    }

    private int[] getTwoSmallestValuesInColumn(int col) {

        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;
        boolean changeInSmallest = false;
        boolean changeInSecondSmallest = false;

        for(int i = 0; i < rows; i++) {
            int val = this.effortArray[col][i];
            if(val < smallest && val != -1) {
                smallest = val;
                changeInSmallest = true;
            }
        }

        for(int i = 0; i < rows; i++) {
            int val = this.effortArray[col][i];
            if(val < secondSmallest && val > smallest && val != -1) {
                secondSmallest = val;
                changeInSecondSmallest = true;
            }
        }

        if(!changeInSmallest) {
            return new int[] {-1, -1};
        }

        if(!changeInSecondSmallest) {
            return new int[] {smallest, -1};
        }

        return new int[] {smallest, secondSmallest};
    }

    //step 2
    private void step2() {

        int indexHighestDiffRows = -1;
        int indexHighestDiffCols = -1;
        int highestDiffRows = -1;
        int highestDiffCols = -1;

        for(int i = 0; i < rows; i++) {
            if(this.rowDiffs[i] > highestDiffRows) {
                highestDiffRows = this.rowDiffs[i];
                indexHighestDiffRows = i;
            }
        }

        for(int i = 0; i < cols; i++) {
            if(this.colDiffs[i] > highestDiffCols) {
                highestDiffCols = this.colDiffs[i];
                indexHighestDiffCols = i;
            }
        }

        //row diff is higher
        if(highestDiffRows > highestDiffCols) {

            int indexSmallestValue = this.getSmallestRowValueIndex(indexHighestDiffRows);
            int effort = this.effortArray[indexSmallestValue][indexHighestDiffRows];
            Warehouse warehouse = this.warehouses.get(indexHighestDiffRows);
            Store store = this.stores.get(indexSmallestValue);
            int availableCapacity = warehouse.getCapacity();
            int neededDemand = store.getDemand();
            int newCapacity;
            int newDemand;

            if(availableCapacity >= neededDemand) {
                newCapacity = availableCapacity - neededDemand;
                newDemand = 0;
                this.totalEffort += effort * neededDemand;
                this.totalEffortEquation += " + " + effort + "*" + neededDemand;
                //remove field from calculation
                //this.effortArray[indexSmallestValue][indexHighestDiffRows] = -1;

                //update ui field
                VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexHighestDiffRows+1, indexSmallestValue+1);
                int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(String.valueOf(oldVal), String.valueOf(neededDemand)), indexHighestDiffRows+1, indexSmallestValue+1);
            } else {
                newCapacity = 0;
                newDemand = neededDemand - availableCapacity;
                this.totalEffort += effort * availableCapacity;
                this.totalEffortEquation += " + " + effort + "*" + availableCapacity;

                //update ui field
                VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexHighestDiffRows+1, indexSmallestValue+1);
                int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                int oldAvailCapacity;
                try {
                    oldAvailCapacity = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                } catch (NumberFormatException exception) {
                    oldAvailCapacity = 0;
                }
                this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(String.valueOf(oldVal), String.valueOf(oldAvailCapacity + availableCapacity)), indexHighestDiffRows+1, indexSmallestValue+1);
            }

            warehouse.setCapacity(newCapacity);
            store.setDemand(newDemand);

            if(warehouse.getCapacity() == 0) {
                for(int i = 0; i < this.cols; i++) {
                    this.effortArray[i][indexHighestDiffRows] = -1;
                    //update ui
                    if(i != indexSmallestValue) {
                        VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexHighestDiffRows+1, i+1);
                        int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                        int oldSubVal;
                        try {
                            oldSubVal = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                        } catch (NumberFormatException exception) {
                            oldSubVal = 0;
                        }
                        if(oldSubVal == 0) {
                            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, ""), indexHighestDiffRows + 1, i + 1);
                        }
                    }
                }
            }

            if(store.getDemand() == 0) {
                for(int i = 0; i < this.rows; i++) {
                    this.effortArray[indexSmallestValue][i] = -1;
                    //update ui
                    if(i != indexHighestDiffRows) {
                        VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(i + 1, indexSmallestValue + 1);
                        int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                        int oldSubVal;
                        try {
                            oldSubVal = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                        } catch (NumberFormatException exception) {
                            oldSubVal = 0;
                        }
                        if(oldSubVal == 0) {
                            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, ""), i + 1, indexSmallestValue + 1);
                        }
                    }
                }
            }

            //update capacity on ui
            VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexHighestDiffRows+1, this.cols+1);
            int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, String.valueOf(newCapacity)), indexHighestDiffRows+1, this.cols+1);

            //update demand on ui
            oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(this.rows+1, indexSmallestValue+1);
            oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, String.valueOf(newDemand)), this.rows+1, indexSmallestValue+1);

        } else {

            int indexSmallestValue = this.getSmallestColumnValueIndex(indexHighestDiffCols);
            int effort = this.effortArray[indexHighestDiffCols][indexSmallestValue];
            Warehouse warehouse = this.warehouses.get(indexSmallestValue);
            Store store = this.stores.get(indexHighestDiffCols);
            int availableCapacity = warehouse.getCapacity();
            int neededDemand = store.getDemand();
            int newCapacity;
            int newDemand;

            if(availableCapacity >= neededDemand) {
                newCapacity = availableCapacity - neededDemand;
                newDemand = 0;
                this.totalEffort += effort * neededDemand;
                this.totalEffortEquation += " + " + effort + "*" + neededDemand;
                //remove field from calculation
                //this.effortArray[indexHighestDiffCols][indexSmallestValue] = -1;

                //update ui field
                VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexSmallestValue+1, indexHighestDiffCols+1);
                int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(String.valueOf(oldVal), String.valueOf(neededDemand)), indexSmallestValue+1, indexHighestDiffCols+1);
            } else {
                newCapacity = 0;
                newDemand = neededDemand - availableCapacity;
                this.totalEffort += effort * availableCapacity;
                this.totalEffortEquation += " + " + effort + "*" + availableCapacity;

                //update ui field
                VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexSmallestValue+1, indexHighestDiffCols+1);
                int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                int oldAvailCapacity;
                try {
                    oldAvailCapacity = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                } catch (NumberFormatException exception) {
                    oldAvailCapacity = 0;
                }
                this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell(String.valueOf(oldVal), String.valueOf(oldAvailCapacity + availableCapacity)), indexSmallestValue+1, indexHighestDiffCols+1);
            }

            warehouse.setCapacity(newCapacity);
            store.setDemand(newDemand);

            if(warehouse.getCapacity() == 0) {
                for(int i = 0; i < this.cols; i++) {
                    this.effortArray[i][indexSmallestValue] = -1;

                    //update ui
                    if(i != indexHighestDiffCols) {
                        VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexSmallestValue+1, i+1);
                        int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                        int oldSubVal;
                        try {
                            oldSubVal = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                        } catch (NumberFormatException exception) {
                            oldSubVal = 0;
                        }
                        if(oldSubVal == 0) {
                            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, ""), indexSmallestValue + 1, i + 1);
                        }
                    }
                }
            }

            if(store.getDemand() == 0) {
                for(int i = 0; i < this.rows; i++) {
                    this.effortArray[indexHighestDiffCols][i] = -1;

                    //update ui
                    if(i != indexSmallestValue) {
                        VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(i+1, indexHighestDiffCols+1);
                        int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
                        int oldSubVal;
                        try {
                            oldSubVal = Integer.parseInt(oldCell.getSubLabel().getText().replaceAll(" ", ""));
                        } catch (NumberFormatException exception) {
                            oldSubVal = 0;
                        }
                        if(oldSubVal == 0) {
                            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, ""), i + 1, indexHighestDiffCols + 1);
                        }
                    }
                }
            }

            //update capacity on ui
            VogelscheApproximationTableCell oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(indexSmallestValue+1, this.cols+1);
            int oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, String.valueOf(newCapacity)), indexSmallestValue+1, this.cols+1);

            //update demand on ui
            oldCell = (VogelscheApproximationTableCell) this.vogelscheApproximationPanel.getTableModel().getValueAt(this.rows+1, indexHighestDiffCols+1);
            oldVal = Integer.parseInt(oldCell.getMainLabel().getText().replaceAll(" ", ""));
            this.vogelscheApproximationPanel.getTableModel().setValueAt(new VogelscheApproximationTableCell("§" + oldVal, String.valueOf(newDemand)), this.rows+1, indexHighestDiffCols+1);

        }

    }

    private int getSmallestRowValueIndex(int row) {

        int smallest = Integer.MAX_VALUE;
        int smallestIndex = -1;

        for(int i = 0; i < this.cols; i++) {
            int val = this.effortArray[i][row];
            if(val < smallest && val != -1) {
                smallest = val;
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

    private int getSmallestColumnValueIndex(int col) {

        int smallest = Integer.MAX_VALUE;
        int smallestIndex = -1;

        for(int i = 0; i < this.rows; i++) {
            int val = this.effortArray[col][i];
            if(val < smallest && val != -1) {
                smallest = val;
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

    private boolean isDone() {
        boolean done = true;
        for(Store store : this.stores) {
            if(store.getDemand() != 0) {
                done = false;
                break;
            }
        }
        return done;
    }

    public List<Warehouse> getWarehouses() {
        return this.warehouses;
    }

    public List<Store> getStores() {
        return this.stores;
    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

}
