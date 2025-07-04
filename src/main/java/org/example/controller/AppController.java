package org.example.controller;

import org.example.model.ColorProvider;
import org.example.model.WallpaperManager;
import org.example.view.ColorTheme;
import org.example.view.DailyDeityDisplay;
import org.example.view.WallpaperSelectionPage;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class AppController extends JFrame {

    public void run() {
        final int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int WIDTH = screenSize.width;
        final int HEIGHT = screenSize.height;

        GradientPaint todayGradientPaint = ColorProvider.getDayGradient(WIDTH, HEIGHT, todayDay);

        ColorTheme.configureWindow(this, 800, 600);

        // Main panel with gradient background
        JPanel mainPanel = ColorTheme.createMainPanel();

        // Top panel for control buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.setOpaque(false);
        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.addActionListener(e -> System.exit(0));
        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        topPanel.add(minimizeButton);
        topPanel.add(exitButton);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);

        JButton solidColorButton = ColorTheme.create3DButton("Set Solid Color");
        solidColorButton.addActionListener(e -> {
            try {
                String wallpaperPath = WallpaperManager.createSolidColorWallpaper(todayGradientPaint, WIDTH, HEIGHT);
                WallpaperManager.setWindowsWallpaper(wallpaperPath);
                JOptionPane.showMessageDialog(this, "Solid color wallpaper set successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton wallpaperButton = ColorTheme.create3DButton("Set Wallpaper");
        wallpaperButton.addActionListener(e -> {
            WallpaperSelectionPage selectionPage = new WallpaperSelectionPage();
            selectionPage.setVisible(true);
            dispose();
        });

        bottomPanel.add(solidColorButton);
        bottomPanel.add(wallpaperButton);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Display deity info
        new DailyDeityDisplay(centerPanel, todayDay);

        add(mainPanel);
        setVisible(true);
    }
}
