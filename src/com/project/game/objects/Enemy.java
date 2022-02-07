package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject {
    private RendererHandler renderer;

    private BufferedImageLoader bufferedImageLoader= new BufferedImageLoader();
    private BufferedImage bufferedImage;

    private Random random = new Random();

    private GameObject PLAYER = null;

    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    private GameObject ENEMY = null;
    public void getEnemy() {
        for (int i = 0; i < renderer.enemies.size(); i++) {
            if (renderer.enemies.get(i).getId() == ID.Enemy) {
                ENEMY = renderer.enemies.get(i);
                break;
            }
        }
    }

    // An array with the possible velocities for the enemy
    int[] possibleVelocities = {
            -1, 1
    };

    public Enemy(float x, float y, int health, ID id, RendererHandler renderer, int uid, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
        this.renderer = renderer;
        this.uid = uid;

        // The velocity is random, enemy can move anywhere. Will move in a straight line once spawned
        velocityY = Game.getRandomIntFromArray(possibleVelocities);
        velocityX = Game.getRandomIntFromArray(possibleVelocities);

        // Get the texture
        bufferedImage = bufferedImageLoader.loadImage("/enemy.png");
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        collision();
    }

    private void collision() {
        for (int i = 0; i < renderer.collidibles.size(); i++) {
            GameObject temporaryObject = renderer.collidibles.get(i);
            // If an enemy object intersects a block or crate
            if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate) {
                if (this.getBounds().intersects(temporaryObject.getBounds())) {
                    if (horizontalCollision().intersects(temporaryObject.getBounds())) {
                        if (velocityX > 0) {
                            // Reflect
                            velocityX *= -1;
                        } else if (velocityX < 0) {
                            // Reflect
                            velocityX *= -1;
                        }
                    }

                    if (verticalCollision().intersects(temporaryObject.getBounds())) {
                        if (velocityY > 0) {
                            // Reflect
                            velocityY *= -1;
                        } else if (velocityY < 0) {
                            // Reflect
                            velocityY *= -1;
                        }
                    }
                }
            }
            /*
                if(temporaryObject.getId() == ID.Player){
                    if (temporaryObject.getBounds().intersects(this.getBounds())) {
                        temporaryObject.setHealth(temporaryObject.getHealth() - 2);
                    }
                }

             */
        }

        // If an enemy collides with the player
        if(Loading.playerObject.getBounds().intersects(this.getBounds())){
            // Lower the players health
            Loading.playerObject.setHealth(Loading.playerObject.getHealth() - 1);
        }
    }

    // Use this hitbox for testing for collision if is horizontal
    public Rectangle horizontalCollision(){
        float boundaryX = x + velocityX;
        float boundaryY = y + 2;
        float boundaryWidth = 16 + velocityX / 2;
        float boundaryHeight = 12;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    // Use this hitbox for testing for collision if is vertical
    public Rectangle verticalCollision(){
        float boundaryX = x + 2;
        float boundaryY = y + velocityY;
        float boundaryWidth = 12;
        float boundaryHeight = 16 + velocityY / 2;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    @Override
    public void render(Graphics graphics) {
        // Make sure enemy isn't null first
        if(ENEMY != null) {
            // Draw the image
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
