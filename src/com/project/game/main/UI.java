/*
TODO:
        Implement Upgrades System
        Highlight feature to UI - done
        Random movement of enemy
        Spawning in of health/score
*/

package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.objects.Player;
import com.project.game.objects.Projectile;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.project.game.identifiers.STATE.MultiplayerSelect;

public class UI extends MouseAdapter {
    private Game game;
    private RendererHandler renderer;
    private Camera camera;
    private Loading loading;

    Font font = new Font("Arial", 1, 20);
    Font largeFont = new Font("Arial", 1, 40);

    private Color startButtonColor = Color.white;
    private Color infoButtonColor = Color.white;
    private Color exitButtonColor = Color.white;
    private Color backButtonColor = Color.white;

    public UI(Game game, RendererHandler renderer, Camera camera) {
        this.game = game;
        this.renderer = renderer;
        this.camera = camera;
    }

    GameObject temporaryObject = null;
    public void findPlayer(){
        for(int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Player) {
                temporaryObject = renderer.objects.get(i);
                break;
            }
        }
    }

    private void resetButtonColors(){
        startButtonColor = Color.white;
        infoButtonColor = Color.white;
        exitButtonColor = Color.white;
        backButtonColor = Color.white;
    }

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /*
        Below: Function for shooting mechanic
    */
    ScheduledFuture<?> shootHandle;
    // We use a scheduler as this allows us to set an event that happens in the future.
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private void shootProjectile(int mouseX, int mouseY){
        GameObject temporaryProjectile = renderer.addObject(new Projectile(temporaryObject.x + 16, temporaryObject.y + 16, 100, ID.Projectile, renderer, false));
        // Work out the angle between mouse and the player.
        float angle = (float) Math.atan2(mouseY - temporaryObject.y, mouseX - temporaryObject.x);
        int projectileVelocity = 5;
        temporaryProjectile.velocityX = (float) ((projectileVelocity) * Math.cos(angle));
        temporaryProjectile.velocityY = (float) ((projectileVelocity) * Math.sin(angle));
    }
    /*
        End of shooting mechanic
    */

    @Override
    public void mousePressed(MouseEvent event){
        super.mousePressed(event);
        int mouseX = (int) (event.getX() + camera.getX());
        int mouseY = (int) (event.getY() + camera.getY());

        int realMouseX = (int) event.getX();
        int realMouseY  = (int) event.getY();

        if(game.windowSTATE == STATE.Game) {
            if (temporaryObject != null) {
                // This calls the shoot function
                Runnable shoot = () -> shootProjectile(mouseX, mouseY);
                // We keep calling the shoot function, because the mouse is held down
                shootHandle = scheduler.scheduleAtFixedRate(shoot, 0, 800, TimeUnit.MILLISECONDS);
            } else {
                findPlayer();
            }
        }

        if(game.windowSTATE == STATE.ModeSelect) {
            resetButtonColors();
            //singleplayer button
            if (mouseOver(realMouseX, realMouseY, 340, 325, 200, 40)) {
                game.windowSTATE = STATE.Loading;
            }


            //multiplayer button
            if (mouseOver(realMouseX, realMouseY, 740, 325, 200, 40)) {
                Runnable callUpdateState = () -> updateState(STATE.MultiplayerSelect);
                executorService.schedule(callUpdateState, 1, TimeUnit.MILLISECONDS);

            }
            //back button
            if(mouseOver(realMouseX, realMouseY, 540, 625, 200, 40)) {
                game.windowSTATE = STATE.Menu;
            }
        }

        if(game.windowSTATE == STATE.MultiplayerSelect) {
            resetButtonColors();
            // Create a new multiplayer session - players will be allowed to join for co-op mode.
            if (mouseOver(realMouseX, realMouseY, 340, 325, 200, 40)) {
                game.windowSTATE = STATE.MultiplayerHost;

            }

            // Join a multiplayer session - joins an already existing multiplayer session
            if (mouseOver(realMouseX, realMouseY, 740, 325, 200, 40)) {
                game.windowSTATE = STATE.MultiplayerJoin;
            }
            //back button
            if(mouseOver(realMouseX, realMouseY, 540, 625, 200, 40)) {
                game.windowSTATE = STATE.ModeSelect;
            }
        }

        if(game.windowSTATE == STATE.MultiplayerHost){
            game.initialiseMultiplayer();
            game.windowSTATE = STATE.Loading;
            loading.setMultiplayerStateString("Server has been created...");
            game.multiplayerEnabled = true;
        }

        if(game.windowSTATE == STATE.MultiplayerJoin){
            Player temporaryPlayer = (Player) Loading.playerObject;
            temporaryPlayer.connectToServer();
            loading.setMultiplayerStateString("Player connected to server...");
            game.windowSTATE = STATE.Loading;
            game.multiplayerEnabled = true;
        }


        if(game.windowSTATE == STATE.Menu) {
            resetButtonColors();
            //start button
            if (mouseOver(realMouseX, realMouseY, 540, 315, 200, 40)) {
               game.windowSTATE = STATE.ModeSelect;
            }
            if (mouseOver(realMouseX, realMouseY, 540, 375, 200, 40)) {
                game.windowSTATE = STATE.Info;
            }
            if (mouseOver(realMouseX, realMouseY, 540, 435, 200, 40)) {
                System.exit(0);
            }
        }

        if(game.windowSTATE == STATE.Info) {
            resetButtonColors();
            if(mouseOver(realMouseX, realMouseY, 540, 625, 200, 40)) {
                game.windowSTATE = STATE.Menu;
            }
        }

        if(game.windowSTATE ==  STATE.Death) {
            if(mouseOver(realMouseX, realMouseY,540, 315, 200, 50)) {
                game.windowSTATE = STATE.Loading;

                HUD.health = 100;
                HUD.level = 1;
                HUD.score = 0;
            }
            if(mouseOver(realMouseX, realMouseY, 540, 375, 200, 50)) {
                System.exit(0);
            }
        }
        if(game.windowSTATE == STATE.Upgrades){

        }
    }


    private static STATE updateState(STATE state) {
        Game.windowSTATE = state;
        return state;
    }

    @Override
    public void mouseMoved(MouseEvent event){
        super.mouseMoved(event);
        int mouseX = event.getX();
        int mouseY = event.getY();
            
        if(game.windowSTATE == STATE.ModeSelect){

            //Singleplayer Button
            if (mouseOver(mouseX, mouseY, 340, 325, 200, 40)) {
                startButtonColor = Color.darkGray;
            }else{
                startButtonColor = Color.white;
            }
        

            //Multiplayer Button
            if (mouseOver(mouseX, mouseY, 740, 325, 200, 40)) {
                infoButtonColor = Color.darkGray;
            }else{
                infoButtonColor = Color.white;
            }

            //Back Button
            if(mouseOver(mouseX, mouseY, 540, 625, 200, 40)) {
                backButtonColor = Color.darkGray;
            }else{
                backButtonColor = Color.white;
            }
        }

        if(game.windowSTATE == MultiplayerSelect){
            //Join Button
            if (mouseOver(mouseX, mouseY, 340, 325, 200, 40)) {
                startButtonColor = Color.darkGray;
            }else{
                startButtonColor = Color.white;
            }


            //Host Button
            if (mouseOver(mouseX, mouseY, 740, 325, 200, 40)) {
                infoButtonColor = Color.darkGray;
            }else{
                infoButtonColor = Color.white;
            }

            //Back Button
            if(mouseOver(mouseX, mouseY, 540, 625, 200, 40)) {
                backButtonColor = Color.darkGray;
            }else{
                backButtonColor = Color.white;
            }
        }

        if(game.windowSTATE == STATE.Menu){

            //Start Button
            if (mouseOver(mouseX, mouseY, 540, 315, 200, 40)) {
                startButtonColor = Color.darkGray;
            }else{
                startButtonColor = Color.white;
            }

            //Info Button
            if (mouseOver(mouseX, mouseY, 540, 375, 200, 40)) {
                infoButtonColor = Color.darkGray;
            }else{
                infoButtonColor = Color.white;
            }

            //Exit Button
            if (mouseOver(mouseX, mouseY, 540, 435, 200, 40)) {
                exitButtonColor = Color.darkGray;
            }else{
                exitButtonColor = Color.white;
            }
        }
        if(game.windowSTATE == STATE.Info){
            if(mouseOver(mouseX, mouseY, 540, 625, 200, 40)) {
                backButtonColor = Color.darkGray;
            }else{
                backButtonColor = Color.white;
            }
        }

    }


    @Override
    public void mouseReleased(MouseEvent event){
        super.mouseReleased(event);
        if(game.windowSTATE == STATE.Game) {
            // Cancel the shoot event, as the mouse has been released.
            try {
                Runnable canceller = () -> shootHandle.cancel(true);
                scheduler.schedule(canceller, 0, TimeUnit.MILLISECONDS);
            } catch (NullPointerException nullPointerException){
                nullPointerException.printStackTrace();
            }
        }
    }

    // We can use this function to determine if the mouse is within bounds of a button
    public boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        if(mouseX > x && mouseX < x + width) {
            if(mouseY > y && mouseY < y + height) {
                return true;
            }else { return false; }
        } else { return false; }
    }

    public void tick() { }

    public void render(Graphics graphics) {
        if(game.windowSTATE == STATE.Menu) {
            graphics.setColor(Color.white);

            graphics.setFont(largeFont);
            graphics.drawString("Game", 580, 100);

            graphics.setFont(font);
            graphics.drawString("By: Radek", 590, 130);

            graphics.setColor(startButtonColor);
            graphics.drawRect(640 - 100, 340 - 25, 200, 40);
            graphics.drawString("Start", 617, 342);

            graphics.setColor(infoButtonColor);
            graphics.drawRect(640 - 100, 400 - 25, 200, 40);
            graphics.drawString("Options", 618, 402);

            graphics.setColor(exitButtonColor);
            graphics.drawRect(640 - 100, 460 - 25, 200, 40);
            graphics.drawString("Quit", 617, 462);

            graphics.setColor(Color.white);
            graphics.drawString("Version: Alpha 0.0.5", 540, 680);


        }

        else if(game.windowSTATE == STATE.ModeSelect){
            graphics.setFont(font);
            graphics.setColor(startButtonColor);
            graphics.drawRect(340, 325, 200, 40);
            graphics.drawString("Singleplayer", 380, 350);

            graphics.setColor(infoButtonColor);
            graphics.drawRect(740, 325, 200, 40);
            graphics.drawString("Multiplayer", 790, 350);

            graphics.setColor(backButtonColor);
            graphics.drawRect(640 - 100, 650 - 25, 200, 40);
            graphics.drawString("Back", 617, 651);
        }

        else if(game.windowSTATE == MultiplayerSelect){
            graphics.setFont(font);
            graphics.setColor(startButtonColor);
            graphics.drawRect(340, 325, 200, 40);
            graphics.drawString("Host", 380, 350);

            graphics.setColor(infoButtonColor);
            graphics.drawRect(740, 325, 200, 40);
            graphics.drawString("Join", 790, 350);

            graphics.setColor(backButtonColor);
            graphics.drawRect(640 - 100, 650 - 25, 200, 40);
            graphics.drawString("Back", 617, 651);
        }

        else if (game.windowSTATE == STATE.Info) {
            graphics.setColor(Color.white);

            graphics.setFont(largeFont);
            graphics.drawString("Java Game", 540, 100);

            graphics.setFont(font);
            graphics.drawString("By: Radek", 590, 130);

            graphics.drawString("This is a basic game created by Radek, coded in Java.", 410, 300);
            graphics.drawString("To play, you must dodge the enemies whilst, attempting to shoot at them.", 330, 330);
            graphics.drawString("If you collide with an enemy, you lose health.", 460, 360);
            graphics.drawString("The longer you play the more enemies will spawn, each with higher difficulty.", 320, 390);
            graphics.drawString("Reminder: The Game is still in development, there will be bugs.", 330, 420);
            graphics.drawString("Good Luck and Have Fun!", 490, 450);

            graphics.setColor(backButtonColor);
            graphics.drawRect(640 - 100, 650 - 25, 200, 40);
            graphics.drawString("Back", 617, 651);
        }

        else if(game.windowSTATE == STATE.Death) {
            for(int i = 0; i < renderer.objects.size(); i++) {
                GameObject temporaryObject = renderer.objects.get(i);
                renderer.removeObject(temporaryObject);
            }

            graphics.setColor(Color.white);
            graphics.setFont(font);

            graphics.drawString("Game Over", 590, 290);

            graphics.drawRect(640 - 100, 340 - 25, 200, 40);
            graphics.drawString("Try Again", 600, 342);

            graphics.drawRect(640 - 100, 400 - 25, 200, 40);
            graphics.drawString("Quit", 617, 402);
        }
        else if(game.windowSTATE == STATE.Paused)
        {
            Font paused = new Font("Arial", 1, 16);
            graphics.setColor(Color.red);
            graphics.setFont(paused);
            graphics.drawString("Paused", 15, 85);
        }

        else if(game.windowSTATE == STATE.Upgrades)
        {

        }

    }
}
