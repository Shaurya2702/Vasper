package org.example;

import org.example.controller.AppController;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppController().run());
    }
}
