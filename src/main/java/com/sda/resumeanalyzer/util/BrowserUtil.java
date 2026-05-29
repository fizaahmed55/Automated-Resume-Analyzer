package com.sda.resumeanalyzer.util;

import java.awt.Desktop;
import java.net.URI;

import javax.swing.JOptionPane;

public class BrowserUtil {

    public static void openLink(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                JOptionPane.showMessageDialog(null, "Opening browser is not supported on this system.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not open link: " + e.getMessage());
        }
    }
}