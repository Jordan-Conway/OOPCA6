import DAO.GemstoneDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static Socket socket;
    public static OutputStream os;
    public static PrintWriter out;
    public static Scanner inStream;
    public static Scanner scanner = new Scanner(System.in);
    public static GemstoneDAO dao = new GemstoneDAO();
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
            menu();

            dao.deleteGemstoneById(1);
            System.out.println("Record deleted");
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

    public static void menu(){
        int input;
        boolean exit = false;
        while(!exit){
            try{
                System.out.println("Options");
                System.out.println("1. List all Gemstones");
                System.out.println("2. Search Gemstone By Id");
                System.out.println("3. Delete Gemstone By Id");
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> printAllGemstones();
                    case 2 -> printGemstoneById();
                    case 3 -> deleteGemstoneById();
                    case 0 -> exit = true;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input, please try again");
            }
        }
    }

    public static void printAllGemstones(){
        out.write("Get All\n");
        out.flush();

        while(inStream.hasNextLine()){
            System.out.println(inStream.nextLine());
        }

    }

    public static void printGemstoneById(){
        int input;
        while (true){
            try{
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();
                if(input != -1){
                    System.out.println(dao.findGemstoneById(input));
                }
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input please try again");
            }
        }
    }

    public static void deleteGemstoneById(){
        int input;
        while (true){
            try{
                System.out.println("Enter the id to search for or enter -1 to exit");
                input = scanner.nextInt();
                scanner.nextLine();
                if(input != -1){
                    if(dao.deleteGemstoneById(input)){
                        System.out.println("Deleted gemstone with id " + input);
                    }
                    else{
                        System.out.println("Failed to delete gemstone with id " + input);
                    }
                }
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Incorrect input please try again");
            }
        }
    }
}
