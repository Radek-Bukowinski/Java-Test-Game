package com.project.game.main;

import java.util.Random;

public class Spawner {
    private RendererHandler renderer;
    private HUD hud;
    private Random random = new Random();

    private int ticks = hud.score;


    public Spawner(RendererHandler renderer, HUD hud) {
        this.renderer = renderer;
        this.hud = hud;
    }

    public void tick() {
        ticks++;
        if(ticks >= 1000) {
            ticks = 0;
            //int x = random.nextInt(Game.WIDTH) - 100;
            //int y = random.nextInt(Game.HEIGHT) - 100;



            //renderer.addObject(new Enemy(x, y, 100, ID.Enemy, renderer));
        }
    }
}
