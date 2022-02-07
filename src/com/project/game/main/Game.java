package com.project.game.main;

import com.project.game.identifiers.ID;
import com.project.game.identifiers.STATE;
import com.project.game.network.GameServer;
import com.project.game.objects.Block;
import com.project.game.objects.Coin;
import com.project.game.objects.Crate;
import com.project.game.objects.Health;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;


/*

*/

// Game is part of the Canvas, and also uses threading
public class Game extends Canvas implements Runnable{

    // The size of the window, using maths we can keep it to a 16 by 9 aspect ratio.
    public static final int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;

    // Set initial state of the window
    public static STATE windowSTATE = STATE.Menu;

    private STATE currentSTATE;
    private STATE lastSTATE = currentSTATE;

    // Set initial level
    public String currentLevel = "/level_zero.png";

    // Create a thread
    private Thread thread;
    private boolean running = false;

    // Declare instances of classes
    private RendererHandler renderer;
    private HUD hud;
    private UI ui;
    private KeyInput keyInput;
    private Loading loading;
    private Camera camera;
    private GameServer gameServer;

    // For now current level is null
    private BufferedImage level = null;

    // Game is not paused
    static boolean paused = false;

    // Do not use DEBUG mode
    public static boolean DEBUG = false;

    // Multiplayer has not been selected yet, so it is false
    public boolean multiplayerEnabled = false;

    public Game() {
        // Create new instances of classes
        renderer = new RendererHandler();
        camera = new Camera(0, 0);
        ui = new UI(this, renderer, camera);
        keyInput = new KeyInput(renderer);
        loading = new Loading(this, renderer);
        hud = new HUD();

        // Add all of these input listeners, so the program can actually process any inputs
        this.addKeyListener(keyInput);
        this.addMouseListener(ui);
        this.addMouseMotionListener(ui);

        // Create a new window, and apply it to this class
        new Window(WIDTH, HEIGHT, "Game", this);
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

    // Returns a random number, between the maximum and minimum values
    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    // Returns a random number from an array
    public static int getRandomIntFromArray(int[] array) {
        int random = new Random().nextInt(array.length);
        return array[random];
    }

    // The class utilises threading, this is the function used to actually start it
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    // In case we need to stop the thread
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
        // Set the ticks per second.
        double ticksPerSecond = 60.0;
        double ns = 1000000000 / ticksPerSecond;
        double accumulatedFrameTime = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        // As long as the game is running, this forms the basis of the game loop
        while(running){
            long now = System.nanoTime();
            accumulatedFrameTime += (now - lastTime) / ns;
            lastTime = now;

            // If accumulatedFrameTime is less than one, we can call these functions
            while(accumulatedFrameTime >= 1){
                tick();
                render();
                frames++;
                accumulatedFrameTime = 0;
            }

            // Print the amount of frames per second
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    private void tick() {
        // Based on the current window state, what should be ticked? Provided that game is not paused
        if(!paused) {
            if (windowSTATE == STATE.Game) {
                //hud.tick();
                renderer.tick();
                for(int i = 0; i < renderer.objects.size(); i++) {
                    if (renderer.objects.get(i).getId() == ID.Player) {
                        // Pass the player as an object to the camera
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

        // Create a buffer strategy
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if(bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        // Create the graphics variable
        // This can then be passed to be used to all of the other render functions
        Graphics graphics = bufferStrategy.getDrawGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;

        // Create the background
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        // Add functionality to the camera, make sure that what is rendered also moves
        graphics2D.translate(-camera.getX(), -camera.getY());

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        // Based on the current state of the game, what should be rendered? Provided that game is not paused
        if(!paused) {
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

        // Again making sure the camera moves
        graphics2D.translate(camera.getX(), camera.getY());

        // This has to be outside the translate functionality, as we want it to stay in the same place on the screen
        if(!paused){
            if(windowSTATE == STATE.Game){
                hud.render(graphics);
            }
        }
        graphics.dispose();
        bufferStrategy.show();
    }

    /*
        The loadLevel function
        Take in an image as an input
        For the entire height and width of it, process every pixel
        Interpret the individual RGB values of the pixel
        If the pixels RGB values match an if statement, we can interpret that pixel as an object
        Then add the object into the game
    */
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

    // We can use this to limit expanding or contracting values to not go beyond certain amounts
    // This is useful to prevent the user from easily breaking the game
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

    // Initialise the game
    public static void main(String args[]) {
        new Game();
    }
}
