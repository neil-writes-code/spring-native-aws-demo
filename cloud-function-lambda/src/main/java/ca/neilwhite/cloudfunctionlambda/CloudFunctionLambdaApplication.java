package ca.neilwhite.cloudfunctionlambda;

import ca.neilwhite.cloudfunctionlambda.models.Request;
import ca.neilwhite.cloudfunctionlambda.models.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.SerializationHint;

@NativeHint
@SerializationHint(types = {Request.class, Response.class})
@SpringBootApplication
public class CloudFunctionLambdaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudFunctionLambdaApplication.class, args);
    }
}
