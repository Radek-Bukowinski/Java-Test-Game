package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.Game;
import com.project.game.main.GameObject;
import com.project.game.main.RendererHandler;

import java.awt.*;

public class Enemy extends GameObject {
    private RendererHandler renderer;


    private GameObject PLAYER = null;
    public void getPlayer() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Player) {
                PLAYER = renderer.objects.get(i);
                break;
            }
        }
    }

    private GameObject ENEMY = null;
    public void getEnemy() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Enemy) {
                ENEMY = renderer.objects.get(i);
                break;
            }
        }
    }

    public Enemy(int x, int y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
        velocityY = 5;
        velocityX = 5;
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        if(y <= 0 || y >= Game.HEIGHT - 32) {
            velocityY *= -1;
        }
        if(x <= 0 || x >= Game.WIDTH - 16) {
            velocityX *= -1;
        }

    }

    @Override
    public void render(Graphics graphics) {
        if(ENEMY != null) {
            graphics.setColor(Color.RED);
            graphics.fillRect((int) x, (int) y, 16, 16);
        }else{
            getEnemy();
        }

    }

    @Override
    public void setTexture() {

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 16, 16);
    }
}
