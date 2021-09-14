package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.objects.Enemy;
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
    
    private Color white = Color.white;

    public Loading(Game game, RendererHandler renderer)
    {
        this.game = game;
        this.renderer = renderer;
    }

    public void tick()
    {
        progressLevel = Game.constrain(progressLevel, 80, 1200);
        ticks++;
        progressLevel += 20;
        
        if(progressLevel % 100) {
            if(Math.floor(progressLevel / 100) == 0){
                loadingString = "Loading";
            }
            if(Math.floor(progressLevel / 100) == 1){
                loadingString = "Loading.";
            }
                
            if(Math.floor(progressLevel / 100) == 2){
                loadingString = "Loading..";
            }
                
            if(Math.floor(progressLevel / 100) == 3){
                loadingString = "Loading...";
            }
                
                
        }

        if(progressLevel == 1200) {
            game.windowSTATE = STATE.Game;
            game.initialiseLevel();
            renderer.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, 100, ID.Player, renderer));
            renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
        }
    }

    public void render(Graphics graphics)
    {
        graphics.setColor(white);

        Font font = new Font("Arial", 1, 30);
        graphics.setFont(font);
        graphics.drawString(loadingString, 570, 300);

        graphics.setColor(Color.gray);
        graphics.fillRect(80, 660, 1200, 20);

        graphics.setColor(white);
        graphics.drawRect(80, 660, 1200, 20);
        graphics.fillRect(80, 660, progressLevel, 20);
    }
}
