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

public class Player extends GameObject {
    private RendererHandler renderer;
    private Random random = new Random();
    private Game game;
    private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
    private BufferedImage bufferedImage;
    private HUD hud;
    private ClientSideConnection clientSideConnection;
    
    //Player.connectToServer();

    public Player(int x, int y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
        bufferedImage = bufferedImageLoader.loadImage("/player.png");
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

    private GameObject BLOCK = null;
    public void getBLOCK() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Block) {
                BLOCK = renderer.objects.get(i);
                //break;
            }
        }
    }

    private GameObject COIN = null;
    public void getCOIN() {
        for (int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Coin) {
                COIN = renderer.objects.get(i);
                //break;
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

        //x = (int) game.constrain((int) x, 0, Game.WIDTH - 32);
        //y = (int) game.constrain((int) y, 0, Game.HEIGHT - 60);

        HUD.health = health;

        collision();
        isAlive();
    }

    private void collision() {
        if(game.windowSTATE == STATE.Game) {
            if(PLAYER != null) {
                //System.out.println("PLAYER FOUND");
                for (int i = 0; i < renderer.objects.size(); i++) {
                    GameObject temporaryObject = renderer.objects.get(i);
                    if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate) {
                        //System.out.println("BLOCK FOUND");
                        if (PLAYER.getBounds().intersects(temporaryObject.getBounds())) {
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
                    /*
                if (ENEMY != null) {
                    if (PLAYER.getBounds().intersects(ENEMY.getBounds())) {
                        PLAYER.setHealth(PLAYER.getHealth() - 2);
                    }
                } else {
                    getEnemy();
                }

                     */
            } else{
                getPlayer();
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
        if(PLAYER.getHealth() == 0) {
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
