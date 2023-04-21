package Server;

import Classes.Gemstone;
import Classes.Request;
import DAO.GemstoneDAO;

import com.google.gson.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Set;


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
                if(command.equals("")){
                    break;
                }
                Request request = gsonParser.fromJson(command, Request.class);
                switch (request.getRequestType()){
                    case GETALL -> returnAll();
                    case GETBYID -> {
                        try {
                            int id = Integer.parseInt(request.getParameter());
                            returnById(id);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("No id was given");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Id was given");
                        }
                    }
                    case INSERT -> {
                        Gemstone gemstone = gsonParser.fromJson(request.getParameter(), Gemstone.class);
                        insert(gemstone);
                    }
                    case DELETE -> {
                        int IdToDelete = gsonParser.fromJson(request.getParameter(), Integer.class);
                        delete(IdToDelete);
                    }
                    case GETIDS -> getIds();
                    default -> System.out.println("Invalid command");

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
        socketWriter.println(resultJSON);
    }

    public void insert(Gemstone gemstone){
        boolean success = dao.insertGemstone(gemstone);
        String response;
        if(success){
            response = "Insert was successful";
        }
        else{
            response = "Insert failed";
        }
        socketWriter.println(gsonParser.toJson(response));
    }

    public void delete(int id){
        boolean success = dao.deleteGemstoneById(id);
        String response;
        if(success){
            response = "Delete was successful";
        }
        else{
            response = "Delete failed";
        }
        socketWriter.println(gsonParser.toJson(response));
    }

    public void getIds(){
        Set<Integer> ids = dao.getIds();
        String idsJSON = gsonParser.toJson(ids);

        socketWriter.println(idsJSON);
    }

}
