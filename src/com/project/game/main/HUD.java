package com.project.game.main;

import java.awt.*;

public class HUD {

    // Used to determine hud health colour
    private int colorValue = 255;

    // Set the values to be displayed
    public static int health = 100;
    public static int score = 0;
    static int level = 1;
    private String version = "Alpha 0.0.5";

    // Update the values
    public void tick(){
        // Make sure health doesn't go outside of its designated place, and that it doesnt't go to unrealistic values
        health = Game.constrain(health, 0, 100);
        colorValue = Game.constrain(colorValue, 0, 255);
        colorValue = health * 2;
    }

    // Draw the boxes and text required
    public void render(Graphics graphics) {
        graphics.setColor(Color.gray);
        graphics.fillRect(15,15,300,25);

        graphics.setColor(new Color(75, (int)colorValue, 0));
        graphics.fillRect(15,15, (int)health * 3, 25);

        graphics.setColor(Color.WHITE);
        graphics.drawRect(15,15,300,25);

        graphics.drawString("Health: " + health, 15, 65);

        graphics.drawString("Score: " + score, 15, 660);
        graphics.drawString("Level: " + level, 15, 675);

        graphics.drawString("Version: " + version, 1150, 675);
    }
}
