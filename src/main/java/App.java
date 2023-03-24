import Classes.Gemstone;
import Classes.Request;
import Enums.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public Socket socket;
    public OutputStream os;
    public PrintWriter out;
    public Scanner inStream;
    public Scanner scanner = new Scanner(System.in);
    public static Gson gsonParser = new Gson();

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public void start() {
        try{
            socket = new Socket("localhost", 8080);
            os = socket.getOutputStream();
            out = new PrintWriter(os, true);
            inStream = new Scanner(socket.getInputStream());
        }
        catch (IOException e){
            System.out.println("Server is not running");
            return;
        }

        if(isRunning()){
            this.menu();
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

    public void menu(){
        int input;
        boolean exit = false;
        while(!exit){
            try{
                System.out.println("Options");
                System.out.println("1. List all Gemstones");
                System.out.println("2. Search Gemstone By Id");
                System.out.println("3. Delete Gemstone By Id");
                input = this.scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> printAllGemstones();
                    case 2 -> printGemstoneById();
//                    case 3 -> deleteGemstoneById();
                    case 0 -> exit = true;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input, please try again");
            }
        }
    }

    public void printAllGemstones(){
        Request request = new Request(RequestType.GETALL);
        String requestJson = gsonParser.toJson(request);
        out.write(requestJson + "\n");
        out.flush();

        ArrayList<Gemstone> gemstones;
        Type gemstoneTypeToken = new TypeToken<ArrayList<Gemstone>>(){}.getType();
        gemstones = gsonParser.fromJson(inStream.nextLine(), gemstoneTypeToken);

        for(Gemstone gemstone: gemstones){
            System.out.println(gemstone);
        }
    }

    public void printGemstoneById(){
        int input;
        while (true) {
            try {
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();
                break; //If we get this far, we have a valid input and can stop asking for new inputs
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input please try again");
            }
        }
        if (input == -1) {
            return;
        }

        Request request = new Request(RequestType.GETBYID, Integer.toString(input));
        String requestJSON = gsonParser.toJson(request);
        out.write(requestJSON + "\n");
        out.flush();

        Gemstone result = gsonParser.fromJson(inStream.nextLine(), Gemstone.class);

        if(result != null){ //if a result was found
            System.out.println(result);
        }
        else{ //Nothing was found
            System.out.println("No gemstone was found with id " + input);
        }
    }

//    public static void deleteGemstoneById(){
//        int input;
//        while (true){
//            try{
//                System.out.println("Enter the id to search for or enter -1 to exit");
//                input = scanner.nextInt();
//                scanner.nextLine();
//                if(input != -1){
//                    if(dao.deleteGemstoneById(input)){
//                        System.out.println("Deleted gemstone with id " + input);
//                    }
//                    else{
//                        System.out.println("Failed to delete gemstone with id " + input);
//                    }
//                }
//                break;
//            }
//            catch (InputMismatchException e){
//                System.out.println("Incorrect input please try again");
//            }
//        }
//    }
}
