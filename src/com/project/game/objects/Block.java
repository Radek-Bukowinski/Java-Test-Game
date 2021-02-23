package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block extends GameObject {

    private BufferedImage objectTexture;

    public Block(float x, float y, int health, ID id) {
        super(x, y, health, id);
        setTexture();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.lightGray);
        //graphics.fillRect((int) x, (int) y, 32, 32);
        graphics.drawImage(objectTexture, (int) x, (int) y, null);
    }

    @Override
    public void setTexture() {
        try {
            objectTexture = ImageIO.read(new File("res/block.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }
}
