package ca.neilwhite.cloudfunctiondynamodblambda.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Session implements Serializable {
    private String sessionId;
    private Long timestamp;
    private List<String> participants;
    private Map<String, Integer> results;

    public Session() {
    }

    public Session(String sessionId, Long timestamp, List<String> participants, Map<String, Integer> results) {
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.participants = participants;
        this.results = results;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public void setResults(Map<String, Integer> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", timestamp=" + timestamp +
                ", participants=" + participants +
                ", results=" + results +
                '}';
    }
}
