package main.gui.components;

import main.gui.VogelscheApproximationPanel;

import javax.swing.*;

public class VogelscheApproximationTableCell {

    private final JLabel mainLabel;
    private final JLabel subLabel;

    public VogelscheApproximationTableCell(String mainLabel) {
        if(mainLabel.startsWith("§")) {
            this.mainLabel = new JLabel(" " + mainLabel.replace("§", "") + " ");
            this.mainLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT_STRIKETHROUGH);
        } else {
            this.mainLabel = new JLabel(" " + mainLabel + " ");
            this.mainLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT);
        }
        this.subLabel = null;
    }

    public VogelscheApproximationTableCell(String mainLabel, String subLabel) {
        if(mainLabel.startsWith("§")) {
            this.mainLabel = new JLabel(" " + mainLabel.replace("§", "") + " ");
            this.mainLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT_STRIKETHROUGH);
        } else {
            this.mainLabel = new JLabel(" " + mainLabel + " ");
            this.mainLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT);
        }
        if(subLabel.startsWith("§")) {
            this.subLabel = new JLabel(" " + subLabel.replace("§", "") + " ");
            this.subLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT_STRIKETHROUGH);
        } else {
            this.subLabel = new JLabel(" " + subLabel + " ");
            this.subLabel.setFont(VogelscheApproximationPanel.CONTENT_FONT);
        }
    }

    public boolean hasSubLabel() {
        return this.subLabel != null;
    }

    public JLabel getMainLabel() {
        return this.mainLabel;
    }

    public JLabel getSubLabel() {
        return this.subLabel;
    }

}
