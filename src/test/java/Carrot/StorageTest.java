package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

public class StorageTest {

    private static final String TEST_PATH = "data/test_Carrot.txt";
    private Storage storage;
    private File testFile;

    @BeforeEach
    void setUp() {
        storage = new Storage(TEST_PATH);
        testFile = new File(TEST_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            boolean deleted = testFile.delete();
            if (!deleted) {
                System.err.println("Warning: Could not delete test file " + TEST_PATH);
            }
        }
    }

    @Test
    void saveAndLoad_validTasks_success() throws CarrotException {
        ArrayList<Task> tasksToSave = new ArrayList<>();

        tasksToSave.add(new Todo("Borrow book"));
        tasksToSave.add(new Deadline("Return book", "2027-03-12 18:00"));
        tasksToSave.add(new Event("Project meeting", "2026-02-02 14:00", "2026-02-02 16:00"));

        tasksToSave.get(0).markCompleted();
        storage.save(tasksToSave);

        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(3, loadedTasks.size(), "Should load exactly 3 tasks");
        assertInstanceOf(Todo.class, loadedTasks.get(0));

        assertEquals("Borrow book", loadedTasks.get(0).toString().contains("Borrow book") ? "Borrow book" : "");
        assertTrue(loadedTasks.get(0).toString().contains("[X]"), "First task should be marked complete");

        assertInstanceOf(Deadline.class, loadedTasks.get(1));
        assertEquals("Return book", loadedTasks.get(1).toString().contains("Return book") ? "Return book" : "");
    }

    @Test
    void load_nonExistentFile_returnsEmptyList() throws CarrotException {
        if (testFile.exists()) {
            testFile.delete();
        }
        ArrayList<Task> tasks = storage.load();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Loading a non-existent file should return an empty list, not crash");
    }

    @Test
    void saveAndLoad_emptyList_success() throws CarrotException {
        ArrayList<Task> emptyList = new ArrayList<>();

        storage.save(emptyList);
        ArrayList<Task> loaded = storage.load();

        assertTrue(loaded.isEmpty(), "Loading empty task list should return empty list");
    }

    @Test
    void saveAndLoad_todoOnly_success() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Todo("Complete assignment"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertInstanceOf(Todo.class, loaded.get(0));
        assertEquals("Complete assignment", loaded.get(0).getTaskName());
    }

    @Test
    void saveAndLoad_deadlineOnly_success() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Deadline("Submit project", "2026-02-21 23:59"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertInstanceOf(Deadline.class, loaded.get(0));
        assertEquals("Submit project", loaded.get(0).getTaskName());
    }

    @Test
    void saveAndLoad_eventOnly_success() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Event("Team meeting", "2026-02-21 14:00", "2026-02-21 15:00"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertInstanceOf(Event.class, loaded.get(0));
        assertEquals("Team meeting", loaded.get(0).getTaskName());
    }

    @Test
    void saveAndLoad_preservesCompletionStatus() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        Todo todo = new Todo("Task 1");
        todo.markCompleted();
        tasks.add(todo);

        Deadline deadline = new Deadline("Task 2", "2026-02-21");
        deadline.markCompleted();
        tasks.add(deadline);

        Event event = new Event("Task 3", "2026-02-21 10:00", "2026-02-21 11:00");
        tasks.add(event);

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertTrue(loaded.get(0).toString().contains("[X]"), "Todo should be marked complete");
        assertTrue(loaded.get(1).toString().contains("[X]"), "Deadline should be marked complete");
        assertTrue(loaded.get(2).toString().contains("[ ]"), "Event should be incomplete");
    }

    @Test
    void saveAndLoad_multipleTasks_maintainOrder() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Todo("First"));
        tasks.add(new Deadline("Second", "2026-02-21"));
        tasks.add(new Event("Third", "2026-02-22 10:00", "2026-02-22 11:00"));
        tasks.add(new Todo("Fourth"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();

        assertEquals(4, loaded.size());
        assertEquals("First", loaded.get(0).getTaskName());
        assertEquals("Second", loaded.get(1).getTaskName());
        assertEquals("Third", loaded.get(2).getTaskName());
        assertEquals("Fourth", loaded.get(3).getTaskName());
    }

    @Test
    void saveAndLoad_eventWithTimePreserved() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Event("Meeting", "2026-02-21 14:30", "2026-02-21 15:45"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();
        Event event = (Event) loaded.get(0);

        assertEquals("2026-02-21T14:30", event.getStartDate().toString());
        assertEquals("2026-02-21T15:45", event.getEndDate().toString());
    }

    @Test
    void saveAndLoad_deadlineWithTimePreserved() throws CarrotException {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(new Deadline("Due", "2026-02-21 23:59"));

        storage.save(tasks);
        ArrayList<Task> loaded = storage.load();
        Deadline deadline = (Deadline) loaded.get(0);

        assertEquals("2026-02-21T23:59", deadline.getDueDate().toString());
    }
}
