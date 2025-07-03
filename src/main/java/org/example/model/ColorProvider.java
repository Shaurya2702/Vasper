/**
 * This class provides a GradientPaint based on the Vastu-recommended colors for each day.
 */
package org.example.model;

import java.awt.GradientPaint;
import java.awt.Color;
import java.util.Calendar;
public class ColorProvider {
    public static GradientPaint getDayGradient(int width, int height, int day) {

        return switch (day) {
            case Calendar.MONDAY -> new GradientPaint(0, 0, Color.WHITE, width, height, new Color(192, 192, 192)); // White & Silver
            case Calendar.TUESDAY -> new GradientPaint(0, 0, Color.RED, width, height, new Color(255, 165, 0)); // Red & Orange
            case Calendar.WEDNESDAY -> new GradientPaint(0, 0, new Color(12, 143, 12), width, height, new Color(181, 101, 29)); // Green & Light Brown
            case Calendar.THURSDAY -> new GradientPaint(0, 0, Color.YELLOW, width, height, new Color(255, 215, 0)); // Yellow & Gold
            case Calendar.FRIDAY -> new GradientPaint(0, 0, new Color(255, 105, 180), width, height, Color.WHITE); // Pink & White
            case Calendar.SATURDAY -> new GradientPaint(0, 0, Color.BLACK, width, height, new Color(25, 25, 112)); // Black & Dark Blue
            case Calendar.SUNDAY -> new GradientPaint(0, 0, Color.YELLOW, width, height, new Color(0, 255, 0)); // Orange & Red
            default -> new GradientPaint(0, 0, Color.WHITE, width, height, Color.GRAY); // Default Gradient
        };
    }
}
