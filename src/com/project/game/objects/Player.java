package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.main.Game;
import com.project.game.main.GameObject;
import com.project.game.main.HUD;
import com.project.game.main.RendererHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.io.*;
import java.net.*;

public class Player extends GameObject {
    private RendererHandler renderer;
    private Random random = new Random();
    private Game game;
    private BufferedImage objectTexture;
    
    private ClientSideConnection clientSideConnection;
    
    //Player.connectToServer();

    public Player(int x, int y, int health, ID id, RendererHandler renderer) {
        super(x, y, health, id);
        this.renderer = renderer;
        setTexture();
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
                    if (temporaryObject.getId() == ID.Block || temporaryObject.getId() == ID.Crate || temporaryObject.getId() == ID.Coin) {
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
    
    private class ClientSideConnection(){
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
    public void setTexture(){
        try {
            objectTexture = ImageIO.read(new File("res/player.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.white);
        //graphics.fillRect((int)x, (int)y, 32, 32);
        graphics.drawImage(objectTexture, (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 32);
    }
}
