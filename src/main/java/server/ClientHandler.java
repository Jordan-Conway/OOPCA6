package server;

import Classes.Gemstone;
import DAO.GemstoneDAO;

import com.google.gson.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class ClientHandler implements Runnable{
    public static GemstoneDAO dao = new GemstoneDAO();
    public static Gson gsonParser = new Gson();
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket socket;

    public ClientHandler(Socket socket){
        try{
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            this.socketReader = new BufferedReader(isReader);

            OutputStream os = socket.getOutputStream();
            this.socketWriter = new PrintWriter(os, true);

            this.socket = socket;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        String command;
        try{
            while((command = socketReader.readLine()) != null){
                System.out.println("Read command: " + command);
                if(command.equals("Get All")){
                    returnAll();
                }
                else if(command.startsWith("Get By Id: ")){
                    try{
                        int id = Integer.parseInt(command.split("Get By Id: ")[1]);
                        returnById(id);
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("No id was given");
                    }
                    catch (NumberFormatException e){
                        System.out.println("Invalid Id was given");
                    }
                }
                else{
                    System.out.println("Invalid command");
                }
            }
            this.socket.close();
        }
        catch (SocketException e){ //Thrown when app is terminated
            System.out.println("Connection lost");
            try{
                this.socket.close();
                Thread.currentThread().interrupt();
            }
            catch (IOException e2){
                e2.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void returnAll(){
        List<Gemstone> gemstones = dao.findAllGemstones();
        String resultJSON = gsonParser.toJson(gemstones);

        socketWriter.println(resultJSON);
    }

    public void returnById(int id){
        Gemstone gemstone = dao.findGemstoneById(id);
        String resultJSON = gsonParser.toJson(gemstone);
        System.out.println(resultJSON);
        socketWriter.println(resultJSON);
    }
}
