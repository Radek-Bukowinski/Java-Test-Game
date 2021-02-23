package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;

import java.awt.*;

public class Block extends GameObject {

    public Block(float x, float y, int health, ID id) {
        super(x, y, health, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.lightGray);
        graphics.fillRect((int) x, (int) y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }
}
