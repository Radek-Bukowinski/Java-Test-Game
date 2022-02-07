package com.project.game.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayer;

    private boolean running = false;

    private ServerSideConnection player1;

    public GameServer(){
        // There are no players yet, so this has to be 0
        numberOfPlayer = 0;
        try{
            // Create a new server socket
            serverSocket = new ServerSocket(8008);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void stopServer(){
        try {
            running = false;
            serverSocket.close();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void acceptConnections(){
        try{
            // Accept an incoming connection to the server
            Socket socket = serverSocket.accept();
            // Create a new server side connection, which is just the data streams essentially.
            ServerSideConnection serverSideConnection = new ServerSideConnection(socket, numberOfPlayer);
            // Assign a connection to the player
            player1 = serverSideConnection;
            Thread thread = new Thread(serverSideConnection);
            thread.start();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable{
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket inputSocket, int id){
            socket = inputSocket;
            playerID = id;
            try{
                // Create in and out streams of data, so that client and server can communicate
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ioException){
                ioException.printStackTrace();
            }
        }

        @Override
        public void run() {
            running = true;
            try{
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();
                while (running){}
            } catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}
