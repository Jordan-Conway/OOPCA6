package Client;

import Classes.Request;
import Enums.RequestType;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.util.Scanner;

public class RequestHandler {
    private static final Gson gsonParser = new Gson();
    private final PrintWriter out;
    private final Scanner in;

    public RequestHandler(PrintWriter out, Scanner in){
        this.out = out;
        this.in = in;
    }

    public String makeRequest(RequestType requestType){
        Request request = new Request(requestType);
        String requestJson = requestToJson(request);
        sendRequestToServer(requestJson);
        return in.nextLine();
    }

    public String makeRequest(RequestType requestType, Object parameter){
        Request request = new Request(requestType, parameter.toString());
        String requestJson = requestToJson(request);
        sendRequestToServer(requestJson);
        return in.nextLine();
    }

    private String requestToJson(Request request){
        return gsonParser.toJson(request);
    }

    private void sendRequestToServer(String requestJson){
        out.write(requestJson + "\n");
        out.flush();
    }

}
