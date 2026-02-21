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

    @Test
    public void markCompleted_todoTask_marksAsComplete() {
        Todo todo = new Todo("read book");
        todo.markCompleted();

        assertEquals("[T] [X] read book", todo.toString());
    }

    @Test
    public void markIncomplete_completedTodo_marksAsIncomplete() {
        Todo todo = new Todo("read book");
        todo.markCompleted();
        todo.markIncomplete();

        assertEquals("[T] [ ] read book", todo.toString());
    }

    @Test
    public void getTaskType_todo_returnsTodoType() {
        Todo todo = new Todo("read book");

        assertEquals(Task.TaskType.TODO, todo.getTaskType());
    }

    @Test
    public void createUpdatedTask_updateDescription_returnsNewTodo() {
        Todo todo = new Todo("read book");
        Todo updated = (Todo) todo.createUpdatedTask("write essay", null, null, null);

        assertEquals("write essay", updated.getTaskName());
    }

    @Test
    public void createUpdatedTask_nullDescription_keepsOriginal() {
        Todo todo = new Todo("read book");
        Todo updated = (Todo) todo.createUpdatedTask(null, null, null, null);

        assertEquals("read book", updated.getTaskName());
    }
}
