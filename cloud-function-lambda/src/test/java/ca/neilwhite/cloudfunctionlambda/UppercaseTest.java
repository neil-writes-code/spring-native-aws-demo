package ca.neilwhite.cloudfunctionlambda;

import ca.neilwhite.cloudfunctionlambda.models.Request;
import ca.neilwhite.cloudfunctionlambda.models.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UppercaseTest {

    @Test
    void shouldReturnResponse(){
        Uppercase uppercaseFunction = new Uppercase();
        Request request = new Request("Hello World!");

        Response response = uppercaseFunction.apply(request);

        assertEquals("HELLO WORLD!", response.getUppercaseMessage());
        assertEquals(12, response.getCharacterCount());
    }
}