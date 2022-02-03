package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.network.GameServer;
import com.project.game.objects.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;


/*

*/

public class Game extends Canvas implements Runnable{
    public static final int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;

    public static STATE windowSTATE = STATE.Menu;
    //public static MODE gameMODE = MODE.Other;

    private STATE currentSTATE;
    private STATE lastSTATE = currentSTATE;

    public String currentLevel = "/level_zero.png";

    private Thread thread;
    private boolean running = false;

    private GameServer gameServer;

    private Random random = new Random();
    private RendererHandler renderer;
    private Spawner spawner;
    private HUD hud;
    private UI ui;
    private KeyInput keyInput;
    private Loading loading;
    private Camera camera;

    private BufferedImage level = null;
    private BufferedImage background = null;

    static int frames = 0;

    static boolean paused = false;
    private static int ticks = 0;

    public boolean DEBUG = false;

    public boolean multiplayerEnabled = false;

    Timer timer;

    //Runner runner = new Runner();

    public boolean isInitialised = false;

    public Game() {

        renderer = new RendererHandler();
        camera = new Camera(0, 0);
        ui = new UI(this, renderer, camera);
        keyInput = new KeyInput(renderer);

        this.addKeyListener(keyInput);
        this.addMouseListener(ui);
        this.addMouseMotionListener(ui);


        new Window(WIDTH, HEIGHT, "Game", this);
        loadBackground();
        loading = new Loading(this, renderer);

        hud = new HUD();
    }

    // Create a new server
    public void initialiseMultiplayer(){
        GameServer gameServer = new GameServer();
    }

    // Open the socket to accept any incoming connection.
    public void openConnections(){
        gameServer.acceptConnections();
    }

    // Here we load an image, and the call loadLevel to interpret pixels as objects within the game.
    public void initialiseLevel(String path){
        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        level = bufferedImageLoader.loadImage(path);
        loadLevel(level);
    }

    public void loadBackground(){
        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        background = bufferedImageLoader.loadImage("/background.png");
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static int getRandomIntFromArray(int[] array) {
        int random = new Random().nextInt(array.length);
        return array[random];
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

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double ticksPerSecond = 60.0;
        double ns = 1000000000 / ticksPerSecond;
        double accumulatedFrameTime = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running){
            long now = System.nanoTime();
            accumulatedFrameTime += (now - lastTime) / ns;
            lastTime = now;
            while(accumulatedFrameTime >= 1){
                tick();
                render();
                frames++;
                accumulatedFrameTime = 0;
            }

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(frames);
                frames = 0;
            }
        }
    }

    private void tick() {
        if(!paused) {
            if (windowSTATE == STATE.Game) {
                //hud.tick();
                renderer.tick();
                for(int i = 0; i < renderer.objects.size(); i++) {
                    if (renderer.objects.get(i).getId() == ID.Player) {
                        camera.tick(renderer.objects.get(i));
                    }
                }
            }
            if (windowSTATE == STATE.Menu || windowSTATE == STATE.Info || windowSTATE == STATE.ModeSelect|| windowSTATE == STATE.MultiplayerSelect) {
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
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        //graphics.drawImage(background, WIDTH, HEIGHT, null);

        graphics2D.translate(-camera.getX(), -camera.getY());


        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        if(!paused) {
            if (windowSTATE == STATE.Game) {
                //hud.render(graphics);
            }

            if (windowSTATE == STATE.Menu || windowSTATE == STATE.Death || windowSTATE == STATE.Info || windowSTATE == STATE.ModeSelect || windowSTATE == STATE.MultiplayerSelect) {
                ui.render(graphics);
            }

            if (windowSTATE == STATE.Loading) {
                loading.render(graphics);
            }
        }else {
            windowSTATE = STATE.Paused;
            //hud.render(graphics);
            ui.render(graphics);
        }
        renderer.render(graphics);
        graphics2D.translate(camera.getX(), camera.getY());
        if(!paused){
            if(windowSTATE == STATE.Game){
                hud.render(graphics);
            }
        }
        graphics.dispose();
        bufferStrategy.show();
    }

    private void loadLevel(BufferedImage bufferedImage){
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for(int xx = 0; xx < width; xx++){
            for(int yy = 0; yy < height; yy++){
                int pixel = bufferedImage.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                // Gray walls
                if(red == 128 && green == 128 && blue == 128){
                    renderer.addObject(new Block(xx * 32, yy * 32, 100, ID.Block, true));
                    renderer.addCollidible(new Block(xx * 32, yy * 32, 100, ID.Block, true));

                }

                //Gray walls as well, but these are out of bounds, and therefore we do not need to worry about assigning collision to them.
                if(red == 255 && blue == 110){
                    renderer.addObject(new Block(xx * 32, yy * 32, 100, ID.Block, false));
                }

                // Brown crates
                if(red == 127 && green == 51 && blue == 0){
                    renderer.addObject(new Crate(xx * 32, yy * 32, 100, ID.Crate, true));
                    renderer.addCollidible(new Crate(xx * 32, yy * 32, 100, ID.Crate, true));
                }
                //Coin
                if(red == 255 && green == 216 && blue == 0){
                    renderer.addObject(new Coin(xx * 32, yy * 32, 100, ID.Coin, true));
                }
                //Health
                if(red == 0 && green == 38 && blue == 255){
                    renderer.addObject(new Health(xx * 32, yy * 32, 100, ID.Health, true));
                }
            }
        }
    }

    // We can use this t
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
