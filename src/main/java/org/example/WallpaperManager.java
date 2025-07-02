/**
 *  this class is for changing the background using shell command
 */

package org.example;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
public class WallpaperManager {

    public static String createSolidColorWallpaper(GradientPaint color, int WIDTH, int HEIGHT) throws IOException {
        // Create a solid color image
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(color); // Use the Color object for simple color we use .setColor() but here we use GradientPaint
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.dispose();

        // Save the image as a PNG file
        String wallpaperPath = System.getProperty("user.home") + "\\Desktop\\day_wallpaper.png";
        File outputFile = new File(wallpaperPath);
        ImageIO.write(image, "png", outputFile);
        System.out.println("Wallpaper saved to: " + wallpaperPath);

        return wallpaperPath;
    }

    public static String getWallpaperPath(String wallpaperName) {
        // Get the full path of the wallpaper
        return System.getProperty("user.home") + "\\Desktop\\Wallpapers\\" + wallpaperName;
    }

    public static void setWindowsWallpaper(String imagePath) throws IOException, InterruptedException {
        // Path to the PowerShell script
        String scriptPath = System.getProperty("user.home") + "\\Desktop\\setWallpaper.ps1";

        // Command to execute the PowerShell script
        String command = String.format("powershell.exe -ExecutionPolicy Bypass -File \"%s\" \"%s\"",
                scriptPath, imagePath.replace("\\", "\\\\"));

        System.out.println("Executing PowerShell command: " + command); // Debug log

        // Use ProcessBuilder to execute the PowerShell command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "powershell.exe", "-ExecutionPolicy", "Bypass", "-File", scriptPath, imagePath
        );

        processBuilder.redirectErrorStream(true); // Merge stdout and stderr
        Process process = processBuilder.start();

        // Capture and log PowerShell output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("PowerShell command failed with exit code " + exitCode);
        }
    }
}