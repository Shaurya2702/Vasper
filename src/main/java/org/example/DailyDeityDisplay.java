package org.example;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;

import java.util.Calendar;
public class DailyDeityDisplay {

    // Constants for days, deities, and mantras
    private static final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] DEITIES_SANSKRIT = {
            "सूर्य देवः", // Lord Surya
            "शिवः एवं पार्वती देवी", // Lord Shiva & Goddess Parvati
            "हनुमान् एवं कार्तिकेयः", // Lord Hanuman & Lord Kartikeya
            "गणेशः एवं बुध देवः", // Lord Ganesha & Mercury (Budh Dev)
            "विष्णुः एवं बृहस्पतिः", // Lord Vishnu & Guru Brihaspati
            "लक्ष्मी देवी एवं दुर्गा देवी", // Goddess Lakshmi & Goddess Durga
            "शनि देवः एवं कृष्णः" // Lord Shani & Lord Krishna
    };
    private static final String[] MANTRA = {
            "ॐ घृणिः सूर्याय नमः", // Sunday
            "ॐ नमः शिवाय", // Monday
            "ॐ हं हनुमते नमः", // Tuesday
            "ॐ गं गणपतये नमः", // Wednesday
            "ॐ नमो भगवते वासुदेवाय", // Thursday
            "ॐ श्रीं महालक्ष्म्यै नमः", // Friday
            "ॐ शं शनैश्चराय नमः\nॐ कृष्णाय गोविन्दाय नमो नमः" // Saturday
    };

    // UI Components
    private JLabel deityLabel;
    private JLabel mantraLabel;
    private JLabel photoLabel;

    public DailyDeityDisplay(JPanel panel) {
        // Get the current day of the week
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0, Monday = 1, etc.

        // Get the deity, mantra, and color for the day
        String deity = DEITIES_SANSKRIT[dayOfWeek];
        String mantra = MANTRA[dayOfWeek];
        GradientPaint gradient = ColorProvider.getDayGradient(panel.getWidth(), panel.getHeight());

        // Create and configure the deity label
        deityLabel = createLabel(deity, new Font("Sanskrit Text", Font.BOLD, 28), gradient);

        // Create and configure the mantra label
        mantraLabel = createLabel("<html><center>" + mantra.replace("\n", "<br>") + "</center></html>",
                new Font("Sanskrit Text", Font.BOLD, 24), gradient);

        // Load the photo for the day
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(200, 200)); // Set preferred size for the photo
        loadPhotoForDay(dayOfWeek);

        // Create a panel to hold the image, deity name, and mantra
        JPanel contentPanel = createContentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(contentPanel, BorderLayout.CENTER); // Add content to the center of the panel
    }

    /**
     * Creates a JLabel with the specified text, font, and gradient color.
     */
    private JLabel createLabel(String text, Font font, GradientPaint gradient) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        label.setFont(font);
        label.setForeground(Color.WHITE); // Set text color to white for better visibility
        return label;
    }

    /**
     * Creates the main content panel with the image, deity name, and mantra.
     */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Transparent background

        // Add vertical glue to center the content
        contentPanel.add(Box.createVerticalGlue());

        // Add the image (centered)
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(photoLabel);

        // Add space between the image and the text
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add the deity label
        deityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(deityLabel);

        // Add space between the deity label and the mantra
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add the mantra label
        mantraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(mantraLabel);

        // Add vertical glue to center the content
        contentPanel.add(Box.createVerticalGlue());

        return contentPanel;
    }

    /**
     * Loads the photo for the given day of the week.
     */
    private void loadPhotoForDay(int dayOfWeek) {
        String imageName = DAYS[dayOfWeek].toLowerCase() + ".jpg"; // Image file name
        String imagePath = "src/main/java/org/example/images/" + imageName; // Absolute file path

        System.out.println("Loading image: " + imagePath); // Debugging statement

        // Load the image using an absolute file path
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getImage() == null) {
            System.out.println("Image not found: " + imagePath + ". Using placeholder.");
            icon = new ImageIcon("src/main/java/org/example/images/placeholder.jpg");
        }
        System.out.println("Image loaded: " + imagePath); // Debugging statement

        // Scale the image and set it to the photo label
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        photoLabel.setIcon(new ImageIcon(scaledImage));
    }
}