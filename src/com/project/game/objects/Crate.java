package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Crate extends GameObject {

    private BufferedImage objectTexture;

    Color brown = new Color(127, 51, 0);

    public Crate(float x, float y, int health, ID id) {
        super(x, y, health, id);
        setTexture();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(brown);
        //graphics.fillRect((int) x, (int) y, 32, 32);
        graphics.drawImage(objectTexture, (int) x, (int) y, null);
    }

    @Override
    public void setTexture() {
        try {
            objectTexture = ImageIO.read(new File("res/crate.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public Rectangle getBounds() {
       return new Rectangle((int) x, (int) y, 32, 32);
    }
}
