package Carrot;

import Carrot.Task.Task;
import Carrot.Task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskListTest {

    private Storage storage;
    private TaskList taskList;

    @TempDir
    Path tempDir;

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
    void deleteTask_invalidIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(99);
        });
    }
}
