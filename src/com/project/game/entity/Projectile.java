package com.project.game.entity;
import com.project.game.framework.Game;
import com.project.game.framework.GameObject;
import com.project.game.framework.HUD;
import com.project.game.framework.RendererHandler;
import com.project.game.identifiers.ID;

import java.awt.*;

public class Projectile extends GameObject {

    private RendererHandler renderer;
    private HUD hud;

    private GameObject ENEMY = null;
    public void getEnemy() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Enemy) {
                ENEMY = renderer.objects.get(i);
                break;
            }
        }
    }

    public Projectile(float x, float y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        if(y <= 0 || y >= Game.HEIGHT - 32) {
            renderer.removeObject(this);
        }

        if(x <= 0 || x >= Game.WIDTH - 16) {
            renderer.removeObject(this);
        }

        collision();
    }

    private void collision() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            GameObject temporaryObject = renderer.objects.get(i);

            if (temporaryObject.getId() == ID.Enemy) {
                if (getBounds().intersects(temporaryObject.getBounds())) {
                    renderer.removeObject(temporaryObject);
                    hud.score += 100;
                }
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillRect((int)x, (int)y, 4, 4);
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int)x, (int)y, 16, 16);
    }
}
