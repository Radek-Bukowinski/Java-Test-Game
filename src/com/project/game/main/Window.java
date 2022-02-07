package com.project.game.main;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

    // Constructor the window, needs size, a window title, and game object
    public Window(int width, int height, String title, Game game) {

        // Create a new JFrame, which acts as the container for everything
        JFrame jframe = new JFrame(title);

        // Set the size of the window
        jframe.setPreferredSize(new Dimension(width, height));
        jframe.setMaximumSize(new Dimension(width, height));
        jframe.setMinimumSize(new Dimension(width, height));

        //Set what happens when user closes window, window will exit
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set window to not be resizable
        jframe.setResizable(false);

        // Window starts in center of screen
        jframe.setLocationRelativeTo(null);

        // Add the game to the window
        jframe.add(game);

        // Set window to be visible
        jframe.setVisible(true);

        //Start the game
        game.start();
    }
}
