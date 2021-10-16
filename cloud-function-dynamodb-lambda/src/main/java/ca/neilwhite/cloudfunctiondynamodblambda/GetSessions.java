package ca.neilwhite.cloudfunctiondynamodblambda;

import ca.neilwhite.cloudfunctiondynamodblambda.models.Request;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Response;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Session;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetSessions implements Function<Request, Response> {
    private final String tableName;
    private final DynamoDbClient dynamoDbClient;

    public GetSessions(String tableName, DynamoDbClient dynamoDbClient) {
        this.tableName = tableName;
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public Response apply(Request request) {
        return new Response(findSessions(request.getUserId()));
    }

    /**
     * Performs a query for all sessions by the provided userId
     * @param userId
     * @return List
     */
    private List<Session> findSessions(String userId) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":userId", AttributeValue.builder().s(userId.toLowerCase()).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("userId = :userId")
                .expressionAttributeValues(expressionValues).build();

        List<Map<String, AttributeValue>> queryResponse = dynamoDbClient.query(queryRequest).items();

        return queryResponse.isEmpty() ? List.of()
                : queryResponse.stream()
                .map(this::transformSession)
                .collect(Collectors.toList());
    }

    /**
     * Transforms all the string values back to their original types
     * @param values
     * @return  {@link Session}
     */
    private Session transformSession(Map<String, AttributeValue> values) {
        Session session = new Session();

        session.setSessionId(values.get("sessionId").s());
        session.setTimestamp(Long.parseLong(values.get("timestamp").n()));
        session.setParticipants(values.get("participants").ss());

        Map<String, AttributeValue> resultValues = values.get("results").m();
        Map<String, Integer> results = new HashMap<>();

        resultValues.forEach((k, v) -> results.put(k, Integer.parseInt(v.n())));
        session.setResults(results);

        return session;
    }
}