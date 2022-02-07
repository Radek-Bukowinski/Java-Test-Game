package com.project.game.main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader {

    // The object which stores the image
    private BufferedImage bufferedImage;


    // Read the image from the directory specified, and then return is
    public BufferedImage loadImage(String path){
        try {
            bufferedImage = ImageIO.read(getClass().getResource(path));
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
        return bufferedImage;
    }
}
