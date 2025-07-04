package org.example.model;

import java.io.File;
import java.util.*;

public class WallpaperManager {

    public static Map<String, List<String>> loadGroupedWallpapers() {
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        String baseDir = System.getProperty("user.home") + "/Desktop/Wallpapers/";

        for (String day : List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) {
            File dayFolder = new File(baseDir + day);
            if (!dayFolder.exists()) dayFolder.mkdirs();

            List<String> imagePaths = new ArrayList<>();
            File[] files = dayFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }
            grouped.put(day, imagePaths);
        }

        return grouped;
    }

    public static void ensureDayFoldersExist() {
        String baseDir = System.getProperty("user.home") + "/Desktop/Wallpapers/";
        for (String day : List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) {
            File folder = new File(baseDir + day);
            if (!folder.exists()) folder.mkdirs();
        }
    }
}
