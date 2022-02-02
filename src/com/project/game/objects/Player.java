package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.main.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import static com.project.game.main.Spawner.enemyCount;

// Player inherits the abstract the GameObject class.
public class Player extends GameObject {

    // Declaring instances of classes we use in this class.
    private RendererHandler renderer;
    private Game game;
    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
    private BufferedImage bufferedImage;
    private HUD hud;
    private ClientSideConnection clientSideConnection;

    // The constructor used to create a new Player object.
    public Player(int x, int y, int health, ID id, RendererHandler renderer, Game game, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
        this.renderer = renderer;
        this.game = game;
        bufferedImage = bufferedImageLoader.loadImage("/sprite.png");
    }

    @Override
    public void tick() {
        // We update the position of the player (x and y) based on the velocity, which is what changes on key input.
        x += velocityX;
        y += velocityY;

        // Update health
        HUD.health = health;

        // Update other values
        collision();
        isAlive();
        spawn();
    }

    private void spawn() {
        /*
            Here we handle the spawning of enemies
            This is called recursively in the tick() function
            If there is less than five enemies, we spawn an enemy inside of one 4 predetermined positions around the player
        */
        if (renderer.enemies.size() < 5) {
            int chosen = Game.getRandomNumber(1, 4);
            if(chosen == 1){
                renderer.addEnemy(new Enemy(this.x + 128, this.y, 100, ID.Enemy, renderer, enemyCount, true));
            }
            if(chosen == 2){
                renderer.addEnemy(new Enemy(this.x - 128, this.y, 100, ID.Enemy, renderer, enemyCount, true));
            }
            if(chosen == 3){
                renderer.addEnemy(new Enemy(this.x, this.y + 128, 100, ID.Enemy, renderer, enemyCount, true));
            }
            if(chosen == 4){
                renderer.addEnemy(new Enemy(this.x + 128, this.y - 128, 100, ID.Enemy, renderer, enemyCount, true));
            }
        }
    }

    private void collision() {
            for (int i = 0; i < renderer.collidibles.size(); i++) {
                // Search through objects that are collidible (solid objects only)
                GameObject temporaryObject = renderer.collidibles.get(i);
                    // Check if the collidible object bounds are within the bounds of the player
                    // Bounds are calculated from the x and y of the object, plus its size
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        //Handle collision if it is horizontal
                        if (horizontalCollision().intersects(temporaryObject.getBounds())) {
                            // We set the velocity in that direction to 0, because we now know it has collided.
                            if (velocityX > 0) {
                                velocityX = 0;
                                x = temporaryObject.getX() - 32;
                            } else if (velocityX < 0) {
                                velocityX = 0;
                                x = temporaryObject.getX() + 32;
                            }
                        }
                        // Handle collision if it is vertical
                        if (verticalCollision().intersects(temporaryObject.getBounds())) {
                            // We set the velocity in that direction to 0, because we now know it has collided.
                            if (velocityY > 0) {
                                velocityY = 0;
                                y = temporaryObject.getY() - 32;
                            } else if (velocityY < 0) {
                                velocityY = 0;
                                y = temporaryObject.getY() + 32;
                            }
                        }
                    }
            }


        for (int i = 0; i < renderer.objects.size(); i++) {
                GameObject temporaryObject = renderer.objects.get(i);
                if (renderer.objects.get(i).getId() == ID.Coin) {
                    //If the player has collided with a coin, remove it from being rendered, and add 100 to the players score.
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        renderer.removeObject(temporaryObject);
                        hud.score += 100;
                    }

                }
                if (renderer.objects.get(i).getId() == ID.Health) {
                    //If the player has collided with a health pack, remove it from being rendered, and add 10 health to the player.
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        renderer.removeObject(temporaryObject);
                        if(hud.health == 100){
                            return;
                        }else {
                            hud.health += 10;
                        }
                    }

                }
        }
    }


    public Rectangle horizontalCollision(){
        float boundaryX = x + velocityX;
        float boundaryY = y + 2;
        float boundaryWidth = 32 + velocityX / 2;
        float boundaryHeight = 28;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    public Rectangle verticalCollision(){
        float boundaryX = x + 2;
        float boundaryY = y + velocityY;
        float boundaryWidth = 28;
        float boundaryHeight = 32 + velocityY / 2;

        return new Rectangle((int) boundaryX, (int) boundaryY, (int) boundaryWidth, (int) boundaryHeight);
    }

    private void isAlive() {
        // Called in a loop in the tick(), function to ensure that the player has not died.
        // If the player has died, we reset the game.
        if(this.getHealth() == 0) {
            Game.windowSTATE = STATE.Death;
        }
    }
    
    public void connectToServer(){
        // Create a new connection on the clients side.
        // This is not used for hosting, only for connecting to servers.
        clientSideConnection = new ClientSideConnection();
    }
    
    private class ClientSideConnection{
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        
        public ClientSideConnection(){
            try{
                socket = new Socket("localhost", 51734);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch(IOException ioException){
                ioException.printStackTrace();
              }
        }
    }

    @Override
    public void render(Graphics graphics) {
        // Draw the player sprite, fetch relevant image to use as sprite from the constructor.
        graphics.drawImage(bufferedImage, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 32);
    }


}
