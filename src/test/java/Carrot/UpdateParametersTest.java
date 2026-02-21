package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UpdateParametersTest {

    @Test
    void constructor_withAllFields_setsValues() {
        UpdateParameters params = new UpdateParameters("desc", "2026-02-21", "2026-02-22", "2026-02-23");

        assertEquals("desc", params.getDescription());
        assertEquals("2026-02-21", params.getStartDate());
        assertEquals("2026-02-22", params.getEndDate());
        assertEquals("2026-02-23", params.getDueDate());
    }

    @Test
    void constructor_withNullFields_preservesNulls() {
        UpdateParameters params = new UpdateParameters(null, null, null, null);

        assertNull(params.getDescription());
        assertNull(params.getStartDate());
        assertNull(params.getEndDate());
        assertNull(params.getDueDate());
    }
}
