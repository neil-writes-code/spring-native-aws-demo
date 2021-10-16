package ca.neilwhite.cloudfunctiondynamodblambda.models;

import java.io.Serializable;

public class Request implements Serializable {
    private String userId;

    public Request() {
    }

    public Request(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
