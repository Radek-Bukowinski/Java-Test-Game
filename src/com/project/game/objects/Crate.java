package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.BufferedImageLoader;
import com.project.game.main.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends GameObject {

    private BufferedImage bufferedImage;
    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();

    public Crate(float x, float y, int health, ID id, boolean isCollidible) {
        super(x, y, health, id, isCollidible);

        // Get the texture
        bufferedImage = bufferedImageLoader.loadImage("/crate.png");
    }

    @Override
    public void tick() {

    }

    //Draw the texture
    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(bufferedImage, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
       return new Rectangle((int) x, (int) y, 32, 32);
    }
}
