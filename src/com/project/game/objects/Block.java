package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.BufferedImageLoader;
import com.project.game.main.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends GameObject {

    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
    private BufferedImage bufferedImage;


    public Block(float x, float y, int health, ID id, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
        // Get the textrure
        bufferedImage = bufferedImageLoader.loadImage("/block.png");
    }

    @Override
    public void tick() {

    }

    // Draw texture
    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(bufferedImage, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    public boolean isCollidible() {
        return isCollidible;
    }

    public void setCollidible(boolean collidible) {
        isCollidible = collidible;
    }
}
