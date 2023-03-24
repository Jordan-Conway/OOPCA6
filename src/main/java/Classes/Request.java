package Classes;

import Enums.RequestType;

public class Request {
    private RequestType requestType;
    private String parameter;

    public Request(RequestType requestType) {
        this.requestType = requestType;
        this.parameter = null;
    }

    public Request(RequestType requestType, String parameter) {
        this.requestType = requestType;
        this.parameter = parameter;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
