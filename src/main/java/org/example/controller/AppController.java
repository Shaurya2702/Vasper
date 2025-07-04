package org.example.controller;

import org.example.model.ColorProvider;
import org.example.model.WallpaperGrouping;
import org.example.view.HomeView;

import java.awt.*;
import java.util.Calendar;

public class AppController {

    public void run() {
        int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WIDTH = screenSize.width;
        int HEIGHT = screenSize.height;

        GradientPaint todayGradientPaint = ColorProvider.getDayGradient(WIDTH, HEIGHT, todayDay);
        HomeView homeView = new HomeView(todayDay, WIDTH, HEIGHT, todayGradientPaint);
        homeView.setVisible(true);
    }

    // Called from view to classify the wallpaper
    public String groupWallpaperByDay(String imagePath) {
        return WallpaperGrouping.getClosestDayForWallpaper(imagePath);
    }
}
