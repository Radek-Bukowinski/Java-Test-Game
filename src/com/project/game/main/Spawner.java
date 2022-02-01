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

    public Spawner(int x, int y, int health, ID id, RendererHandler renderer, int uid, boolean isCollidible) {
        super(x, y, health, id, isCollidible);
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
        int xToSpawnAt = 0;
        int yToSpawnAt = 0;
        if(renderer.enemies.size() < 2) {
            int chosen = Game.getRandomNumber(1, 4);
            for (int q = 0; q < renderer.spawners.size(); q++) {
                Spawner spawnerToUse = (Spawner) renderer.spawners.get(q);
                if (spawnerToUse.uid == chosen) {
                    // Must determine direction where the enemy will spawn.
                    // Each spawner has a unique identifier, based on this we can tell where it should spawn.
                    // Spawn decided relative to the player.
                    // 1 is right
                    // 2 is left
                    // 3 is up
                    // 4 is down

                    if(spawnerToUse.uid == 1){
                        xToSpawnAt = (int) ((int) Loading.playerObject.x + (spawnerToUse.x - Loading.playerObject.x));
                        yToSpawnAt = (int) Loading.playerObject.y;
                    }
                    if(spawnerToUse.uid == 2){
                        xToSpawnAt = (int) ((int) Loading.playerObject.x - (spawnerToUse.x - Loading.playerObject.x));
                        yToSpawnAt = (int) Loading.playerObject.y;
                    }

                    if(spawnerToUse.uid == 3){
                        xToSpawnAt = (int) Loading.playerObject.x;
                        yToSpawnAt = (int) ((int) Loading.playerObject.y + (spawnerToUse.y - Loading.playerObject.y));
                    }
                    if(spawnerToUse.uid == 4){
                        xToSpawnAt = (int) Loading.playerObject.x;
                        yToSpawnAt = (int) ((int) Loading.playerObject.y - (spawnerToUse.y - Loading.playerObject.y));
                    }

                    renderer.addEnemy(new Enemy(xToSpawnAt, yToSpawnAt, 100, ID.Enemy, renderer, enemyCount, true));
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
