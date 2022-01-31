package com.project.game.main;

import com.project.game.identifiers.DIRECTION;
import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class KeyInput extends KeyAdapter {
    private RendererHandler renderer;

    private Random random;

    private boolean[] keyDown = new boolean[4];

    private DIRECTION facingDirection = DIRECTION.RIGHT;

    GameObject temporaryObject = null;
    public void findPlayer(){
        for(int i = 0; i < renderer.objects.size(); i++) {
            if (renderer.objects.get(i).getId() == ID.Player) {
                temporaryObject = renderer.objects.get(i);
                break;
            }
        }
    }

    public KeyInput(RendererHandler renderer)
    {
        this.renderer = renderer;

        keyDown[0] = false;
        keyDown[1] = false;
        keyDown[2] = false;
        keyDown[3] = false;
    }

    public void keyPressed(KeyEvent event)
    {
        int key = event.getKeyCode();

        if(key == KeyEvent.VK_ESCAPE)
        {
            if(Game.paused)
            {
                Game.paused = false;
                Game.windowSTATE = STATE.Game;
            }else
            {
                Game.paused = true;
                Game.windowSTATE = STATE.Paused;
            }
        }

        if(key == KeyEvent.VK_E)
        {
            if(Game.paused)
            {
                Game.paused = false;
                Game.windowSTATE = STATE.Game;
            }else
            {
                Game.paused = true;
                Game.windowSTATE = STATE.Upgrades;
            }
        }

        if(key == KeyEvent.VK_F)
        {
            HUD.health -= 10;
        }

        if(key == KeyEvent.VK_L) {
            System.out.println(renderer.enemies.size());
        }


        for(int i = 0; i < renderer.objects.size(); i++)
        {
            GameObject temporaryObject = renderer.objects.get(i);

            if (temporaryObject.getId() == ID.Player)
            {
                if (key == KeyEvent.VK_W)
                {
                    temporaryObject.setVelocityY(-5);
                    keyDown[0] = true;
                    facingDirection = DIRECTION.UP;
                }
                if (key == KeyEvent.VK_S)
                {
                    temporaryObject.setVelocityY(5);
                    keyDown[1] = true;
                    facingDirection = DIRECTION.DOWN;
                }
                if (key == KeyEvent.VK_A)
                {
                    temporaryObject.setVelocityX(-5);
                    keyDown[2] = true;
                    facingDirection = DIRECTION.LEFT;
                }
                if (key == KeyEvent.VK_D)
                {
                    temporaryObject.setVelocityX(5);
                    keyDown[3] = true;
                    facingDirection = DIRECTION.RIGHT;
                }
            }
        }
    }

    public void keyReleased(KeyEvent event)
    {
        int key = event.getKeyCode();

        for(int i = 0; i < renderer.objects.size(); i++)
        {
            GameObject temporaryObject = renderer.objects.get(i);

            if(temporaryObject.getId() == ID.Player)
            {
                if(key == KeyEvent.VK_W)
                {
                    keyDown[0] = false;
                }
                if(key == KeyEvent.VK_S)
                {
                    keyDown[1] = false;
                }
                if(key == KeyEvent.VK_A)
                {
                    keyDown[2] = false;
                }
                if(key == KeyEvent.VK_D)
                {
                    keyDown[3] = false;
                }

                if(!keyDown[0] && !keyDown[1])
                {
                    temporaryObject.setVelocityY(0);
                }

                if(!keyDown[2] && !keyDown[3])
                {
                    temporaryObject.setVelocityX(0);
                }
            }
        }
    }
}
