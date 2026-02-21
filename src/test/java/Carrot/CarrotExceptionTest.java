package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CarrotExceptionTest {

    @Test
    void constructor_setsMessage() {
        CarrotException exception = new CarrotException("error message");
        assertEquals("error message", exception.getMessage());
    }
}
