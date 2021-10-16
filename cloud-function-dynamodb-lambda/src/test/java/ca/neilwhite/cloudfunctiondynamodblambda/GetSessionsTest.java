package ca.neilwhite.cloudfunctiondynamodblambda;

import ca.neilwhite.cloudfunctiondynamodblambda.models.Request;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Response;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class GetSessionsTest {

    @Container
    private static final GenericContainer dynamoDBContainer = new GenericContainer("amazon/dynamodb-local:latest")
            .withCommand("-jar DynamoDBLocal.jar -inMemory -sharedDb")
            .withExposedPorts(8000);

    private static GetSessions getSessions;
    private static DynamoDbClient dynamoDbClient;

    @BeforeAll
    public static void setup(){
        String tableName = "sessions";

        URI endpoint = URI.create("http://localhost:" + dynamoDBContainer.getFirstMappedPort());

        dynamoDbClient = DynamoDbClient.builder().endpointOverride(endpoint).build();

        // Create a partition key(userId) and sort key(sessionId) for the table
        List<KeySchemaElement> keys = new ArrayList<>();
        keys.add(KeySchemaElement.builder().keyType(KeyType.HASH).attributeName("userId").build());
        keys.add(KeySchemaElement.builder().keyType(KeyType.RANGE).attributeName("sessionId").build());

        // Assign types to the keys (strings)
        List<AttributeDefinition> attributes = new ArrayList<>();
        attributes.add(AttributeDefinition.builder().attributeType(ScalarAttributeType.S).attributeName("userId").build());
        attributes.add(AttributeDefinition.builder().attributeType(ScalarAttributeType.S).attributeName("sessionId").build());

        // Create the table
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .keySchema(keys)
                .attributeDefinitions(attributes).build();

        dynamoDbClient.createTable(createTableRequest);

        // Create some dummy data
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("test-def456", Instant.now().getEpochSecond(),List.of("player1", "player2", "player3"), Map.of("player1", 6, "player2", 3,"player3", 7)));
        sessions.add(new Session("test-abc123", Instant.now().getEpochSecond(),List.of("player1", "player3", "player4"), Map.of("player1", 2, "player3", 7,"player4", 9)));
        sessions.add(new Session("test-ghi789", Instant.now().getEpochSecond(),List.of("player3", "player5", "player12"), Map.of("player3", 3, "player5", 4,"player12", 10)));

        // For each session, create a table row for each userId. This will create duplicate data but allow us to
        // quickly query all sessions for a given player
        sessions.forEach(session -> {
            session.getParticipants().forEach(participant ->{
                Map<String, AttributeValue> values = new HashMap<>();
                // s = String
                values.put("userId", AttributeValue.builder().s(participant).build());
                values.put("sessionId", AttributeValue.builder().s(session.getSessionId()).build());
                // n = Number
                values.put("timestamp", AttributeValue.builder().n(String.valueOf(session.getTimestamp())).build());
                // ss = String Set/List
                values.put("participants", AttributeValue.builder().ss(session.getParticipants()).build());

                Map<String, AttributeValue> restaurants = new HashMap<>();
                session.getResults().forEach((key,value) -> restaurants.put(key, AttributeValue.builder().n(value.toString()).build()));
                // m = Map
                values.put("results", AttributeValue.builder().m(restaurants).build());

                PutItemRequest putItemRequest = PutItemRequest.builder().tableName(tableName).item(values).build();
                dynamoDbClient.putItem(putItemRequest);
            });
        });

        getSessions = new GetSessions(tableName, dynamoDbClient);
    }

    @ParameterizedTest
    @ValueSource(strings = { "player1", "player3"})
    void shouldReturnSessions(String userId) {
        Request request = new Request(userId);

        Response response = getSessions.apply(request);

        assertFalse(response.getSessions().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "player19", "player42"})
    void shouldNotReturnSessions(String userId) {

        Request request = new Request(userId);

        Response response = getSessions.apply(request);

        assertTrue(response.getSessions().isEmpty());
    }
}