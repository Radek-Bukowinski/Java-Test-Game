package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.objects.Enemy;

import java.awt.*;

public class Spawner extends GameObject{

    private RendererHandler renderer;
    private GameObject enemy;

    private boolean active;

    public static int enemyCount = 0;

    private int uid;

    public Spawner(int x, int y, int health, ID id, RendererHandler renderer, int uid) {
        super(x, y, health, id);
        this.renderer = renderer;
        this.uid = uid;
    }

    private void isActive(){
        for(int i = 0; i < renderer.spawners.size(); i++) {
            Spawner temporaryObject = (Spawner) renderer.spawners.get(i);
            for (int z = 0; z < renderer.collidibles.size(); z++) {
                GameObject temporaryObject2 = renderer.collidibles.get(z);
                if (temporaryObject.getBounds().intersects(temporaryObject2.getBounds()) == true) {
                    temporaryObject.active = false;
                }
                if (temporaryObject.getBounds().intersects(temporaryObject2.getBounds()) == false){
                    temporaryObject.active = true;
                }
            }
        }
    }


    private void spawn(){
        if(renderer.enemies.size() < 4) {
            int chosen = Game.getRandomNumber(1, 4);
            for (int q = 0; q < renderer.spawners.size(); q++) {
                Spawner spawnerToUse = (Spawner) renderer.spawners.get(q);
                if (spawnerToUse.uid == chosen) {
                    renderer.addEnemy(new Enemy(spawnerToUse.getX(), spawnerToUse.getY(), 100, ID.Enemy, renderer, enemyCount));
                }
            }
        }
    }

    public void tick() {
        x += Loading.playerObject.getVelocityX();
        y += Loading.playerObject.getVelocityY();
        isActive();
        spawn();
    }


    @Override
    public void render(Graphics graphics) {
        //graphics.setColor(Color.gray);
        //graphics.fillRect((int)x, (int)y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 32);
    }
}
