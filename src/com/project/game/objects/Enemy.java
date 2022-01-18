package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.main.BufferedImageLoader;
import com.project.game.main.Game;
import com.project.game.main.GameObject;
import com.project.game.main.RendererHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject {
    private RendererHandler renderer;

    private BufferedImageLoader bufferedImageLoader= new BufferedImageLoader();
    private BufferedImage bufferedImage;

    private Random random = new Random();

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

    public Enemy(float x, float y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
        velocityY = 1;
        velocityX = 1;
        bufferedImage = bufferedImageLoader.loadImage("/enemy.png");
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        collision();

        //vectorX = Game.getRandomNumber(-5, 5);
        //vectorY = Game.getRandomNumber(-5, 5);

        /*
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

         */

       //vectorX = (attractionX + directionX) / 2;
       //vectorY = (attractionY + directionY) / 2;

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

    private void collision() {
        if(Game.windowSTATE == STATE.Game) {
            if(ENEMY != null) {
                //System.out.println("PLAYER FOUND");
                for (int i = 0; i < renderer.objects.size(); i++) {
                    GameObject temporaryObject = renderer.objects.get(i);
                    if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate || temporaryObject.getId() == ID.Coin) {
                        //System.out.println("BLOCK FOUND");
                        if (ENEMY.getBounds().intersects(temporaryObject.getBounds())) {
                            //System.out.println("collision detected");
                            if(horizontalCollision().intersects(temporaryObject.getBounds())){
                                if(velocityX > 0){
                                    velocityX *= -1;
                                    //x = temporaryObject.getX() - 32;
                                }else if(velocityX < 0){
                                    velocityX *= -1;
                                    //x = temporaryObject.getX() + 32;
                                }
                            }

                            if(verticalCollision().intersects(temporaryObject.getBounds())){
                                if(velocityY > 0){
                                    velocityY *= -1;
                                    //y = temporaryObject.getY() - 32;
                                }else if(velocityY < 0){
                                    velocityY *= -1;
                                    //y = temporaryObject.getY() + 32;
                                }
                            }
                        }

                        //break;
                    }

                }
            } else{
                getEnemy();
            }

            /*
            if (PLAYER != null) {

                if (ENEMY != null) {
                    if (PLAYER.getBounds().intersects(ENEMY.getBounds())) {
                        PLAYER.setHealth(PLAYER.getHealth() - 2);
                    }
                }else{
                    getEnemy();
                }

             */


        }
    }

    public Rectangle horizontalCollision(){
        float boundaryX = x + velocityX;
        float boundaryY = y + 2;
        float boundaryWidth = 16 + velocityX / 2;
        float boundaryHeight = 12;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    public Rectangle verticalCollision(){
        float boundaryX = x + 2;
        float boundaryY = y + velocityY;
        float boundaryWidth = 12;
        float boundaryHeight = 16 + velocityY / 2;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    @Override
    public void render(Graphics graphics) {
        if(ENEMY != null) {
            graphics.setColor(Color.RED);
            //graphics.fillRect((int) x, (int) y, 16, 16);
            graphics.drawImage(bufferedImage, (int) x, (int) y, null);
        }else{
            getEnemy();
        }

    }


    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 16, 16);
    }
}
