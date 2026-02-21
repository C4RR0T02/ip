package carrot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TaskTest {

    private static class TaskStub extends Task {
        public TaskStub(String name) {
            super(name);
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.TASK;
        }

        @Override
        public String getAddPrint() {
            return "stub";
        }

        @Override
        public String saveToString() {
            return "stub_save";
        }

        @Override
        public Task createUpdatedTask(String description, String startDate, String endDate,
                                     String dueDate) {
            String newDescription = (description != null) ? description : this.taskName;
            return new TaskStub(newDescription);
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

    @Test
    void testGetTaskName_returnsCorrectName() {
        Task task = new TaskStub("Important Task");

        assertEquals("Important Task", task.getTaskName());
    }

    @Test
    void testCreateUpdatedTask_updatesDescription() {
        Task task = new TaskStub("Original");
        Task updated = task.createUpdatedTask("Updated", null, null, null);

        assertEquals("Updated", updated.getTaskName());
    }

    @Test
    void testToString_incompleteTask_containsSpaceBox() {
        Task task = new TaskStub("Test Task");

        assertEquals("[ ] Test Task", task.toString());
    }

    @Test
    void testToString_completeTask_containsXBox() {
        Task task = new TaskStub("Test Task");
        task.markCompleted();

        assertEquals("[X] Test Task", task.toString());
    }

    @Test
    void testGetStartDateFormatted_nonEventTask_throwsException() {
        Task task = new TaskStub("Test");
        try {
            task.getStartDateFormatted();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Start date is not supported"));
        }
    }

    @Test
    void testGetEndDateFormatted_nonEventTask_throwsException() {
        Task task = new TaskStub("Test");
        try {
            task.getEndDateFormatted();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("End date is not supported"));
        }
    }

    @Test
    void testGetDueDateFormatted_nonDeadlineTask_throwsException() {
        Task task = new TaskStub("Test");
        try {
            task.getDueDateFormatted();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Due date is not supported"));
        }
    }
}
