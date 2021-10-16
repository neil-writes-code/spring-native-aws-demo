package ca.neilwhite.cloudfunctionlambda.models;

import java.io.Serializable;

public class Request implements Serializable {
    private String message;

    public Request(){
    }

    public Request(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
