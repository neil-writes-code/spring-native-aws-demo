package ca.neilwhite.cloudfunctionlambda;

import ca.neilwhite.cloudfunctionlambda.models.Request;
import ca.neilwhite.cloudfunctionlambda.models.Response;

import java.util.function.Function;

public class Uppercase implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        return new Response(request.getMessage().toUpperCase(), request.getMessage().length());
    }
}
