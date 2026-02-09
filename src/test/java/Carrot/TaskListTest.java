package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import carrot.task.Task;
import carrot.task.Todo;

class TaskListTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        String tempPath = tempDir.resolve("tasks.txt").toString();
        storage = new Storage(tempPath);
        taskList = new TaskList(storage);
    }

    @Test
    void addTask_addsToListAndSaves() throws CarrotException {
        Task todo = new Todo("Test Task");
        taskList.addTask(todo);
        assertEquals(1, taskList.getTasks().size());
        assertEquals(todo, taskList.getTasks().get(0));
        ArrayList<Task> savedTasks = storage.load();
        assertEquals(1, savedTasks.size());
        assertTrue(savedTasks.get(0).toString().contains("Test Task"));
    }

    @Test
    void deleteTask_removesTaskAndReturnsIt() {
        Task todo = new Todo("Delete Me");
        taskList.addTask(todo);
        Task deleted = taskList.deleteTask(0);
        assertEquals(todo, deleted, "deleteTask should return the task that was removed");
        assertEquals(0, taskList.getTasks().size(), "List should be empty after deletion");
    }

    @Test
    void loadTaskList_updatesMemoryFromStorage() throws CarrotException {
        ArrayList<Task> initialData = new ArrayList<>();
        initialData.add(new Todo("Initial Task"));
        storage.save(initialData);
        taskList.loadTaskList();
        assertEquals(1, taskList.getTasks().size());
        assertTrue(taskList.getTasks().get(0).toString().contains("Initial Task"));
    }

    @Test
    void findTasks_keywordExists_addsToArrayList() {
        taskList.addTask(new Todo("Buy fresh carrots"));
        taskList.addTask(new Todo("Eat healthy cake"));
        taskList.addTask(new Todo("Cook carrot soup"));
        ArrayList<Task> results = new ArrayList<>();
        taskList.findTasks("carrot", results);
        assertEquals(2, results.size(), "Should find exactly 2 tasks containing 'carrot'");
        assertTrue(results.get(0).toString().contains("fresh carrots"));
        assertTrue(results.get(1).toString().contains("carrot soup"));
    }

    @Test
    void findTasks_partialKeyword_findsMatches() {
        taskList.addTask(new Todo("Internationalization"));
        ArrayList<Task> results = new ArrayList<>();
        taskList.findTasks("nation", results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Internationalization"));
    }

    @Test
    void findTasks_keywordDoesNotExist_arrayListRemainsEmpty() {
        taskList.addTask(new Todo("Buy milk"));
        ArrayList<Task> results = new ArrayList<>();
        taskList.findTasks("carrot", results);
        assertTrue(results.isEmpty(), "Results list should be empty when no matches are found");
    }

    @Test
    void deleteTask_invalidIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.deleteTask(99));
    }
}
