import Classes.Gemstone;
import Comparators.GemstoneCaratComparator;
import Comparators.GemstoneNameComparator;
import Enums.Clarity;
import Enums.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static Classes.Gemstone.printGemstones;

public class App {
    public Socket socket;
    public OutputStream os;
    public PrintWriter out;
    public Scanner inStream;
    public Scanner scanner = new Scanner(System.in);
    public static Gson gsonParser = new Gson();

    private Set<Integer> cache;

    private RequestHandler requestHandler;

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
            this.requestHandler = new RequestHandler(out, inStream);
            this.cache = getCache();
        }
        catch (IOException e){
            System.out.println("Server is not running");
            return;
        }

        this.menu();
    }

    private Set<Integer> getCache(){
        return gsonParser.fromJson(requestHandler.makeRequest(RequestType.GETIDS), TypeToken.getParameterized(Set.class, Integer.class).getType());
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
                System.out.println("5. List all Gemstones sorted by carats");
                input = this.scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> printAllGemstones(new GemstoneNameComparator());
                    case 2 -> printGemstoneById();
                    case 3 -> deleteGemstoneById();
                    case 4 -> insertGemstone();
                    case 5 -> printAllGemstones(new GemstoneCaratComparator());
                    case 0 -> exit = true;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input, please try again");
            }
        }
    }

    private ArrayList<Gemstone> getAllGemstones(){
        return gsonParser.fromJson(requestHandler.makeRequest(RequestType.GETALL), TypeToken.getParameterized(ArrayList.class, Gemstone.class).getType());
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

        if(this.cache.contains(input)){
            Gemstone result = gsonParser.fromJson(requestHandler.makeRequest(RequestType.GETBYID, input), Gemstone.class);

            if(result != null){ //if a result was found
                System.out.println(result);
                return;
            }
        }
        System.out.println("No gemstone was found with id " + input);
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

        String response = gsonParser.fromJson(requestHandler.makeRequest(RequestType.INSERT, gemstoneToInsertJSON), String.class);

        System.out.println(response);
    }

    public void deleteGemstoneById(){
        int input;
        while (true){
            try{
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();

                String response = gsonParser.fromJson(requestHandler.makeRequest(RequestType.DELETE, input), String.class);

                System.out.println(response);
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input please try again");
            }
        }
    }
}
