package com.project.game.main;

import java.awt.*;

public class HUD {
    public static int health = 100;
    private int colorValue = 255;
    public static int score = 0;
    static int level = 1;
    private String version = "Alpha 0.0.5";

    public void tick(){
        health = Game.constrain(health, 0, 100);
        colorValue = Game.constrain(colorValue, 0, 255);
        colorValue = health * 2;
    }

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
