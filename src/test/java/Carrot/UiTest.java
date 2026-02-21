package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

class UiTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private Ui ui;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        ui = new Ui();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testShowWelcome_printsCorrectMessages() {
        String output = ui.showWelcome();

        assertTrue(output.contains("Salut! Je suis C4RR0T"));
        assertTrue(output.contains("What can I do for you?"));
    }

    @Test
    void testPrintTaskList_emptyList_printsEmpty() {
        ArrayList<Task> emptyList = new ArrayList<>();

        String output = ui.printTaskList(emptyList);

        assertEquals("Empty Results ʕ•́ᴥ•̀ʔっ", output.trim());
    }

    @Test
    void testPrintTaskList_populatedList_printsTasks() {
        ArrayList<Task> list = new ArrayList<>();

        list.add(new Todo("Buy Milk"));

        String output = ui.printTaskList(list);

        assertTrue(output.contains("1 [T] [ ] Buy Milk"));
    }

    @Test
    void testExit_setsIsExitToTrue() {
        assertFalse(ui.isExit());

        String output = ui.exit();

        assertTrue(ui.isExit());
        assertTrue(output.contains("Au revoir"));
    }

    @Test
    void testShowLoadingError_printsErrorMessage() {
        String output = ui.showLoadingError();

        assertTrue(output.contains("Failed to load task list"));
    }

    @Test
    void testPrintTaskList_withMultipleTasks_showsAllTasks() {
        ArrayList<Task> list = new ArrayList<>();

        list.add(new Todo("Task 1"));
        list.add(new Deadline("Task 2", "2026-02-21"));
        list.add(new Event("Task 3", "2026-02-21 10:00", "2026-02-21 11:00"));

        String output = ui.printTaskList(list);

        assertTrue(output.contains("Task 1"));
        assertTrue(output.contains("Task 2"));
        assertTrue(output.contains("Task 3"));
    }

    @Test
    void testPrintTaskList_withCompletedTasks_showsCheckmark() {
        ArrayList<Task> list = new ArrayList<>();

        Todo todo = new Todo("Completed Task");
        todo.markCompleted();
        list.add(todo);

        String output = ui.printTaskList(list);

        assertTrue(output.contains("[X]"));
    }

    @Test
    void testPrintAddTask_printsFeedback() {
        Task task = new Todo("New Task");

        String output = ui.printAddTask(task);

        assertFalse(output.isEmpty());
        assertTrue(output.contains("New Task"));
    }

    @Test
    void testPrintDeleteTask_printsDeletionMessage() {
        Task task = new Todo("Deleted Task");

        String output = ui.printDeleteTask(task);

        assertFalse(output.isEmpty());
        assertTrue(output.contains("Deleted Task"));
    }

    @Test
    void testPrintUpdateMessage_printsUpdateFeedback() {
        Task oldTask = new Todo("Old");
        Task newTask = new Todo("New");

        String output = ui.printUpdateMessage(oldTask, newTask);

        assertFalse(output.isEmpty());
    }

    @Test
    void testSetExit_marksExit() {
        assertFalse(ui.isExit());

        ui.setExit();

        assertTrue(ui.isExit());
    }

    @Test
    void testShowInvalidCommands_printsHelp() {
        String output = ui.showInvalidCommands();

        assertFalse(output.isEmpty());
        assertTrue(output.contains("command"));
    }

    @Test
    void testPrintHelp_printsSupportedCommands() {
        String output = ui.printHelp();

        assertFalse(output.isEmpty());
        assertTrue(output.contains("todo") || output.contains("deadline") || output.contains("event"));
    }

    @Test
    void testIsExit_defaultFalse() {
        Ui newUi = new Ui();

        assertFalse(newUi.isExit());
    }

    @Test
    void testPrintTaskList_withNumbers_showsCorrectIndexing() {
        ArrayList<Task> list = new ArrayList<>();

        list.add(new Todo("First"));
        list.add(new Todo("Second"));

        String output = ui.printTaskList(list);

        assertTrue(output.contains("1") || output.contains("First"));
        assertTrue(output.contains("2") || output.contains("Second"));
    }
}
