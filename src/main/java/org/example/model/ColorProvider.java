package org.example.model;

import java.awt.*;

public class ColorProvider {

    // Struct-like pair for gradients
    private static class ColorPair {
        final Color start, end;
        ColorPair(Color start, Color end) {
            this.start = start;
            this.end = end;
        }
    }

    // Direct index-based lookup for days (0=Sunday to 6=Saturday)
    private static final ColorPair[] DAY_GRADIENTS = {
            new ColorPair(Color.YELLOW, new Color(0, 255, 0)),               // Sunday: Yellow → Green
            new ColorPair(Color.WHITE, new Color(192, 192, 192)),            // Monday: White → Silver
            new ColorPair(Color.RED, new Color(255, 165, 0)),                // Tuesday: Red → Orange
            new ColorPair(new Color(12, 143, 12), new Color(181, 101, 29)),  // Wednesday: Green → Light Brown
            new ColorPair(Color.YELLOW, new Color(255, 215, 0)),             // Thursday: Yellow → Gold
            new ColorPair(new Color(255, 105, 180), Color.WHITE),            // Friday: Pink → White
            new ColorPair(Color.BLACK, new Color(25, 25, 112))               // Saturday: Black → Midnight Blue
    };

    public static GradientPaint getDayGradient(int width, int height, int dayIndex) { // Sunday = 0
        ColorPair pair = (dayIndex >= 0 && dayIndex < DAY_GRADIENTS.length)
                ? DAY_GRADIENTS[dayIndex-1]
                : new ColorPair(Color.WHITE, Color.GRAY); // Fallback

        return new GradientPaint(0, 0, pair.start, width, height, pair.end);
    }
}
