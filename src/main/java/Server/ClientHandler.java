package Server;

import Classes.Gemstone;
import Classes.Request;
import Classes.Response;
import DAO.GemstoneDAO;

import Enums.ResponseStatus;
import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Set;


public class ClientHandler implements Runnable{
    private Set<Integer> cache;
    public static GemstoneDAO dao = new GemstoneDAO();
    public static Gson gsonParser = new Gson();
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket socket;
    String notFound = "Id not found in database";

    public ClientHandler(Socket socket){
        try{
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            this.socketReader = new BufferedReader(isReader);

            OutputStream os = socket.getOutputStream();
            this.socketWriter = new PrintWriter(os, true);

            this.socket = socket;

            this.cache = getCache();
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
                        try{
                            int id = gsonParser.fromJson(request.getParameter(), Integer.class);
                            returnById(id);
                        }
                        catch (JsonSyntaxException e){
                            System.out.println("Malformed parameter");
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
        Response response = new Response(ResponseStatus.OK, gsonParser.toJson(gemstones));
        String resultJSON = gsonParser.toJson(response);
        socketWriter.println(resultJSON);
    }

    public void returnById(int id){
        cache = getCache();
        if(cache.contains(id)){
            Gemstone gemstone = dao.findGemstoneById(id);
            Response response;
            if(gemstone != null){
                response = new Response(ResponseStatus.OK, gsonParser.toJson(gemstone));
                String resultJSON = gsonParser.toJson(response);
                socketWriter.println(resultJSON);
            }
            else{
                returnNotFound();
            }
        }
        else{
            returnNotFound();
        }
    }

    public void insert(Gemstone gemstone){
        boolean success = dao.insertGemstone(gemstone);
        Response response;
        if(success){
            response = new Response(ResponseStatus.OK, gsonParser.toJson("Insert was successful"));
            cache = getCache();
        }
        else{
            response = new Response(ResponseStatus.ERROR, gsonParser.toJson("Insert failed"));
        }
        socketWriter.println(gsonParser.toJson(response));
    }

    public void delete(int id){
        boolean success = dao.deleteGemstoneById(id);
        Response response;
        if(success){
            response = new Response(ResponseStatus.OK, gsonParser.toJson("Delete was successful"));
            cache = getCache();
        }
        else{
            response = new Response(ResponseStatus.ERROR, gsonParser.toJson("Delete failed"));
        }
        socketWriter.println(gsonParser.toJson(response));
    }

    public void getIds(){
        Set<Integer> ids = dao.getIds();
        Response response = new Response(ResponseStatus.OK, gsonParser.toJson(ids));
        socketWriter.println(gsonParser.toJson(response));
    }

    private Set<Integer> getCache(){
        return dao.getIds();
    }

    private void returnNotFound(){
        Response response = new Response(ResponseStatus.ERROR, gsonParser.toJson(notFound));
        String responseJSON = gsonParser.toJson(response);
        socketWriter.println(responseJSON);
    }

}
