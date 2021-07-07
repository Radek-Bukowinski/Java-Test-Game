import java.io.*;
import java.net.*;

public class GameServer {
  private ServerSocket serverSocket;
  private int falsePlayerCount;
  private int playerCount;
  private ServerSideConnection player1;
  private ServerSideConnection player2;
  private ServerSideConnection player3;
  private ServerSideConnection player4;
  
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
        ServerSideConnection serverSideConnection = new ServerSideConnection(socket, playerCount);
        if(playerCount == 1){
            player1 = serverSideConnection;
        }
        else if(playerCount == 2){
            player2 = serverSideConnection;
        }
        else if(playerCount == 3){
            player3 = serverSideConnection;
        }
        else if(playerCount == 4){
            player4 = serverSideConnection;
        }
        
        Thread thread = new Thread(serverSideConnection);
        thread.start();
      }
    } catch(IOException ioException){
      ioException.printStackTrace();
     
    }
  }
  
  private class ServerSideConnection() implements Runnable{
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private int playerID;
    
    public ServerSideConnection(Socket socketIn, int id){
        socket = socketIn;
        playerID = id;
      
      try{
          dataInputStream = new DataInputStream(socket.getInputStream());
          dataOutputStream = new DataOutputStream(socket.getOutputStream());
      } catch (IOException ioException) {
          ioException.printStackTrace();
      }
      
      public void run(){
        try {
          dataOutputStream.writeInt(playerID);
          dataOutputStream.flush();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
      }
    }
  }
  
  public static void main(String[] args){
    GameServer gameServer = new GameServer();
    gameServer.acceptConnection();
  }
}
