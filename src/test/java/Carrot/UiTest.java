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
}
