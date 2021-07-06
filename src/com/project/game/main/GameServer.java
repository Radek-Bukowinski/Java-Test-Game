import java.io.*;
import java.net.*;

public class GameServer {
  private ServerSocket serverSocket;
  private int playerCount;
  
  public GameServer(){
    playerCount = 0;
    try{
      while(playerCount < 4){
        serverSocket = new serverSocket(51734);
      } catch(IOException ioException){
        ioException.printStackTrace();
      }
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
  public static void main(String[] args){
    GameServer gameServer = new GameServer();
    gameServer.acceptConnection();
  }
}
