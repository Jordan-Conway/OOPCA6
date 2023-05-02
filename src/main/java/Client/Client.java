package Client;

import Classes.Gemstone;
import Classes.Response;
import Comparators.GemstoneCaratComparator;
import Comparators.GemstoneIDComparator;
import Comparators.GemstoneNameComparator;
import Enums.Clarity;
import Enums.RequestType;
import Enums.ResponseStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static Classes.Gemstone.printGemstones;

public class Client {
    public Socket socket;
    public OutputStream os;
    public PrintWriter out;
    public Scanner inStream;
    public Scanner scanner = new Scanner(System.in);
    public static Gson gsonParser = new Gson();

    private RequestHandler requestHandler;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try{
            socket = new Socket("localhost", 8080);
            os = socket.getOutputStream();
            out = new PrintWriter(os, true);
            inStream = new Scanner(socket.getInputStream());
            this.requestHandler = new RequestHandler(out, inStream);
        }
        catch (IOException e){
            System.out.println("Server is not running");
            return;
        }

        this.menu();
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
                System.out.println("4. Add a new Gemstone");
                System.out.println("5. List all Gemstones sorted by name");
                System.out.println("6. List all Gemstones sorted by carats");
                input = this.scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> printAllGemstones(new GemstoneIDComparator());
                    case 2 -> printGemstoneById();
                    case 3 -> deleteGemstoneById();
                    case 4 -> insertGemstone();
                    case 5 -> printAllGemstones(new GemstoneNameComparator());
                    case 6 -> printAllGemstones(new GemstoneCaratComparator());
                    case 0 -> exit = true;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input, please try again");
            }
        }
    }

    private ArrayList<Gemstone> getAllGemstones(){
        String responseAsJSON = requestHandler.makeRequest(RequestType.GETALL);
        Response response = gsonParser.fromJson(responseAsJSON, Response.class);
        if(response.getResponseStatus() == ResponseStatus.OK){
            return gsonParser.fromJson(response.getResponse(), TypeToken.getParameterized(ArrayList.class, Gemstone.class).getType());
        }
        else{
            return new ArrayList<>();
        }
    }

    public void printAllGemstones(Comparator<Gemstone> comparator){
        ArrayList<Gemstone> gemstones = getAllGemstones();

        gemstones.sort(comparator);

        printGemstones(gemstones);
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

        String responseAsJSON = requestHandler.makeRequest(RequestType.GETBYID, input);
        Response response = gsonParser.fromJson(responseAsJSON, Response.class);
        if(response.getResponseStatus() == ResponseStatus.OK){
            System.out.println(gsonParser.fromJson(response.getResponse(), Gemstone.class));
        }
        else{
            System.out.println("No gemstone was found with id " + input);
        }

    }

    public void insertGemstone(){
        //TODO input validation
        boolean validInput = false;
        while(!validInput){
            try{
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

                validInput = true;

                Gemstone gemstoneToInsert = new Gemstone(0, name, carats, colour, clarity);

                String gemstoneToInsertJSON = gsonParser.toJson(gemstoneToInsert);

                Response response = gsonParser.fromJson(requestHandler.makeRequest(RequestType.INSERT, gemstoneToInsertJSON), Response.class);

                System.out.println(gsonParser.fromJson(response.getResponse(), String.class));
            }catch (InputMismatchException e){
                System.out.println("Invalid input, please try again");
                validInput = false;
                if(scanner.hasNextLine()){
                    scanner.nextLine();
                }
            }
        }
    }

    public void deleteGemstoneById(){
        int input;
        while (true){
            try{
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();

                Response response = gsonParser.fromJson(requestHandler.makeRequest(RequestType.DELETE, input), Response.class);

                System.out.println(gsonParser.fromJson(response.getResponse(), String.class));
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input please try again");
            }
        }
    }
}
