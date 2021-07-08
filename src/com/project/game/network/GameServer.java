package com.project.game.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
  private ServerSocket serverSocket;
  private int playerCount;
  
  public GameServer(){
    playerCount = 0;
    try{
      while(playerCount < 4){
        serverSocket = new ServerSocket(51734);
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
  
  public void acceptConnection(){
    try {
      while(playerCount < 4){
        Socket socket = serverSocket.accept();
        playerCount++;
      }
    } catch(IOException ioException){
      ioException.printStackTrace();
     
    }
  }
  
  private class ServerSideConnection {
  
  }
}
