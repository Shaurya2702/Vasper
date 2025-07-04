package org.example.view;

import org.example.model.WallpaperSetter;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    public HomeView(int todayDay, int WIDTH, int HEIGHT, GradientPaint todayGradientPaint) {
        ColorTheme.configureWindow(this, 800, 600);
        JPanel mainPanel = ColorTheme.createMainPanel();

        // Top Panel (Minimize and Exit)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.setOpaque(false);
        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.addActionListener(e -> System.exit(0));
        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        topPanel.add(minimizeButton);
        topPanel.add(exitButton);

        // Center Panel (Deity Info)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        new DailyDeityDisplay(centerPanel, todayDay);

        // Bottom Panel (Buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);

        JButton solidColorButton = ColorTheme.create3DButton("Set Solid Color");
        solidColorButton.addActionListener(e -> {
            try {
                String wallpaperPath = WallpaperSetter.createSolidColorWallpaper(todayGradientPaint, WIDTH, HEIGHT);
                WallpaperSetter.setWindowsWallpaper(wallpaperPath);
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

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
