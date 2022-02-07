package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;
import com.project.game.main.HUD;
import com.project.game.main.RendererHandler;

import java.awt.*;

public class Projectile extends GameObject {

    private RendererHandler renderer;
    private HUD hud;

    public Projectile(float x, float y, int health, ID id, RendererHandler renderer, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
        this.renderer = renderer;
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        collision();
    }

    private void collision() {
        for (int i = 0; i < renderer.enemies.size(); i++) {
            Enemy temporaryObject = (Enemy) renderer.enemies.get(i);
            // If bullet collides with an enemy
            if (getBounds().intersects(temporaryObject.getBounds())) {
                // Stop the enemy being rendered
                renderer.removeEnemy(temporaryObject);
                // Add 100 score to the player
                hud.score += 100;
            }
        }
        for (int x  = 0; x < renderer.collidibles.size(); x++) {
            GameObject temporaryObject2 = renderer.collidibles.get(x);
            // If bullet collides with a crate or a block
            if (temporaryObject2.getId() == ID.Crate || temporaryObject2.getId() == ID.Block) {
                if (getBounds().intersects(temporaryObject2.getBounds())) {
                    // Stop that bullet being rendered
                    renderer.removeObject(this);
                }
            }
        }

    }

    // No texture - render bullet as white square
    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillRect((int)x, (int)y, 4, 4);
    }


    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 16, 16);
    }
}
