package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import carrot.task.Task;

/**
 * Tests for the Carrot application
 */
class CarrotTest {

    @TempDir
    Path tempDir;

    /**
     * Test that Carrot creates tasks in the correct file path
     */
    @Test
    void testCarrot_withCustomPath_createsFileCorrectly() {
        Path testFilePath = tempDir.resolve("test_tasks.txt");
        String testPathString = testFilePath.toString();

        try {
            Carrot carrot = new Carrot(testPathString);
            carrot.getTaskList().addTask(new carrot.task.Todo("Save the world"));
            carrot.getStorage().save(carrot.getTaskList().getTasks());

            File testFile = testFilePath.toFile();
            assertTrue(testFile.exists(), "Should have created the file in the temp directory");

            Storage testStorage = new Storage(testPathString);
            ArrayList<Task> loadedTasks = testStorage.load();
            assertEquals(1, loadedTasks.size(), "Should have saved 1 task to the temp file");
        } catch (Exception e) {
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        }
    }
}
