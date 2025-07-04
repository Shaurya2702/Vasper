package org.example.view;

import org.example.controller.AppController;
import org.example.model.WallpaperManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class WallpaperSelectionPage extends JFrame {

    private final JComboBox<String> wallpaperListComboBox = new JComboBox<>();
    private final JLabel previewLabel = new JLabel();
    private final List<String> wallpaperPaths = new ArrayList<>();

    public WallpaperSelectionPage() {
        ColorTheme.configureWindow(this, 800, 600);

        JPanel mainPanel = ColorTheme.createMainPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        setupTopControls(mainPanel);
        setupWallpaperDropdown(mainPanel);
        setupPreview(mainPanel);
        setupButtons(mainPanel);

        loadWallpapers();
        updateWallpaperList();

        add(mainPanel);
    }

    private void setupTopControls(JPanel parent) {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);

        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(minimizeButton);
        controlPanel.add(exitButton);

        parent.add(controlPanel);
    }

    private void setupWallpaperDropdown(JPanel parent) {
        wallpaperListComboBox.setPreferredSize(new Dimension(400, 30));
        wallpaperListComboBox.addActionListener(e -> updatePreview());

        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setOpaque(false);
        dropdownPanel.add(wallpaperListComboBox);

        parent.add(dropdownPanel);
    }

    private void setupPreview(JPanel parent) {
        previewLabel.setPreferredSize(new Dimension(400, 200));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel previewPanel = new JPanel();
        previewPanel.setOpaque(false);
        previewPanel.add(previewLabel);

        parent.add(previewPanel);
    }

    private void setupButtons(JPanel parent) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton setButton = ColorTheme.create3DButton("Set Wallpaper");
        setButton.addActionListener(e -> setSelectedWallpaper());

        JButton addButton = ColorTheme.create3DButton("Add Wallpaper");
        addButton.addActionListener(e -> addNewWallpaper());

        JButton backButton = ColorTheme.create3DButton("Back");
        backButton.addActionListener(e -> {
            new AppController().run(); // Return to home view
            dispose();
        });

        buttonPanel.add(setButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        parent.add(buttonPanel);
    }

    private void loadWallpapers() {
        String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
        try {
            Files.createDirectories(Paths.get(wallpapersDir));
            Files.list(Paths.get(wallpapersDir))
                    .filter(path -> path.toString().endsWith(".jpg") || path.toString().endsWith(".png"))
                    .forEach(path -> wallpaperPaths.add(path.toString()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateWallpaperList() {
        wallpaperListComboBox.removeAllItems();
        for (String path : wallpaperPaths) {
            wallpaperListComboBox.addItem(Paths.get(path).getFileName().toString());
        }
        updatePreview();
    }

    private void updatePreview() {
        String selected = (String) wallpaperListComboBox.getSelectedItem();
        if (selected != null) {
            String fullPath = WallpaperManager.getWallpaperPath(selected);
            ImageIcon icon = new ImageIcon(fullPath);
            Image scaled = icon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(scaled));
        } else {
            previewLabel.setIcon(null);
        }
    }

    private void setSelectedWallpaper() {
        String selected = (String) wallpaperListComboBox.getSelectedItem();
        if (selected != null) {
            try {
                String fullPath = WallpaperManager.getWallpaperPath(selected);
                WallpaperManager.setWindowsWallpaper(fullPath);
                JOptionPane.showMessageDialog(this, "Wallpaper set successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNewWallpaper() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Wallpaper Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            try {
                String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
                Path target = Paths.get(wallpapersDir, selected.getName());
                Files.copy(selected.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                wallpaperPaths.add(target.toString());
                updateWallpaperList();

                // Classify and show matched day
                AppController controller = new AppController();
                String matchedDay = controller.groupWallpaperByDay(target.toString());

                JOptionPane.showMessageDialog(this,
                        "Wallpaper added and grouped under: " + matchedDay,
                        "Wallpaper Grouped",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
