package ca.neilwhite.cloudfunctionlambda;

import ca.neilwhite.cloudfunctionlambda.models.Request;
import ca.neilwhite.cloudfunctionlambda.models.Response;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.nativex.hint.SerializationHint;

@SerializationHint(types = {Request.class, Response.class})
@SpringBootConfiguration
public class CloudFunctionLambdaApplication implements ApplicationContextInitializer<GenericApplicationContext> {

	public static void main(String[] args) {
		FunctionalSpringApplication.run(CloudFunctionLambdaApplication.class, args);
	}

	@Override
	public void initialize(GenericApplicationContext applicationContext) {
		applicationContext.registerBean("uppercase", FunctionRegistration.class,
				() -> new FunctionRegistration<>(new Uppercase()).type(FunctionType.from(Request.class).to(Response.class)));
	}
}
