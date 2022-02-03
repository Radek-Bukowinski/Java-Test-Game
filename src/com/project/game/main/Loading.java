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
        playerObject = new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, 100, ID.Player, renderer, game , true);
    }

    public void tick()
    {
        progressLevel = Game.constrain(progressLevel, 80, 1100);
        ticks++;
        progressLevel += 20;
        
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

        if(progressLevel == 1100) {
            game.windowSTATE = STATE.Game;
            game.initialiseLevel(game.currentLevel);
            renderer.addObject(playerObject);

            if(game.multiplayerEnabled == true){
                game.openConnections();
            }


            /*
            renderer.addObject(new Spawner((int) (playerObject.getX() + 64), (int) (playerObject.getY()), 100, ID.Spawner, renderer, 1, true));
            renderer.addSpawner(new Spawner((int) (playerObject.getX() + 64), (int) (playerObject.getY()), 100, ID.Spawner, renderer, 1, true));

            renderer.addObject(new Spawner((int) (playerObject.getX() - 64), (int) (playerObject.getY()), 100, ID.Spawner, renderer, 2, true));
            renderer.addSpawner(new Spawner((int) (playerObject.getX() - 64), (int) (playerObject.getY()), 100, ID.Spawner, renderer, 2, true));


            renderer.addObject(new Spawner((int) (playerObject.getX()), (int) (playerObject.getY() + 64), 100, ID.Spawner, renderer, 3, true));
            renderer.addSpawner(new Spawner((int) (playerObject.getX()), (int) (playerObject.getY() + 64), 100, ID.Spawner, renderer, 3, true));


            renderer.addObject(new Spawner((int) (playerObject.getX()), (int) (playerObject.getY() - 64), 100, ID.Spawner, renderer, 4, true));
            renderer.addSpawner(new Spawner((int) (playerObject.getX()), (int) (playerObject.getY() - 64), 100, ID.Spawner, renderer, 4, true));
            */

            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
        }
    }

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

    public String getMultiplayerStateString() {
        return multiplayerStateString;
    }

    public void setMultiplayerStateString(String multiplayerStateString) {
        this.multiplayerStateString = multiplayerStateString;
    }
}
