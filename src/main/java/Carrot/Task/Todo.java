package carrot.task;

/**
 * Represents a todo task.
 * Extends the Task class to add todo-specific functionality.
 */
public class Todo extends Task {

    /**
     * Constructor for Todo class
     * @param taskName Name of the todo task
     */
    public Todo(String taskName) {
        super(taskName);
    }

    @Override
    public Task createUpdatedTask(String description, String startDate, String endDate,
                                  String dueDate) {
        String newDescription = (description != null) ? description : this.taskName;
        return new Todo(newDescription);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    @Override
    public String getAddPrint() {
        return "New Todo Alert: " + this.taskName;
    }

    @Override
    public String saveToString() {
        return "T|" + (this.isComplete ? "1" : "0") + "|" + this.taskName;
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
