package com.project.game.main;

import com.project.game.identifiers.ID;

import java.awt.*;

public abstract class GameObject {

    protected float x, y;
    protected float velocityX, velocityY;
    protected ID id;
    protected int health;
    protected boolean isRendered;

    public Color getObjectColor() {
        return objectColor;
    }
    public void setObjectColor(Color objectColor) {
        this.objectColor = objectColor;
    }

    protected Color objectColor;

    public GameObject(float x, float y, int health, ID id) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics graphics);

    public abstract Rectangle getBounds();

    public float getX() { return x; }
    public void setX(int x) { this.x = x; }
    public float getY() { return y; }
    public void setY(int y) { this.y = y; }

    public float getVelocityX() { return velocityX; }
    public void setVelocityX(int velocityX) { this.velocityX = velocityX; }

    public float getVelocityY() { return velocityY; }
    public void setVelocityY(int velocityY) { this.velocityY = velocityY; }

    public ID getId() { return id; }
    public void setId(ID id) { this.id = id; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
}
