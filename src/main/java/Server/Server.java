package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    public void start(){
        if(isRunning()){
            try(ServerSocket ss = new ServerSocket(8080)){
                System.out.println("Server is started");
                while (true){
                    Socket socket = ss.accept();
                    System.out.println("Creating thread");
                    Thread newThread = new Thread(new ClientHandler(socket));
                    newThread.start();
                    System.out.println("Total number of threads: " + java.lang.Thread.activeCount());
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("MySQL is not running");
        }
    }

    public static boolean isRunning(){
        boolean running;
        try{
            Socket socket = new Socket("127.0.0.1", 3306);
            running = true;
            socket.close();
        }
        catch (IOException e){
            running = false;
        }
        return running;
    }

}
