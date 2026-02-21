package carrot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void getTaskName_validName_returnsCorrectName() {
        Event event = new Event("CCA Training", "2025-02-15 10:00", "2025-02-15 12:00");

        assertEquals("CCA Training", event.getTaskName());
    }

    @Test
    public void getAddPrint_validTaskName_returnsCorrectMessages() {
        Event event = new Event("CCA Training", "2025-02-15 07:30", "2025-02-15 12:00");
        String expected = "New Event Alert: CCA Training occurring from 15-02-2025 07:30 to 15-02-2025 12:00";

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
        Event event = new Event("CCA Training", "2025-02-15 10:00", "2025-02-15 12:00");

        assertEquals("[E] [ ] CCA Training (from: 15-02-2025 10:00 to: 15-02-2025 12:00)", event.toString());
    }

    @Test
    public void toString_validTaskNameValidDateTime_returnsCorrectString() {
        Event event = new Event("CCA Training", "2025-02-15 08:00", "2025-02-15 13:00");

        assertEquals("[E] [ ] CCA Training (from: 15-02-2025 08:00 to: 15-02-2025 13:00)", event.toString());
    }

    @Test
    public void getStartDate_validEvent_returnsCorrectStartDate() {
        Event event = new Event("Meeting", "2026-02-21 14:30", "2026-02-21 15:30");

        assertEquals("2026-02-21T14:30", event.getStartDate().toString());
    }

    @Test
    public void getEndDate_validEvent_returnsCorrectEndDate() {
        Event event = new Event("Meeting", "2026-02-21 14:30", "2026-02-21 15:30");

        assertEquals("2026-02-21T15:30", event.getEndDate().toString());
    }

    @Test
    public void createUpdatedTask_updateAllFields_returnsNewEvent() {
        Event event = new Event("Old Meeting", "2026-02-21 14:00", "2026-02-21 15:00");
        Event updated = (Event) event.createUpdatedTask("New Meeting", "2026-02-22 10:00", "2026-02-22 11:00", null);

        assertEquals("New Meeting", updated.getTaskName());
        assertEquals("2026-02-22T10:00", updated.getStartDate().toString());
        assertEquals("2026-02-22T11:00", updated.getEndDate().toString());
    }

    @Test
    public void createUpdatedTask_partialUpdate_keepsOriginalValues() {
        Event event = new Event("Meeting", "2026-02-21 14:00", "2026-02-21 15:00");
        Event updated = (Event) event.createUpdatedTask("Updated Name", null, null, null);

        assertEquals("Updated Name", updated.getTaskName());
        assertEquals("2026-02-21T14:00", updated.getStartDate().toString());
        assertEquals("2026-02-21T15:00", updated.getEndDate().toString());
    }

    @Test
    public void getStartDateFormatted_validEvent_returnsCorrectFormat() {
        Event event = new Event("Meeting", "2026-02-21 14:30", "2026-02-21 15:30");

        assertEquals("2026-02-21 14:30", event.getStartDateFormatted());
    }

    @Test
    public void getEndDateFormatted_validEvent_returnsCorrectFormat() {
        Event event = new Event("Meeting", "2026-02-21 14:30", "2026-02-21 15:30");

        assertEquals("2026-02-21 15:30", event.getEndDateFormatted());
    }

    @Test
    public void markCompleted_eventTask_marksAsComplete() {
        Event event = new Event("Meeting", "2026-02-21 14:00", "2026-02-21 15:00");
        event.markCompleted();

        assertEquals("[E] [X] Meeting (from: 21-02-2026 14:00 to: 21-02-2026 15:00)", event.toString());
    }

    @Test
    public void markIncomplete_completedEvent_marksAsIncomplete() {
        Event event = new Event("Meeting", "2026-02-21 14:00", "2026-02-21 15:00");
        event.markCompleted();
        event.markIncomplete();

        assertEquals("[E] [ ] Meeting (from: 21-02-2026 14:00 to: 21-02-2026 15:00)", event.toString());
    }

    @Test
    public void getTaskType_event_returnsEventType() {
        Event event = new Event("Meeting", "2026-02-21 14:00", "2026-02-21 15:00");

        assertEquals(Task.TaskType.EVENT, event.getTaskType());
    }
}
