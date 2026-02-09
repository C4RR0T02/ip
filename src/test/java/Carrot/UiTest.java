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
        ui.showWelcome();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Hello! I'm C4RR0T"));
        assertTrue(output.contains("What can I do for you?"));
    }

    @Test
    void testPrintTaskList_emptyList_printsEmpty() {
        ArrayList<Task> emptyList = new ArrayList<>();
        ui.printTaskList(emptyList);
        assertEquals("Empty Results ʕ•́ᴥ•̀ʔっ", outputStreamCaptor.toString().trim());
    }

    @Test
    void testPrintTaskList_populatedList_printsTasks() {
        ArrayList<Task> list = new ArrayList<>();
        list.add(new Todo("Buy Milk"));
        ui.printTaskList(list);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("1 [T] [ ] Buy Milk"));
    }

    @Test
    void testExit_setsIsExitToTrue() {
        assertFalse(ui.isExit());
        ui.exit();
        assertTrue(ui.isExit());
        assertTrue(outputStreamCaptor.toString().contains("Bye. Hope to see you again soon!"));
    }

    @Test
    void testShowLoadingError_printsErrorMessage() {
        ui.showLoadingError();
        assertTrue(outputStreamCaptor.toString().contains("Failed to load task list"));
    }
}
