package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

class TaskListTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        String tempPath = tempDir.resolve("test_Carrot.txt").toString();
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
        assertThrows(AssertionError.class, () -> taskList.deleteTask(99));
    }

    @Test
    void updateTask_replacesTaskAtIndex() {
        Task original = new Todo("Original Task");
        Task updated = new Todo("Updated Task");

        taskList.addTask(original);
        taskList.updateTask(0, updated);

        assertEquals(1, taskList.getTasks().size());
        assertEquals("Updated Task", taskList.getTasks().get(0).getTaskName());
    }

    @Test
    void getTasks_returnsAllTasks() {
        taskList.addTask(new Todo("Task 1"));
        taskList.addTask(new Todo("Task 2"));
        taskList.addTask(new Deadline("Task 3", "2026-02-21"));

        ArrayList<Task> tasks = taskList.getTasks();

        assertEquals(3, tasks.size());
    }

    @Test
    void getTasks_emptyList_returnsEmptyArrayList() {
        ArrayList<Task> tasks = taskList.getTasks();

        assertTrue(tasks.isEmpty(), "New task list should be empty");
    }

    @Test
    void addTask_multipleTasks_maintainsOrder() {
        Task task1 = new Todo("First");
        Task task2 = new Todo("Second");
        Task task3 = new Todo("Third");

        taskList.addTask(task1);
        taskList.addTask(task2);
        taskList.addTask(task3);

        assertEquals("First", taskList.getTasks().get(0).getTaskName());
        assertEquals("Second", taskList.getTasks().get(1).getTaskName());
        assertEquals("Third", taskList.getTasks().get(2).getTaskName());
    }

    @Test
    void deleteTask_removesCorrectTaskWhenMultipleTasks() {
        taskList.addTask(new Todo("Keep 1"));
        taskList.addTask(new Todo("Delete"));
        taskList.addTask(new Todo("Keep 2"));

        taskList.deleteTask(1);

        assertEquals(2, taskList.getTasks().size());
        assertEquals("Keep 1", taskList.getTasks().get(0).getTaskName());
        assertEquals("Keep 2", taskList.getTasks().get(1).getTaskName());
    }

    @Test
    void findTasks_multipleTasks_findsAllMatches() {
        taskList.addTask(new Todo("BUY carrots"));
        taskList.addTask(new Todo("buy milk"));

        ArrayList<Task> results = new ArrayList<>();
        taskList.findTasks("buy", results);

        assertEquals(2, results.size(), "Search should be case-insensitive");
    }

    @Test
    void updateTask_updateEvent_replacesCompleteEvent() {
        Task original = new Event("Old Event", "2026-02-21 10:00", "2026-02-21 11:00");
        Task updated = new Event("New Event", "2026-02-22 14:00", "2026-02-22 15:00");

        taskList.addTask(original);
        taskList.updateTask(0, updated);

        Event event = (Event) taskList.getTasks().get(0);

        assertEquals("New Event", event.getTaskName());
        assertEquals("2026-02-22T14:00", event.getStartDate().toString());
    }

    @Test
    void updateTask_updateDeadline_replacesCompleteDeadline() {
        Task original = new Deadline("Old Deadline", "2026-02-21 14:00");
        Task updated = new Deadline("New Deadline", "2026-02-22 15:00");

        taskList.addTask(original);
        taskList.updateTask(0, updated);

        Deadline deadline = (Deadline) taskList.getTasks().get(0);

        assertEquals("New Deadline", deadline.getTaskName());
        assertEquals("2026-02-22T15:00", deadline.getDueDate().toString());
    }
}
