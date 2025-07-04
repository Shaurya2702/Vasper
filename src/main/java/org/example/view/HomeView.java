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
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        String wallpaperPath = WallpaperSetter.createSolidColorWallpaper(todayGradientPaint, WIDTH, HEIGHT);
                        WallpaperSetter.setWindowsWallpaper(wallpaperPath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(HomeView.this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE));
                    }
                    return null;
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(HomeView.this, "Solid color wallpaper set successfully!");
                }
            };
            worker.execute();
        });


        JButton wallpaperButton = ColorTheme.create3DButton("Set Wallpaper");
        wallpaperButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    // Can simulate delay or loading animation if needed
                    return null;
                }

                @Override
                protected void done() {
                    WallpaperSelectionPage selectionPage = new WallpaperSelectionPage();
                    selectionPage.setVisible(true);
                    dispose(); // Close current frame safely
                }
            };
            worker.execute();   // real start of thread
        });


        bottomPanel.add(solidColorButton);
        bottomPanel.add(wallpaperButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}


/* why we use SwingWorker instead of thread
Use SwingWorker whenever your Swing UI needs to run background tasks
and then update the UI — it's safer, cleaner, and more robust than raw Thread.
 */