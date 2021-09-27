package com.project.game.main;

import java.awt.*;
import java.util.LinkedList;

public class RendererHandler {
    public LinkedList<GameObject> objects = new LinkedList<GameObject>();
    public LinkedList<GameObject> outOfRender = new LinkedList<GameObject>();
    public void tick() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject temporaryObject = objects.get(i);

            temporaryObject.tick();
        }
    }

    public void render(Graphics graphics) {
        for(int i = 0; i < objects.size(); i++) {
            GameObject temporaryObject = objects.get(i);
            if(temporaryObject.isRendered == false){
                GameObject holder = temporaryObject;
                outOfRender.add(holder);  
                objects.remove(temporaryObject);
            } else{
                  temporaryObject.render(graphics);
            }
        }
    }

    public GameObject addObject(GameObject object) {
        this.objects.add(object);
        return object;
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }
}
