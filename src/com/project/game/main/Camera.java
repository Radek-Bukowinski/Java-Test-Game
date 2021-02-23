package com.project.game.main;

public class Camera {
    private float x;
    private float y;

    public Camera(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject gameObject){
        x += ((gameObject.getX() - x) - Game.WIDTH / 2) * 0.05f;
        y += ((gameObject.getY() - y) - Game.HEIGHT / 2) * 0.05f;
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}
