package org.example.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class WallpaperGrouping {

    // Map of day to two representative gradient colors
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
            List<Color> dominantColors = extractTwoDominantColors(image);

            return findClosestDay(dominantColors);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private static List<Color> extractTwoDominantColors(BufferedImage image) {
        Map<Integer, Integer> colorCount = new HashMap<>();

        for (int y = 0; y < image.getHeight(); y += 10) {
            for (int x = 0; x < image.getWidth(); x += 10) {
                int rgb = image.getRGB(x, y);
                colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
            }
        }

        return colorCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(2)
                .map(entry -> new Color(entry.getKey()))
                .toList();
    }

    private static String findClosestDay(List<Color> dominantColors) {
        String closestDay = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<String, Color[]> entry : DAY_COLOR_MAP.entrySet()) {
            Color[] dayColors = entry.getValue();
            double dist1 = getColorDistance(dominantColors.get(0), dayColors[0]);
            double dist2 = getColorDistance(dominantColors.get(1), dayColors[1]);
            double avgDist = (dist1 + dist2) / 2;

            if (avgDist < minDistance) {
                minDistance = avgDist;
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

    // This is used in WallpaperSelectionPage to move images
    public static void groupAndMoveIfNeeded(File imageFile) throws IOException {
        String baseDir = System.getProperty("user.home") + "/Desktop/Wallpapers/";

        for (String day : DAY_COLOR_MAP.keySet()) {
            File dayFolder = new File(baseDir + day);
            if (!dayFolder.exists()) dayFolder.mkdirs();

            Path inTarget = Paths.get(dayFolder.getAbsolutePath(), imageFile.getName());
            if (Files.exists(inTarget)) return; // Skip if already copied
        }

        String day = getClosestDayForWallpaper(imageFile.getAbsolutePath());
        Path targetPath = Paths.get(baseDir + day, imageFile.getName());
        Files.copy(imageFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
