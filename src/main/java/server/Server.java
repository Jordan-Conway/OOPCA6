package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    public void start(){
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

}
