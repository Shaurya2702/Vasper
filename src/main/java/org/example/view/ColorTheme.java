package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ColorTheme {

    // Background gradient colors
    public static GradientPaint getBackgroundGradient(int width, int height) {
        return new GradientPaint(0, 0, new Color(180, 70, 118), width, height, new Color(53, 73, 149));
    }

    // Button gradient colors
    public static GradientPaint getButtonGradient(int width, int height) {
        return new GradientPaint(0, 0, new Color(255, 105, 180), width, height, new Color(147, 112, 219)); // Pink to Violet
    }

    // Text color for buttons
    public static Color getButtonTextColor() {
        return Color.WHITE;
    }

    // Method to create a consistent 3D button
    public static JButton create3DButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(getButtonGradient(getWidth(), getHeight()));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                g2d.setColor(getButtonTextColor());
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setBorderPainted(false); // Remove default border
        button.setContentAreaFilled(false); // Remove default background
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(new Dimension(300, 100)); // Button size
        return button;
    }

    // Method to create a small control button (e.g., exit, minimize)
    public static JButton createSmallControlButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(getButtonGradient(getWidth(), getHeight()));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Smaller rounded corners
                g2d.setColor(getButtonTextColor());
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setBorderPainted(false); // Remove default border
        button.setContentAreaFilled(false); // Remove default background
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(new Dimension(40, 40)); // Small button size
        return button;
    }

    // Method to create a minimize button
    public static JButton createMinimizeButton() {
        JButton button = createSmallControlButton("_");
        button.setPreferredSize(new Dimension(40, 40)); // Small button size
        return button;
    }

    // Method to create a consistent main panel with gradient background
    public static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(getBackgroundGradient(getWidth(), getHeight()));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        return mainPanel;
    }

    // Method to create a consistent window frame
    public static void configureWindow(JFrame window, int width, int height) {
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setUndecorated(true); // Remove default window decorations
        window.setShape(new RoundRectangle2D.Double(0, 0, width, height, 30, 30)); // Rounded corners
        window.setLocationRelativeTo(null); // Center the window
    }
}