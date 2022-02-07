package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.objects.Player;

import java.awt.*;
import java.util.Random;

public class Loading {
    private int progressLevel = 0;
    private Game game;
    private RendererHandler renderer;
    private int ticks;
    private Random random = new Random();
    private String loadingString = "Loading";
    private String informationString = "Shoot enemies and collect coins to win!";
    private String multiplayerStateString = "";
    private Color white = Color.white;

    public static GameObject playerObject;

    public Loading(Game game, RendererHandler renderer)
    {
        this.game = game;
        this.renderer = renderer;

        // Creates a new player object, that can then be added into the game.
        playerObject = new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, 100, ID.Player, renderer, game , true);
    }

    public void tick()
    {
        progressLevel = Game.constrain(progressLevel, 80, 1100);
        ticks++;

        // Increase the progress amount by 20 every tick
        progressLevel += 20;

        // Update the string while the game loads
        if((progressLevel % 100) == 0) {
            if(Math.floor(progressLevel / 100) == 0){
                loadingString = "Loading";
            }
            if(Math.floor(progressLevel / 100) == 1){
                loadingString = "Loading.";
            }
                
            if(Math.floor(progressLevel / 100) == 2){
                loadingString = "Loading..";
            }
                
            if(Math.floor(progressLevel / 100) == 3) {
                loadingString = "Loading...";
            }
        }

        // Once fully loaded, initialise necessary components and multiplayer if that option has been selected.
        if(progressLevel == 1100) {
            game.windowSTATE = STATE.Game;
            game.initialiseLevel(game.currentLevel);
            renderer.addObject(playerObject);

            if(game.multiplayerEnabled == true){
                game.openConnections();
            }
        }
    }

    // Draw all the components
    public void render(Graphics graphics)
    {
        graphics.setColor(white);

        Font font = new Font("Arial", 1, 30);
        graphics.setFont(font);
        graphics.drawString(loadingString, 570, 300);

        graphics.drawString(informationString, 500, 400);
        graphics.drawString(multiplayerStateString, 500, 450);

        graphics.setColor(Color.gray);
        graphics.fillRect(80, 640, 1100, 20);

        graphics.setColor(white);
        graphics.drawRect(80, 640, 1100, 20);
        graphics.fillRect(80, 640, progressLevel, 20);
    }


    /*
        Getter and setter for determining if multiplayer is host or join
    */
    public String getMultiplayerStateString() {
        return multiplayerStateString;
    }

    public void setMultiplayerStateString(String multiplayerStateString) {
        this.multiplayerStateString = multiplayerStateString;
    }
}
