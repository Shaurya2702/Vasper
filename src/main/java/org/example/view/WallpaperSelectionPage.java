package org.example.view;

import org.example.controller.AppController;
import org.example.model.WallpaperGrouping;
import org.example.model.WallpaperSetter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class WallpaperSelectionPage extends JFrame {

    private final Map<String, List<String>> groupedWallpapers = new LinkedHashMap<>();
    private final Map<String, JPanel> dayPanels = new HashMap<>();
    private final JPanel contentPanel = new JPanel();
    private final JLabel loadingLabel = new JLabel("Loading wallpapers...");
    private String selectedWallpaperPath = null;

    public WallpaperSelectionPage() {
        ColorTheme.configureWindow(this, 1000, 700);

        JPanel mainPanel = ColorTheme.createMainPanel();
        mainPanel.setLayout(new BorderLayout());

        setupTopControls(mainPanel);
        setupDaysContent(mainPanel);
        setupFooter(mainPanel);

        add(mainPanel);
        loadWallpapersInBackground();
    }

    private void setupTopControls(JPanel parent) {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);

        JButton backButton = ColorTheme.createSmallControlButton("<");
        backButton.setToolTipText("Back to Home");
        backButton.addActionListener(e -> {
            new AppController().run();
            dispose();
        });

        JButton minimizeButton = ColorTheme.createMinimizeButton();
        minimizeButton.setToolTipText("Minimize");
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton exitButton = ColorTheme.createSmallControlButton("X");
        exitButton.setToolTipText("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(backButton);
        controlPanel.add(minimizeButton);
        controlPanel.add(exitButton);

        parent.add(controlPanel, BorderLayout.NORTH);
    }

    private void setupDaysContent(JPanel parent) {
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        parent.add(scrollPane, BorderLayout.CENTER);

        for (String day : List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) {
            JPanel dayRow = new JPanel(new BorderLayout());
            dayRow.setOpaque(false);

            JLabel dayLabel = new JLabel(day);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 18));
            dayRow.add(dayLabel, BorderLayout.WEST);

            JPanel wallpaperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            wallpaperPanel.setOpaque(false);

            JScrollPane scrollWallpapers = new JScrollPane(wallpaperPanel);
            scrollWallpapers.setPreferredSize(new Dimension(900, 120));
            scrollWallpapers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollWallpapers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollWallpapers.setOpaque(false);
            scrollWallpapers.getViewport().setOpaque(false);
            scrollWallpapers.setBorder(null);

            dayRow.add(scrollWallpapers, BorderLayout.CENTER);
            contentPanel.add(dayRow);

            groupedWallpapers.put(day, new ArrayList<>());
            dayPanels.put(day, wallpaperPanel);
        }
    }

    private void setupFooter(JPanel parent) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footer.setOpaque(false);

        JButton addButton = ColorTheme.create3DButton("Add Wallpaper");
        addButton.addActionListener(e -> addNewWallpapers());

        JButton setButton = ColorTheme.create3DButton("Set Wallpaper");
        setButton.addActionListener(e -> {
            if (selectedWallpaperPath != null) {
                try {
                    WallpaperSetter.setWindowsWallpaper(selectedWallpaperPath);
                    JOptionPane.showMessageDialog(this, "Wallpaper set successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to set wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton deleteButton = ColorTheme.create3DButton("Delete Wallpaper");
        deleteButton.addActionListener(e -> {
            if (selectedWallpaperPath != null) {
                try {
                    Files.deleteIfExists(Paths.get(selectedWallpaperPath));
                    loadWallpapersInBackground();
                    JOptionPane.showMessageDialog(this, "Wallpaper deleted successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to delete wallpaper", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        footer.add(addButton);
        footer.add(setButton);
        footer.add(deleteButton);

        parent.add(footer, BorderLayout.SOUTH);
    }

    private void loadWallpapersInBackground() {
        loadingLabel.setText("Loading wallpapers...");
        JOptionPane pane = new JOptionPane(loadingLabel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = pane.createDialog(this, "Please wait");
        dialog.setModal(false);
        dialog.setVisible(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                for (List<String> list : groupedWallpapers.values()) list.clear();
                for (JPanel panel : dayPanels.values()) panel.removeAll();

                String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
                try {
                    Files.createDirectories(Paths.get(wallpapersDir));
                    Files.list(Paths.get(wallpapersDir))
                            .filter(path -> path.toString().endsWith(".jpg") || path.toString().endsWith(".png"))
                            .forEach(path -> {
                                try {
                                    if (ImageIO.read(path.toFile()) != null) {
                                        String day = WallpaperGrouping.getClosestDayForWallpaper(path.toString());
                                        groupedWallpapers.get(day).add(path.toString());
                                    } else {
                                        System.err.println("Skipped unreadable image: " + path);
                                    }
                                } catch (IOException e) {
                                    System.err.println("Error reading image: " + path + " | " + e.getMessage());
                                }
                            });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                for (Map.Entry<String, List<String>> entry : groupedWallpapers.entrySet()) {
                    JPanel panel = dayPanels.get(entry.getKey());
                    for (String path : entry.getValue()) {
                        ImageIcon icon = new ImageIcon(path);
                        Image scaled = icon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
                        JLabel label = new JLabel(new ImageIcon(scaled));
                        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        label.setToolTipText(path);
                        label.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                selectedWallpaperPath = path;
                                JOptionPane.showMessageDialog(null, "Selected: " + path);
                            }
                        });
                        panel.add(label);
                    }
                    panel.revalidate();
                    panel.repaint();
                }
                dialog.dispose();
            }
        };
        worker.execute();
    }

    private void addNewWallpapers() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Wallpaper Images");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            try {
                String wallpapersDir = System.getProperty("user.home") + "\\Desktop\\Wallpapers";
                Files.createDirectories(Paths.get(wallpapersDir));
                for (File selected : selectedFiles) {
                    Path target = Paths.get(wallpapersDir, selected.getName());
                    Files.copy(selected.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                }
                loadWallpapersInBackground();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add wallpapers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
