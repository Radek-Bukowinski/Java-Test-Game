package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;
import com.project.game.main.HUD;
import com.project.game.main.RendererHandler;

import java.awt.*;

public class Projectile extends GameObject {

    private RendererHandler renderer;
    private HUD hud;

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
        /*
        if(PLAYER != null) {
            x += (velocityX + PLAYER.getVelocityX());
            y += (velocityY + PLAYER.getVelocityY());
        }else{
            getPlayer();
        }

         */

        x += velocityX;
        y += velocityY;


        collision();
    }

    private void collision() {
        for (int i = 0; i < renderer.enemies.size(); i++) {
            Enemy temporaryObject = (Enemy) renderer.enemies.get(i);
                if (getBounds().intersects(temporaryObject.getBounds())) {
                    renderer.removeEnemy(temporaryObject);
                    //Spawner.enemyCount--;
                    hud.score += 100;
                }



            /*
            if(temporaryObject.getId() != ID.Player) {
                if (getBounds().intersects(temporaryObject.getBounds())) {
                    renderer.removeObject(temporaryObject);
                    if (temporaryObject.getId() == ID.Enemy) {
                        hud.score += 100;
                    }
                }
            }

             */


            if (temporaryObject.getId() == ID.Crate || temporaryObject.getId() == ID.Block) {
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
