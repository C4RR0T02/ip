package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ResponseTest {

    @Test
    void constructor_validCommandType_setsFields() {
        Response response = new Response("TODO", "message");

        assertEquals(Response.CommandType.TODO, response.getCommandType());
        assertEquals("message", response.getMessage());
    }

    @Test
    void constructor_invalidCommandType_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Response("NOT_A_COMMAND", "message"));
    }

    @Test
    void toString_returnsCommandTypeAndMessage() {
        Response response = new Response("HELP", "help message");

        assertEquals("HELPhelp message", response.toString());
    }
}
