package ca.neilwhite.cloudfunctionlambda.models;

import java.io.Serializable;

public class Response implements Serializable {
    private String uppercaseMessage;
    private Integer characterCount;

    public Response() {
    }

    public Response(String uppercaseMessage, Integer characterCount) {
        this.uppercaseMessage = uppercaseMessage;
        this.characterCount = characterCount;
    }

    public String getUppercaseMessage() {
        return uppercaseMessage;
    }

    public void setUppercaseMessage(String uppercaseMessage) {
        this.uppercaseMessage = uppercaseMessage;
    }

    public Integer getCharacterCount() {
        return characterCount;
    }

    public void setCharacterCount(Integer characterCount) {
        this.characterCount = characterCount;
    }
}
