package com.project.game.main;

import java.util.Random;

public class Spawner {
    private RendererHandler renderer;
    private HUD hud;
    private Random random = new Random();

    private int ticks = hud.score;

    private GameObject enemy;


    public Spawner(RendererHandler renderer, HUD hud) {
        this.renderer = renderer;
        this.hud = hud;
    }

    public void tick() {

        if(renderer.enemies.size() < 5){
            //enemy = renderer.addEnemy(new Enemy((, , 100, ID.Enemy, renderer));
        }

        ticks++;
        if(ticks >= 1000) {
            ticks = 0;


        }
    }
}
