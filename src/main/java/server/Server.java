package server;

import Classes.Gemstone;
import DAO.GemstoneDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Server {
    Socket socket;
    Scanner in;
    OutputStream os;
    PrintWriter out;
    GemstoneDAO dao;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start(){
        try(ServerSocket ss = new ServerSocket(8080)){
            System.out.println("Server is started");

            while (true){
                Socket socket = ss.accept();
                System.out.println("New connection");

                this.socket = socket;
                this.in = new Scanner(socket.getInputStream());
                this.os = socket.getOutputStream();
                this.out = new PrintWriter(this.os, true);
                this.dao = new GemstoneDAO();

                String command = this.in.nextLine();
                System.out.println(command);
                if(command.equals("Get All")){
                    List<Gemstone> gemstones = dao.findAllGemstones();
                    for(Gemstone gemstone: gemstones) {
                        out.println(gemstone);
                    }
                }
                socket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
