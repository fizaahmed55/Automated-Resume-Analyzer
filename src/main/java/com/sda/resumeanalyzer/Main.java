package com.sda.resumeanalyzer;

import javax.swing.SwingUtilities;
import com.sda.resumeanalyzer.view.MainFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}