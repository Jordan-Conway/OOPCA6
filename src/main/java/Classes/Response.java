package Classes;

import Enums.ResponseStatus;

public class Response {
    private final ResponseStatus responseStatus;
    private final String response;

    public Response(ResponseStatus responseStatus, String responseJSON){
        this.responseStatus = responseStatus;
        this.response = responseJSON;
    }

    public ResponseStatus getResponseStatus(){
        return this.responseStatus;
    }

    public String getResponse() {
        return response;
    }


    @Override
    public String toString() {
        return "Response{" +
                "responseStatus=" + responseStatus +
                ", response=" + response +
                '}';
    }
}
