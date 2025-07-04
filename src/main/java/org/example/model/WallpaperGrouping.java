package org.example.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class WallpaperGrouping {

    // Map of day to two representative gradient colors (start and end)
    private static final Map<String, Color[]> DAY_COLOR_MAP = Map.of(
            "Monday",    new Color[]{Color.WHITE, new Color(192, 192, 192)},
            "Tuesday",   new Color[]{Color.RED, new Color(255, 165, 0)},
            "Wednesday", new Color[]{new Color(12, 143, 12), new Color(181, 101, 29)},
            "Thursday",  new Color[]{Color.YELLOW, new Color(255, 215, 0)},
            "Friday",    new Color[]{new Color(255, 105, 180), Color.WHITE},
            "Saturday",  new Color[]{Color.BLACK, new Color(25, 25, 112)},
            "Sunday",    new Color[]{Color.YELLOW, new Color(0, 255, 0)}
    );

    public static String getClosestDayForWallpaper(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Color avgColor = getAverageColor(image);
            return findClosestDay(avgColor);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private static Color getAverageColor(BufferedImage image) {
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        int count = 0;

        for (int y = 0; y < image.getHeight(); y += 10) {
            for (int x = 0; x < image.getWidth(); x += 10) {
                Color pixel = new Color(image.getRGB(x, y));
                sumRed += pixel.getRed();
                sumGreen += pixel.getGreen();
                sumBlue += pixel.getBlue();
                count++;
            }
        }

        return new Color((int)(sumRed / count), (int)(sumGreen / count), (int)(sumBlue / count));
    }

    private static String findClosestDay(Color avgColor) {
        String closestDay = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<String, Color[]> entry : DAY_COLOR_MAP.entrySet()) {
            Color[] gradientColors = entry.getValue();
            double dist1 = getColorDistance(avgColor, gradientColors[0]);
            double dist2 = getColorDistance(avgColor, gradientColors[1]);
            double minDistForDay = Math.min(dist1, dist2);

            if (minDistForDay < minDistance) {
                minDistance = minDistForDay;
                closestDay = entry.getKey();
            }
        }

        return closestDay;
    }

    private static double getColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}
