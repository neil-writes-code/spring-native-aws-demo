package ca.neilwhite.cloudfunctiondynamodblambda;

import ca.neilwhite.cloudfunctiondynamodblambda.models.Request;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Response;
import ca.neilwhite.cloudfunctiondynamodblambda.models.Session;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.nativex.hint.SerializationHint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SerializationHint(types = {Request.class, Response.class, Session.class})
@SpringBootConfiguration
public class CloudFunctionDynamodbLambdaApplication implements ApplicationContextInitializer<GenericApplicationContext> {

	public static void main(String[] args) {
		FunctionalSpringApplication.run(CloudFunctionDynamodbLambdaApplication.class, args);
	}

	@Override
	public void initialize(GenericApplicationContext applicationContext) {
		DynamoDbClient dynamoDbClient = DynamoDbClient.builder().region(Region.CA_CENTRAL_1).build();

		applicationContext.registerBean("getSessions", FunctionRegistration.class,
				() -> new FunctionRegistration<>(new GetSessions("sessions", dynamoDbClient)).type(FunctionType.from(Request.class).to(Response.class)));
	}
}
