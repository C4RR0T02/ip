package carrot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void getTaskName_validName_returnsCorrectName() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 14:00");

        assertEquals("Complete CS2103T Task", deadline.getTaskName());
    }

    @Test
    public void getAddPrint_validTaskName_returnsCorrectMessages() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 14:00");

        assertEquals("New Deadline Alert: Complete CS2103T Task by 15-02-2025 14:00", deadline.getAddPrint());
    }

    @Test
    public void saveToString_completedTask_returnsCorrectMessages() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 14:00");
        deadline.markCompleted();

        assertEquals("D|1|Complete CS2103T Task|2025-02-15 14:00", deadline.saveToString());
    }

    @Test
    public void saveToString_incompleteTask_returnsCorrectMessages() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 14:00");

        assertEquals("D|0|Complete CS2103T Task|2025-02-15 14:00", deadline.saveToString());
    }

    @Test
    public void toString_validTaskNameNoTime_returnsCorrectString() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 14:00");

        assertEquals("[D] [ ] Complete CS2103T Task (by: 15-02-2025 14:00)", deadline.toString());
    }

    @Test
    public void toString_validTaskNameValidDate_returnsCorrectString() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 13:00");

        assertEquals("[D] [ ] Complete CS2103T Task (by: 15-02-2025 13:00)", deadline.toString());
    }

    @Test
    public void getDueDate_validDeadline_returnsCorrectDueDate() {
        Deadline deadline = new Deadline("Submit Assignment", "2026-02-21 23:59");

        assertEquals("2026-02-21T23:59", deadline.getDueDate().toString());
    }

    @Test
    public void getDueDateFormatted_validDeadline_returnsCorrectFormat() {
        Deadline deadline = new Deadline("Submit Assignment", "2026-02-21 23:59");

        assertEquals("2026-02-21 23:59", deadline.getDueDateFormatted());
    }

    @Test
    public void markCompleted_deadlineTask_marksAsComplete() {
        Deadline deadline = new Deadline("Task", "2026-02-21 14:00");
        deadline.markCompleted();

        assertEquals("[D] [X] Task (by: 21-02-2026 14:00)", deadline.toString());
    }

    @Test
    public void markIncomplete_completedDeadline_marksAsIncomplete() {
        Deadline deadline = new Deadline("Task", "2026-02-21 14:00");

        deadline.markCompleted();
        deadline.markIncomplete();

        assertEquals("[D] [ ] Task (by: 21-02-2026 14:00)", deadline.toString());
    }

    @Test
    public void getTaskType_deadline_returnsDeadlineType() {
        Deadline deadline = new Deadline("Task", "2026-02-21 14:00");

        assertEquals(Task.TaskType.DEADLINE, deadline.getTaskType());
    }

    @Test
    public void createUpdatedTask_updateDescription_returnsNewDeadline() {
        Deadline deadline = new Deadline("Old Task", "2026-02-21 14:00");
        Deadline updated = (Deadline) deadline.createUpdatedTask("New Task", null, null, null);

        assertEquals("New Task", updated.getTaskName());
        assertEquals("2026-02-21 14:00", updated.getDueDateFormatted());
    }

    @Test
    public void createUpdatedTask_updateDueDate_returnsNewDeadline() {
        Deadline deadline = new Deadline("Task", "2026-02-21 14:00");
        Deadline updated = (Deadline) deadline.createUpdatedTask(null, null, null, "2026-02-22 15:00");

        assertEquals("Task", updated.getTaskName());
        assertEquals("2026-02-22 15:00", updated.getDueDateFormatted());
    }

    @Test
    public void createUpdatedTask_updateBoth_returnsNewDeadline() {
        Deadline deadline = new Deadline("Old", "2026-02-21 14:00");
        Deadline updated = (Deadline) deadline.createUpdatedTask("New", null, null, "2026-02-22 15:00");

        assertEquals("New", updated.getTaskName());
        assertEquals("2026-02-22 15:00", updated.getDueDateFormatted());
    }

    @Test
    public void createUpdatedTask_nullValues_keepsOriginal() {
        Deadline deadline = new Deadline("Task", "2026-02-21 14:00");
        Deadline updated = (Deadline) deadline.createUpdatedTask(null, null, null, null);

        assertEquals("Task", updated.getTaskName());
        assertEquals("2026-02-21 14:00", updated.getDueDateFormatted());
    }
}
