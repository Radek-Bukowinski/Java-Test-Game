package com.project.game.framework;

import com.project.game.entity.Player;
import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{
    public static final int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;

    public static STATE windowSTATE = STATE.Menu;

    private Thread thread;
    private boolean running = false;

    private Random random = new Random();
    private RendererHandler renderer;
    private Spawner spawner;
    private HUD hud;
    private UI ui;
    private KeyInput keyInput;
    private Loading loading;

    static int frames = 0;

    static boolean paused = false;
    private static int ticks = 0;


    public static int getFrames() {
        ticks++;
        if(ticks > 1000) {
            return frames;
        }
        return 1000;
    }

    public Game() {
        renderer = new RendererHandler();

        ui = new UI(this, renderer);
        keyInput = new KeyInput(renderer);

        this.addKeyListener(keyInput);
        this.addMouseListener(ui);
        this.addMouseMotionListener(ui);

        new Window(WIDTH, HEIGHT, "Game", this);
        loading = new Loading(this, renderer);

        hud = new HUD();
        spawner = new Spawner(renderer, hud);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void run() {
        this.requestFocus();

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();


        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >=1) {
                tick();
                delta--;
            }

            if(running) {
                render();
            }

            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }

        stop();
    }

    private void tick() {
        if(!paused) {
            if (windowSTATE == STATE.Game) {
                hud.tick();
                spawner.tick();
                renderer.tick();
                //effect.tick();
            }
            if (windowSTATE == STATE.Menu || windowSTATE == STATE.Info) {
                ui.tick();
                renderer.tick();
            }
            if (windowSTATE == STATE.Loading) {
                loading.tick();
                renderer.tick();
            }
        }else {
            ui.tick();
        }
    }

    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if(bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        if(!paused) {
            if (windowSTATE == STATE.Game) {
                hud.render(graphics);
                //effect.render(graphics);
            }

            if (windowSTATE == STATE.Menu || windowSTATE == STATE.Death || windowSTATE == STATE.Info) {
                ui.render(graphics);
            }

            if (windowSTATE == STATE.Loading) {
                loading.render(graphics);
            }
        }else {
            windowSTATE = STATE.Paused;
            hud.render(graphics);
            ui.render(graphics);
        }
        renderer.render(graphics);
        graphics.dispose();
        bufferStrategy.show();
    }

    public static int constrain(int value, int minimum, int maximum) {
        if(value >= maximum) {
            return value = maximum;
        }
        else if(value <= minimum) {
            return value = minimum;
        }
        else {
            return value;
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}