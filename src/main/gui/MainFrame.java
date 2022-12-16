package main.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 500;
    private final JFrame frame;
    private final JPanel content;

    public MainFrame() {
        this.frame = new JFrame();
        this.frame.setTitle("Vogelsche Approximation");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.frame.setLocationRelativeTo(null);
        this.content = new JPanel(new BorderLayout());
        this.frame.setContentPane(this.content);
        this.frame.setVisible(true);
    }

    public void setContentPane(JPanel contentPane) {
        this.content.removeAll();
        this.content.add(contentPane, BorderLayout.CENTER);
        this.updateFrame();
    }

    public void updateFrame() {
        this.content.updateUI();
    }

    public JFrame getFrame() {
        return this.frame;
    }

}
