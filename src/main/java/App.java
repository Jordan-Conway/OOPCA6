import Classes.Gemstone;
import Classes.Request;
import Enums.Clarity;
import Enums.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

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
                    case 3 -> deleteGemstoneById();
                    case 4 -> insertGemstone();
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

    public void insertGemstone(){
        //TODO input validation
        System.out.println("Enter the gemstone name");
        String name = scanner.nextLine();
        System.out.println("Enter the number of carats");
        float carats = scanner.nextFloat();
        scanner.nextLine();
        System.out.println("Enter the colour of the gemstone");
        String colour = scanner.nextLine();
        System.out.println("Choose a clarity");
        List<Clarity> clarityList = Arrays.asList(Clarity.values());
        for(int i=0; i<clarityList.size(); i++){
            System.out.println(i + 1 + ": " + clarityList.get(i));
        }
        Clarity clarity = clarityList.get(scanner.nextInt() - 1);

        Gemstone gemstoneToInsert = new Gemstone(0, name, carats, colour, clarity);

        String gemstoneToInsertJSON = gsonParser.toJson(gemstoneToInsert);

        Request request = new Request(RequestType.INSERT, gemstoneToInsertJSON);
        String requestJSON = gsonParser.toJson(request);

        out.write(requestJSON + "\n");
        out.flush();

        String response = gsonParser.fromJson(inStream.nextLine(), String.class);

        System.out.println(response);
    }

    public void deleteGemstoneById(){
        int input;
        while (true){
            try{
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();
                Request request = new Request(RequestType.DELETE, Integer.toString(input));
                out.println(gsonParser.toJson(request));

                String response = gsonParser.fromJson(inStream.nextLine(), String.class);

                System.out.println(response);
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input please try again");
            }
        }
    }
}
