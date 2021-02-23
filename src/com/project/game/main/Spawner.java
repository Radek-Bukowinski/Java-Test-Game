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
            //renderer.addObject(new Enemy(random.nextInt(Game.WIDTH) - 100, random.nextInt(Game.HEIGHT) - 100, 100, ID.Enemy, renderer));
        }
    }
}
