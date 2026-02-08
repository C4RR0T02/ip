package Carrot;

import Carrot.Task.Deadline;
import Carrot.Task.Event;
import Carrot.Task.Task;
import Carrot.Task.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorageTest {

    private final String TEST_PATH = "data/test_storage.txt";
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
}
