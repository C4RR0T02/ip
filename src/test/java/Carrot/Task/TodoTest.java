package carrot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void getTaskName_validName_returnsCorrectName() {
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getTaskName());
    }

    @Test
    public void getAddPrint_validTaskName_returnsCorrectMessages() {
        Todo todo = new Todo("read book");
        assertEquals("New Todo Alert: read book", todo.getAddPrint());
    }

    @Test
    public void saveToString_completedTask_returnsCorrectMessages() {
        Todo todo = new Todo("read book");
        todo.markCompleted();
        assertEquals("T|1|read book", todo.saveToString());
    }

    @Test
    public void saveToString_incompleteTask_returnsCorrectMessages() {
        Todo todo = new Todo("read book");
        assertEquals("T|0|read book", todo.saveToString());
    }

    @Test
    public void toString_validTaskName_returnsCorrectString() {
        Todo todo = new Todo("read book");
        assertEquals("[T] [ ] read book", todo.toString());
    }
}
