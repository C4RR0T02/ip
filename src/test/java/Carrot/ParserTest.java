package carrot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

class ParserTest {

    @TempDir
    Path tempDir;

    private Parser parser;
    private TaskList taskList;
    private Ui ui;
    private Storage storage;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        parser = new Parser();
        ui = new Ui();
        String tempPath = tempDir.resolve("parser_test.txt").toString();
        storage = new Storage(tempPath);
        taskList = new TaskList(storage);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void addTodo_validArgs_addsTask() throws CarrotException {
        Response response = parser.addTodo(ui, "Read Book", taskList, storage);

        assertEquals(1, taskList.getTasks().size());
        assertInstanceOf(Todo.class, taskList.getTasks().get(0));
        assertEquals("Read Book", taskList.getTasks().get(0).toString().contains("Read Book") ? "Read Book" : "");
    }

    @Test
    void addTodo_emptyArgs_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addTodo(ui, "", taskList, storage));
    }

    @Test
    void addDeadline_validArgs_parsesCorrectly() throws CarrotException {
        Response response = parser.addDeadline(ui, "Submit Report /by 2026-02-08 23:59", taskList, storage);

        Task task = taskList.getTasks().get(0);

        assertInstanceOf(Deadline.class, task);
        assertTrue(task.toString().contains("Submit Report"));
    }

    @Test
    void addDeadline_missingBy_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addDeadline(ui,
                "Submit Report 2026-02-08",
                taskList,
                storage));
    }

    @Test
    void mark_validIndex_updatesTask() throws CarrotException {
        taskList.addTask(new Todo("Initial Task"));

        Response response = parser.mark(ui, taskList.getTasks(), "1", storage);

        assertTrue(taskList.getTasks().get(0).toString().contains("[X]"));
    }

    @Test
    void getIndex_invalidNumber_throwsException() {
        assertThrows(CarrotException.class, () -> parser.deleteTask(ui, taskList, "100", storage));
    }

    @Test
    void findTask_validKeyword_printsMatchingTasks() throws CarrotException {
        taskList.addTask(new Todo("Buy fresh carrots"));
        taskList.addTask(new Todo("Eat healthy cake"));

        Response response = parser.findTask(ui, "carrot", taskList);
        String output = response.getMessage();

        assertTrue(output.contains("Buy fresh carrots"));
    }

    @Test
    void findTask_emptyArgs_throwsCarrotException() {
        assertThrows(CarrotException.class, () -> parser.findTask(ui, "", taskList));
    }

    @Test
    void findTask_noMatches_printsEmptyMessage() throws CarrotException {
        taskList.addTask(new Todo("Buy milk"));

        Response response = parser.findTask(ui, "carrot", taskList);
        String output = response.getMessage().trim();

        assert(output.contains("Empty Results ʕ•́ᴥ•̀ʔっ"));
    }

    @Test
    void addEvent_validArgs_addsTask() throws CarrotException {
        Response response = parser.addEvent(ui,
             "Team Meeting /from 2026-02-21 10:00 /to 2026-02-21 11:00",
             taskList, storage);

        assertEquals(1, taskList.getTasks().size());
        assertInstanceOf(Event.class, taskList.getTasks().get(0));
        assertTrue(taskList.getTasks().get(0).toString().contains("Team Meeting"));
    }

    @Test
    void addEvent_invalidDateOrder_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addEvent(ui,
             "Team Meeting /from 2026-02-21 11:00 /to 2026-02-21 10:00",
             taskList, storage));
    }

    @Test
    void addEvent_sameStartAndEnd_succeeds() {
        assertDoesNotThrow(() -> parser.addEvent(ui,
             "Team Meeting /from 2026-02-21 10:00 /to 2026-02-21 10:00",
             taskList, storage));
        assertEquals(1, taskList.getTasks().size());
    }

    @Test
    void addEvent_missingFrom_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addEvent(ui,
             "Team Meeting /to 2026-02-21 11:00",
             taskList, storage));
    }

    @Test
    void addEvent_missingTo_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addEvent(ui,
             "Team Meeting /from 2026-02-21 10:00",
             taskList, storage));
    }

    @Test
    void addEvent_emptyArgs_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addEvent(ui, "", taskList, storage));
    }

    @Test
    void updateEvent_validDates_succeeds() throws CarrotException {
        taskList.addTask(new Event("Old Meeting", "2026-02-21 10:00", "2026-02-21 11:00"));

        Response response = parser.updateTask(ui,
             "1 /from 2026-02-22 14:00 /to 2026-02-22 15:00",
             taskList, storage);
        Event updated = (Event) taskList.getTasks().get(0);

        assertTrue(updated.toString().contains("14:00"));
        assertTrue(updated.toString().contains("15:00"));
    }

    @Test
    void updateEvent_invalidDateOrder_throwsException() {
        taskList.addTask(new Event("Old Meeting", "2026-02-21 10:00", "2026-02-21 11:00"));

        assertThrows(CarrotException.class, () -> parser.updateTask(ui,
             "1 /from 2026-02-21 15:00 /to 2026-02-21 14:00",
             taskList, storage));
    }

    @Test
    void updateEvent_partialUpdate_validatesWithOriginal() throws CarrotException {
        taskList.addTask(new Event("Meeting", "2026-02-21 10:00", "2026-02-21 11:00"));

        Response response = parser.updateTask(ui,
             "1 /from 2026-02-21 10:30",
             taskList, storage);

        assertEquals(Response.CommandType.UPDATE, response.getCommandType());
    }

    @Test
    void updateEvent_partialUpdateInvalid_throwsException() {
        taskList.addTask(new Event("Meeting", "2026-02-21 10:00", "2026-02-21 11:00"));

        assertThrows(CarrotException.class, () -> parser.updateTask(ui,
             "1 /from 2026-02-21 12:00",
             taskList, storage));
    }

    @Test
    void addTodo_nullArgs_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addTodo(ui, null, taskList, storage));
    }

    @Test
    void addDeadline_emptyArgs_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addDeadline(ui, "", taskList, storage));
    }

    @Test
    void addDeadline_nullArgs_throwsException() {
        assertThrows(CarrotException.class, () -> parser.addDeadline(ui, null, taskList, storage));
    }

    @Test
    void unmark_validIndex_updatesTask() throws CarrotException {
        taskList.addTask(new Todo("Initial Task"));

        taskList.getTasks().get(0).markCompleted();
        Response response = parser.unmark(ui, taskList.getTasks(), "1", storage);

        assertTrue(taskList.getTasks().get(0).toString().contains("[ ]"));
    }

    @Test
    void deleteTask_validIndex_removesTask() throws CarrotException {
        taskList.addTask(new Todo("Task to delete"));

        assertEquals(1, taskList.getTasks().size());

        Response response = parser.deleteTask(ui, taskList, "1", storage);

        assertEquals(0, taskList.getTasks().size());
    }

    @Test
    void deleteTask_invalidIndex_throwsException() {
        taskList.addTask(new Todo("Only task"));

        assertThrows(CarrotException.class, () -> parser.deleteTask(ui, taskList, "5", storage));
    }

    @Test
    void findTask_noKeywordProvided_throwsException() {
        assertThrows(CarrotException.class, () -> parser.findTask(ui, null, taskList));
    }

    @Test
    void clearCommand_clearsTasksAndSaves() throws CarrotException {
        taskList.addTask(new Todo("Task 1"));
        taskList.addTask(new Deadline("Task 2", "2026-02-21"));

        assertEquals(2, taskList.getTasks().size());

        Response response = parser.command(ui, "clear", taskList, storage);

        assertEquals(Response.CommandType.CLEAR, response.getCommandType());
        assertEquals(0, taskList.getTasks().size());
        assertEquals(ui.showClearMessage(), response.getMessage());
        assertTrue(storage.load().isEmpty());
    }
}
