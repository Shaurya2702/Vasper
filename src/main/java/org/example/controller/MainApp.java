package org.example.controller;

import org.example.model.ColorProvider;
import org.example.model.WallpaperManager;
import org.example.view.ColorTheme;
import org.example.view.DailyDeityDisplay;
import org.example.view.WallpaperSelectionPage;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class MainApp extends JFrame {
    public MainApp() {

        // today day
        final int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        // calling the color provider class to get the today color
        final GradientPaint todayGradientPaint = ColorProvider.getDayGradient(WIDTH, HEIGHT, todayDay);
        // Add the DailyDeityDisplay to the center panel

        ColorTheme.configureWindow(this, 800, 600); // Landscape mode (width > height)




// Main panel with 3D gradient background
        JPanel mainPanel = ColorTheme.createMainPanel();


    // Top panel for control buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.setOpaque(false); // Transparent background
        // Control buttons (Exit and Minimize)
        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.addActionListener(e -> System.exit(0));

        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        topPanel.add(minimizeButton);
        topPanel.add(exitButton);


    // Center panel for deity and mantra
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false); // Transparent background


        // Bottom panel for action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 20px horizontal gap, 10px vertical gap
        bottomPanel.setOpaque(false); // Transparent background

        // Button 1: Set Solid Color
        JButton solidColorButton = ColorTheme.create3DButton("Set Solid Color");
        solidColorButton.addActionListener(e -> {
            try {
                // Getting pc width and height
                final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                final int WIDTH = screenSize.width;
                final int HEIGHT = screenSize.height;

                String wallpaperPath = WallpaperManager.createSolidColorWallpaper(todayGradientPaint, WIDTH, HEIGHT);
                WallpaperManager.setWindowsWallpaper(wallpaperPath);
                JOptionPane.showMessageDialog(this, "Solid color wallpaper set successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomPanel.add(solidColorButton);

        // Button 2: Open Wallpaper Selection Page
        JButton wallpaperButton = ColorTheme.create3DButton("Set Wallpaper");
        wallpaperButton.addActionListener(e -> {
            WallpaperSelectionPage selectionPage = new WallpaperSelectionPage();
            selectionPage.setVisible(true);
            dispose(); // Close the main window
        });
        bottomPanel.add(wallpaperButton);


// Add components to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        // add the photo, name and the mantra
        new DailyDeityDisplay(centerPanel, todayDay);

// add the main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}