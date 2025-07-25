package org.example.view;

import org.example.model.ColorProvider;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DailyDeityDisplay {

    private static final int IMAGE_SIZE = 200;

    // Consolidated Day Information using a data structure
    private static class DayInfo {
        final String deity;
        final String mantra;
        final String imageFile;

        DayInfo(String deity, String mantra, String imageFile) {
            this.deity = deity;
            this.mantra = mantra;
            this.imageFile = imageFile;
        }
    }

    private static final Map<Integer, DayInfo> DAY_INFO_MAP = Map.of(
            Calendar.SUNDAY,    new DayInfo("सूर्य देवः", "ॐ घृणिः सूर्याय नमः", "sunday.jpg"),
            Calendar.MONDAY,    new DayInfo("शिवः एवं पार्वती देवी", "ॐ नमः शिवाय", "monday.jpg"),
            Calendar.TUESDAY,   new DayInfo("हनुमान् एवं कार्तिकेयः", "ॐ हं हनुमते नमः", "tuesday.jpg"),
            Calendar.WEDNESDAY, new DayInfo("गणेशः एवं बुध देवः", "ॐ गं गणपतये नमः", "wednesday.jpg"),
            Calendar.THURSDAY,  new DayInfo("विष्णुः एवं बृहस्पतिः", "ॐ नमो भगवते वासुदेवाय", "thursday.jpg"),
            Calendar.FRIDAY,    new DayInfo("लक्ष्मी देवी एवं दुर्गा देवी", "ॐ श्रीं महालक्ष्म्यै नमः", "friday.jpg"),
            Calendar.SATURDAY,  new DayInfo("शनि देवः एवं कृष्णः", "ॐ शं शनैश्चराय नमः\nॐ कृष्णाय गोविन्दाय नमो नमः", "saturday.jpg")
    );

    private final JLabel deityLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel mantraLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel photoLabel = new JLabel();

    public DailyDeityDisplay(JPanel panel, int todayDay) {

        DayInfo info = DAY_INFO_MAP.getOrDefault(todayDay, new DayInfo("देवता", "ॐ", "placeholder.jpg"));
        GradientPaint gradient = ColorProvider.getDayGradient(panel.getWidth(), panel.getHeight(), todayDay);

        setupImage(info.imageFile);

        setupLabel(deityLabel, info.deity, new Font("Sanskrit Text", Font.BOLD, 28), gradient);
        setupLabel(mantraLabel, "<html><center>" + info.mantra.replace("\n", "<br>") + "</center></html>",
                new Font("Sanskrit Text", Font.BOLD, 24), gradient);

// adding all the image, test(label) on the panel
        setupPanel(panel);
    }

    private void setupImage(String fileName) {
        String resourcePath = "/DegnityImagesAccordingToDay/" + fileName;
        java.net.URL imageUrl = getClass().getResource(resourcePath);
        ImageIcon icon = new ImageIcon(imageUrl);
        Image img = icon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
        photoLabel.setIcon(new ImageIcon(img));
        photoLabel.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void setupLabel(JLabel label, String text, Font font, GradientPaint gradient) {
        label.setFont(font);
        label.setForeground(Color.BLACK);
        label.setText(text);
        label.setOpaque(false);
    }

    private void setupPanel(JPanel panel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(photoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(centerAlign(deityLabel));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(centerAlign(mantraLabel));
        contentPanel.add(Box.createVerticalGlue());

        panel.setLayout(new BorderLayout());
        panel.add(contentPanel, BorderLayout.CENTER);
    }

    private Component centerAlign(JComponent comp) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        return comp;
    }
}
