package com.project.game.objects;

import com.project.game.identifiers.ID;
import com.project.game.main.BufferedImageLoader;
import com.project.game.main.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Health extends GameObject {

        private BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        private BufferedImage bufferedImage;


        public Health(float x, float y, int health, ID id, boolean isCollidible) {
            super(x, y, health, id, isCollidible);
            // Get the image
            bufferedImage = bufferedImageLoader.loadImage("/health.png");
        }

        @Override
        public void tick() {
        }

        @Override
        public void render(Graphics graphics) {
            // Draw the image
            graphics.drawImage(bufferedImage, (int) x, (int) y, null);
        }



        @Override
        public Rectangle getBounds() {
            return new Rectangle((int) x, (int) y, 32, 32);
        }

}
