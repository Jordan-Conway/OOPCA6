package Classes;

public class Request {
    private String requestType;
    private String parameter;

    public Request(String requestType) {
        this.requestType = requestType;
        this.parameter = null;
    }

    public Request(String requestType, String parameter) {
        this.requestType = requestType;
        this.parameter = parameter;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
