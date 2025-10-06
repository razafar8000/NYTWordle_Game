package org.example;

import javax.swing.SwingUtilities;
import view.SwingWordle;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingWordle::new);
    }
}