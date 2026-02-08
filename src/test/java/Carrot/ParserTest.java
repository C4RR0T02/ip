package Carrot;

import Carrot.Task.Deadline;
import Carrot.Task.Task;
import Carrot.Task.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    private Parser parser;
    private TaskList taskList;
    private Ui ui;
    private Storage storage;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @TempDir
    Path tempDir;

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
        parser.addTodo(ui, "Read Book", taskList, storage);
        assertEquals(1, taskList.getTasks().size());
        assertInstanceOf(Todo.class, taskList.getTasks().get(0));
        assertEquals("Read Book", taskList.getTasks().get(0).toString().contains("Read Book") ? "Read Book" : "");
    }

    @Test
    void addTodo_emptyArgs_throwsException() {
        assertThrows(CarrotException.class, () -> {
            parser.addTodo(ui, "", taskList, storage);
        });
    }

    @Test
    void addDeadline_validArgs_parsesCorrectly() throws CarrotException {
        parser.addDeadline(ui, "Submit Report /by 2026-02-08 23:59", taskList, storage);
        Task task = taskList.getTasks().get(0);
        assertInstanceOf(Deadline.class, task);
        assertTrue(task.toString().contains("Submit Report"));
    }

    @Test
    void addDeadline_missingBy_throwsException() {
        assertThrows(CarrotException.class, () -> {
            parser.addDeadline(ui, "Submit Report 2026-02-08", taskList, storage);
        });
    }

    @Test
    void mark_validIndex_updatesTask() throws CarrotException {
        taskList.addTask(new Todo("Initial Task"));
        parser.mark(ui, taskList.getTasks(), "1", storage);
        assertTrue(taskList.getTasks().get(0).toString().contains("[X]"));
    }

    @Test
    void getIndex_invalidNumber_throwsException() {
        assertThrows(CarrotException.class, () -> {
            parser.deleteTask(ui, taskList, "100", storage);
        });
    }

    @Test
    void findTask_validKeyword_printsMatchingTasks() throws CarrotException {
        taskList.addTask(new Todo("Buy fresh carrots"));
        taskList.addTask(new Todo("Eat healthy cake"));
        parser.findTask(ui, "carrot", taskList);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Buy fresh carrots"));
    }

    @Test
    void findTask_emptyArgs_throwsCarrotException() {
        assertThrows(CarrotException.class, () -> {
            parser.findTask(ui, "", taskList);
        });
    }

    @Test
    void findTask_noMatches_printsEmptyMessage() throws CarrotException {
        taskList.addTask(new Todo("Buy milk"));
        parser.findTask(ui, "carrot", taskList);
        String output = outputStreamCaptor.toString().trim();
        assert(output.contains("Empty Results ʕ•́ᴥ•̀ʔっ"));
    }
}
