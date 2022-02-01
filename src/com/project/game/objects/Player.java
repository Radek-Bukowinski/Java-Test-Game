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

public class Player extends GameObject {
    private RendererHandler renderer;
    private Random random = new Random();
    private Game game;
    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
    private BufferedImage bufferedImage;
    private HUD hud;
    private ClientSideConnection clientSideConnection;

    public Player(int x, int y, int health, ID id, RendererHandler renderer, Game game, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
        this.renderer = renderer;
        this.game = game;
        bufferedImage = bufferedImageLoader.loadImage("/sprite.png");
    }

    @Override
    public void tick() {
        x += velocityX;
        y += velocityY;

        HUD.health = health;

        collision();
        isAlive();
        spawn();
    }

    private void spawn() {
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
                GameObject temporaryObject = renderer.collidibles.get(i);
                if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate) {
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        System.out.println("collision detected");
                        if (horizontalCollision().intersects(temporaryObject.getBounds())) {
                            if (velocityX > 0) {
                                velocityX = 0;
                                x = temporaryObject.getX() - 32;
                            } else if (velocityX < 0) {
                                velocityX = 0;
                                x = temporaryObject.getX() + 32;
                            }
                        }

                        if (verticalCollision().intersects(temporaryObject.getBounds())) {
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
            }


        for (int i = 0; i < renderer.objects.size(); i++) {
            if(i > renderer.objects.size() - 1) {
                GameObject temporaryObject = renderer.objects.get(i);
                if (renderer.objects.get(i).getId() == ID.Coin) {
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        renderer.removeObject(temporaryObject);
                        hud.score += 100;
                    }

                }
                if (renderer.objects.get(i).getId() == ID.Health) {
                    if (this.getBounds().intersects(temporaryObject.getBounds())) {
                        renderer.removeObject(temporaryObject);
                        hud.health += 10;
                    }

                }
            }
        }

        /*
        if(game.windowSTATE == STATE.Game) {
            if(PLAYER != null) {
                //System.out.println("PLAYER FOUND");
                for (int i = 0; i < renderer.objects.size(); i++) {
                    GameObject temporaryObject = renderer.objects.get(i);
                    if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate) {
                        //System.out.println("BLOCK FOUND");
                        if (game.PLAYER.getBounds().intersects(temporaryObject.getBounds())) {
                            System.out.println("collision detected");
                            if(horizontalCollision().intersects(temporaryObject.getBounds())){
                                if(velocityX > 0){
                                    velocityX = 0;
                                    x = temporaryObject.getX() - 32;
                                }else if(velocityX < 0){
                                    velocityX = 0;
                                    x = temporaryObject.getX() + 32;
                                }
                            }

                            if(verticalCollision().intersects(temporaryObject.getBounds())){
                                if(velocityY > 0){
                                    velocityY = 0;
                                    y = temporaryObject.getY() - 32;
                                }else if(velocityY < 0){
                                    velocityY = 0;
                                    y = temporaryObject.getY() + 32;
                                }
                            }
                        }

                        //break;
                    }

                }
            } else{
                getPlayer();
            }




            if (PLAYER != null) {

                    for (int i = 0; i < renderer.objects.size(); i++) {
                        GameObject temporaryObject = renderer.objects.get(i);
                        if (renderer.objects.get(i).getId() == ID.Coin) {
                            if (PLAYER.getBounds().intersects(temporaryObject.getBounds())) {
                                renderer.removeObject(temporaryObject);
                                hud.score += 100;
                            }

                        }
                        if (renderer.objects.get(i).getId() == ID.Health) {
                            if (PLAYER.getBounds().intersects(temporaryObject.getBounds())) {
                                renderer.removeObject(temporaryObject);
                                hud.health += 10;
                            }

                        }
                }

                if (ENEMY != null) {
                    if (PLAYER.getBounds().intersects(ENEMY.getBounds())) {
                        PLAYER.setHealth(PLAYER.getHealth() - 2);
                    }
                } else {
                    getEnemy();
                }


            } else{
                getPlayer();
            }
            */




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
        if(this.getHealth() == 0) {
            Game.windowSTATE = STATE.Death;
        }
    }
    
    public void connectToServer(){
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
                dataOutputStream =new DataOutputStream(socket.getOutputStream());
            } catch(IOException ioException){
                ioException.printStackTrace();
              }
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        //graphics.fillRect((int)x, (int)y, 32, 32);
        graphics.drawImage(bufferedImage, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 32);
    }


}
