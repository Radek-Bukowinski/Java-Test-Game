package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;
import com.project.game.main.HUD;
import com.project.game.main.RendererHandler;

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

    private GameObject PROJECTILE = null;
    public void getProjectile() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Projectile) {
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

            if (temporaryObject.getId() == ID.Coin || temporaryObject.getId() == ID.Crate || temporaryObject.getId() == ID.Block) {
                if (getBounds().intersects(temporaryObject.getBounds())) {
                    renderer.removeObject(this);
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
