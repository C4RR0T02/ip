package Carrot.Task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    private static class TaskStub extends Task {
        public TaskStub(String name) {
            super(name);
        }

        @Override
        public String getAddPrint() {
            return "stub";
        }

        @Override
        public String saveToString() {
            return "stub_save";
        }
    }

    @Test
    void testConstructor_initializesCorrectly() {
        Task task = new TaskStub("Buy Milk");
        assertEquals("Buy Milk", task.taskName, "Task name should be set correctly");
        assertFalse(task.isComplete, "New task should be incomplete by default");
    }

    @Test
    void testMarkCompleted_setsStatusTrue() {
        Task task = new TaskStub("Eat Lunch");
        task.markCompleted();
        assertTrue(task.isComplete, "Task should be marked as complete");
        assertEquals("[X] Eat Lunch", task.toString());
    }

    @Test
    void testMarkIncomplete_setsStatusFalse() {
        Task task = new TaskStub("Wash Car");
        task.markCompleted();
        task.markIncomplete();
        assertFalse(task.isComplete, "Task should be marked as incomplete");
        assertEquals("[ ] Wash Car", task.toString());
    }
}
