package com.project.game.entity;

import com.project.game.framework.Game;
import com.project.game.framework.GameObject;
import com.project.game.framework.HUD;
import com.project.game.framework.RendererHandler;
import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject {
    private RendererHandler renderer;
    private Random random = new Random();
    private Game game;

    public Player(int x, int y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
    }

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

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        x = (int) game.constrain((int) x, 0, Game.WIDTH - 32);
        y = (int) game.constrain((int) y, 0, Game.HEIGHT - 60);

        HUD.health = health;

        collision();
        isAlive();
    }

    private void collision() {
        if(game.windowSTATE == STATE.Game) {
            if (renderer.objects.size() != 0)
                if (PLAYER != null) {
                    if (PLAYER.getBounds().intersects(ENEMY.getBounds())) {
                        PLAYER.setHealth(PLAYER.getHealth() - 2);
                    }
                } else {
                    getPlayer();
                    getEnemy();
                }
        }
    }

    private void isAlive() {
        if(PLAYER.getHealth() == 0) {
            Game.windowSTATE = STATE.Death;
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillRect((int)x, (int)y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 32);
    }
}
