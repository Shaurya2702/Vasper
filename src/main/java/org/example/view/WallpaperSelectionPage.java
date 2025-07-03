package org.example.view;

import org.example.controller.MainApp;
import org.example.model.WallpaperManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class WallpaperSelectionPage extends JFrame {

    private JComboBox<String> wallpaperListComboBox;
    private JLabel previewLabel;
    private List<String> wallpaperPaths;

    public WallpaperSelectionPage() {
        ColorTheme.configureWindow(this, 800, 600); // Consistent window configuration

        // Initialize wallpaper paths
        wallpaperPaths = new ArrayList<>();
        loadWallpapers();

        // Main panel with 3D background
        JPanel mainPanel = ColorTheme.createMainPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Control buttons (Exit and Minimize) in the top-right corner
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false); // Transparent background

        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.addActionListener(e -> System.exit(0));

        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        controlPanel.add(minimizeButton);
        controlPanel.add(exitButton);
        mainPanel.add(controlPanel);

        // Dropdown to select a wallpaper
        wallpaperListComboBox = new JComboBox<>();
        mainPanel.add(wallpaperListComboBox);

        // Preview label
        previewLabel = new JLabel();
        previewLabel.setPreferredSize(new Dimension(400, 200));
        mainPanel.add(previewLabel);

        // Update the wallpaper list and preview
        updateWallpaperList();

        // Button to set wallpaper
        JButton setWallpaperButton = ColorTheme.create3DButton("Set Wallpaper");
        setWallpaperButton.addActionListener(e -> {
            try {
                String selectedWallpaper = (String) wallpaperListComboBox.getSelectedItem();
                String wallpaperPath = WallpaperManager.getWallpaperPath(selectedWallpaper);
                WallpaperManager.setWindowsWallpaper(wallpaperPath);
                JOptionPane.showMessageDialog(this, "Wallpaper set successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(setWallpaperButton);

        // Button to add a new wallpaper
        JButton addWallpaperButton = ColorTheme.create3DButton("Add Wallpaper");
        addWallpaperButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a Wallpaper Image");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png"));

            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Copy the selected file to the wallpapers folder
                    String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
                    Files.createDirectories(Paths.get(wallpapersDir)); // Create directory if it doesn't exist
                    Path targetPath = Paths.get(wallpapersDir, selectedFile.getName());
                    Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // Update the wallpaper list
                    wallpaperPaths.add(targetPath.toString());
                    updateWallpaperList();
                    JOptionPane.showMessageDialog(this, "Wallpaper added successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to add wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(addWallpaperButton);

        // Button to go back to the main page
        JButton backButton = ColorTheme.create3DButton("Back");
        backButton.addActionListener(e -> {
            MainApp mainApp = new MainApp();
            mainApp.setVisible(true);
            dispose(); // Close the current window
        });
        mainPanel.add(backButton);

        add(mainPanel);
    }

    private void loadWallpapers() {
        // Load wallpapers from the wallpapers folder
        String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
        try {
            Files.createDirectories(Paths.get(wallpapersDir)); // Create directory if it doesn't exist
            Files.list(Paths.get(wallpapersDir))
                    .filter(path -> path.toString().endsWith(".jpg") || path.toString().endsWith(".png"))
                    .forEach(path -> wallpaperPaths.add(path.toString()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateWallpaperList() {
        // Update the wallpaper list in the dropdown
        wallpaperListComboBox.removeAllItems();
        for (String path : wallpaperPaths) {
            wallpaperListComboBox.addItem(Paths.get(path).getFileName().toString());
        }
        updatePreview();
    }

    private void updatePreview() {
        // Update the preview image
        String selectedWallpaper = (String) wallpaperListComboBox.getSelectedItem();
        if (selectedWallpaper != null) {
            String wallpaperPath = WallpaperManager.getWallpaperPath(selectedWallpaper);
            ImageIcon icon = new ImageIcon(wallpaperPath);
            Image scaledImage = icon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            previewLabel.setIcon(null);
        }
    }
}