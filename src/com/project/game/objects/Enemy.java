package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.GameObject;
import com.project.game.main.RendererHandler;

import java.awt.*;

public class Enemy extends GameObject {
    private RendererHandler renderer;


    private GameObject PLAYER = null;

    private float directionX;
    private float directionY;



    private float attractionX;
    private float attractionY;

    private float vectorX;
    private float vectorY;

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

    public Enemy(float x, float y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
        velocityY = 1;
        velocityX = 1;
    }

    @Override
    public void tick() {
        x += vectorX;
        y += vectorY;



        if(PLAYER != null) {

            float differenceX = (float) x - PLAYER.getX() - 16;
            float differenceY = (float) y - PLAYER.getY() - 16;
            float distance = (float) Math.sqrt((x - PLAYER.getX()) * (x - PLAYER.getX()) + (y - PLAYER.getY()) * (y - PLAYER.getY()));

            attractionX =  ((-1 / distance) * differenceX);
            attractionY =  ((-1 / distance) * differenceY);



            //velocityX =  ((-1 / distance) * differenceX);
            //velocityY =  ((-1 / distance) * differenceY);

            directionX =  velocityX;
            directionY =  velocityY;
        } else{
            getPlayer();
        }

       vectorX = (attractionX + directionX) / 2;
       vectorY = (attractionY + directionY) / 2;

        //x += velocityX;
        //y += velocityY;

        /*
        if(y <= 0 || y >= Game.HEIGHT - 32) {
            velocityY *= -1;
        }
        if(x <= 0 || x >= Game.WIDTH - 16) {
            velocityX *= -1;
        }

         */

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
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 16, 16);
    }
}
