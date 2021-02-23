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

    public Loading(Game game, RendererHandler renderer)
    {
        this.game = game;
        this.renderer = renderer;
    }

    public void tick()
    {
        progressLevel = Game.constrain(progressLevel, 0, 1280);
        ticks++;
        progressLevel += 10;


        if(progressLevel ==  100) {
            loadingString = "Loading.";
        }
        if(progressLevel ==  200) {
            loadingString = "Loading..";
        }
        if(progressLevel ==  300) {
            loadingString = "Loading...";
        }
        if(progressLevel ==  400) {
            loadingString = "Loading";
        }
        if(progressLevel ==  500) {
            loadingString = "Loading.";
        }
        if(progressLevel ==  600) {
            loadingString = "Loading..";
        }
        if(progressLevel ==  700) {
            loadingString = "Loading...";
        }
        if(progressLevel ==  800) {
            loadingString = "Loading";
        }
        if(progressLevel ==  900) {
            loadingString = "Loading.";
        }
        if(progressLevel ==  1000) {
            loadingString = "Loading..";
        }
        if(progressLevel ==  1100) {
            loadingString = "Loading...";
        }

        if(progressLevel == 1280) {
            game.windowSTATE = STATE.Game;
            game.initialiseLevel();
            renderer.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, 100, ID.Player, renderer));
            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
        }
    }

    public void render(Graphics graphics)
    {
        graphics.setColor(Color.white);

        Font font = new Font("Arial", 1, 30);
        graphics.setFont(font);
        graphics.drawString(loadingString, 570, 300);

        graphics.setColor(Color.gray);
        graphics.fillRect(0, 660, 1280, 20);

        graphics.setColor(Color.white);
        graphics.drawRect(0, 660, 1280, 20);

        graphics.setColor(Color.white);
        graphics.fillRect(0, 660, progressLevel, 20);
    }
}
