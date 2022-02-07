package com.project.game.main;

import java.awt.*;
import java.util.LinkedList;

public class RendererHandler {

    // Create all the different lists for storing objects
    public LinkedList<GameObject> objects = new LinkedList<GameObject>();
    public LinkedList<GameObject> enemies = new LinkedList<GameObject>();
    public LinkedList<GameObject> collidibles = new LinkedList<GameObject>();
    public LinkedList<GameObject> collectibles = new LinkedList<GameObject>();

    public LinkedList<GameObject> spawners = new LinkedList<GameObject>();

    // Go through the entire list and update every object
    public void tick() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject temporaryObject = objects.get(i);
            temporaryObject.tick();
        }
        for(int i = 0; i < enemies.size(); i++) {
            GameObject enemyObject = enemies.get(i);
            enemyObject.tick();
        }
    }

    // Go through the entire list and update every object
    public void render(Graphics graphics) {
        for(int i = 0; i < objects.size(); i++) {
            GameObject temporaryObject = objects.get(i);
            temporaryObject.render(graphics);
        }
        for(int i = 0; i < enemies.size(); i++) {
            GameObject enemyObject1 = enemies.get(i);
            enemyObject1.render(graphics);
        }
    }

    /*
        Functions to add and remove objects from the lists
    */

    public GameObject addObject(GameObject object) {
        this.objects.add(object);
        return object;
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }

    public GameObject addEnemy(GameObject object) {
        this.enemies.add(object);
        return object;
    }

    public void removeEnemy(GameObject object) {
        this.enemies.remove(object);
    }

    public GameObject addCollidible(GameObject object) {
        this.collidibles.add(object);
        return object;
    }

    public void removeCollidible(GameObject object) {
        this.collidibles.remove(object);
    }

    public GameObject addSpawner(GameObject object) {
        this.spawners.add(object);
        return object;
    }

    public void removeSpawner(GameObject object) {
        this.spawners.remove(object);
    }

    public GameObject addCollectible(GameObject object) {
        this.collectibles.add(object);
        return object;
    }

    public void removeCollectible(GameObject object) {
        this.collectibles.remove(object);
    }

}
