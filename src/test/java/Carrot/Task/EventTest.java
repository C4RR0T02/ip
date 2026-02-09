package carrot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void getTaskName_validName_returnsCorrectName() {
        Event event = new Event("CCA Training", "2025-02-15", "2025-02-15");
        assertEquals("CCA Training", event.getTaskName());
    }

    @Test
    public void getAddPrint_validTaskName_returnsCorrectMessages() {
        Event event = new Event("CCA Training", "2025-02-15", "2025-02-15");
        String expected = "New Event Alert: CCA Training occurring from 15-02-2025 00:00 to 15-02-2025 00:00";
        assertEquals(expected, event.getAddPrint());
    }

    @Test
    public void saveToString_completedTask_returnsCorrectMessages() {
        Event event = new Event("CCA Training", "2025-02-15 07:30", "2025-02-15 12:00");
        event.markCompleted();
        assertEquals("E|1|CCA Training|2025-02-15 07:30|2025-02-15 12:00", event.saveToString());
    }

    @Test
    public void saveToString_incompleteTask_returnsCorrectMessages() {
        Event event = new Event("CCA Training", "2025-02-15 07:30", "2025-02-15 12:00");
        assertEquals("E|0|CCA Training|2025-02-15 07:30|2025-02-15 12:00", event.saveToString());
    }

    @Test
    public void toString_validTaskNameNoTime_returnsCorrectString() {
        Event event = new Event("CCA Training", "2025-02-15", "2025-02-15");
        assertEquals("[E] [ ] CCA Training (from: 15-02-2025 00:00 to: 15-02-2025 00:00)", event.toString());
    }

    @Test
    public void toString_validTaskNameValidDateTime_returnsCorrectString() {
        Event event = new Event("CCA Training", "2025-02-15 08:00", "2025-02-15 13:00");
        assertEquals("[E] [ ] CCA Training (from: 15-02-2025 08:00 to: 15-02-2025 13:00)", event.toString());
    }
}
