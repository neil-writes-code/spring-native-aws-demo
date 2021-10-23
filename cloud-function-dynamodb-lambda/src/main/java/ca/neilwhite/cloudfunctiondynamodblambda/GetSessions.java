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
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":userId", AttributeValue.builder().s(request.getUserId().toLowerCase()).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("userId = :userId")
                .expressionAttributeValues(expressionValues).build();

        List<Map<String, AttributeValue>> queryResponse = dynamoDbClient.query(queryRequest).items();

        List<Session> sessions = queryResponse.isEmpty() ? List.of()
                : queryResponse.stream()
                .map(Session::from)
                .collect(Collectors.toList());

        return new Response(sessions);
    }
}