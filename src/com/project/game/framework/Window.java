package com.project.game.framework;

import com.project.game.framework.Game;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {
    public Window(int width, int height, String title, Game game)
    {
        JFrame jframe = new JFrame(title);

        jframe.setPreferredSize(new Dimension(width, height));
        jframe.setMaximumSize(new Dimension(width, height));
        jframe.setMinimumSize(new Dimension(width, height));

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);

        jframe.add(game);

        jframe.setVisible(true);

        game.start();

    }
}
