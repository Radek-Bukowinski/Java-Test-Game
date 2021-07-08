package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.BufferedImageLoader;
import com.project.game.main.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends GameObject {

    private BufferedImage bufferedImage;
    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();

    Color brown = new Color(127, 51, 0);

    public Crate(float x, float y, int health, ID id) {
        super(x, y, health, id);
        bufferedImage = bufferedImageLoader.loadImage("/crate.png");
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(brown);
        //graphics.fillRect((int) x, (int) y, 32, 32);
        graphics.drawImage(bufferedImage, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
       return new Rectangle((int) x, (int) y, 32, 32);
    }
}
