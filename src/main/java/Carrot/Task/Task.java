package carrot.task;

/**
 * Abstract class representing a general Task.
 * Contains common attributes and methods for all task types.
 */
public abstract class Task {

    protected boolean isComplete;
    protected String taskName;

    /**
    * Enum representing the type of task.
    */
    public enum TaskType { TASK, TODO, DEADLINE, EVENT };

    /**
     * Constructor for Task class
     * @param taskName Name of the task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isComplete = false;
    }

    /**
     * Returns the name of the task.
     *
     * @return The name of the task.
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Marks the task as completed
     */
    public void markCompleted() {
        this.isComplete = true;
    }

    /**
     * Marks the task as incompleted
     */
    public void markIncomplete() {
        this.isComplete = false;
    }

    /**
     * Gets the start date of the task (for Event only).
     * Default implementation throws exception for non-Event tasks.
     *
     * @return The start date as formatted string
     * @throws UnsupportedOperationException if called on non-Event task
     */
    public String getStartDateFormatted() {
        throw new UnsupportedOperationException(
                "Error: Start date is not supported for " + getTaskType() + " tasks");
    }

    /**
     * Gets the end date of the task (for Event only).
     * Default implementation throws exception for non-Event tasks.
     *
     * @return The end date as formatted string
     * @throws UnsupportedOperationException if called on non-Event task
     */
    public String getEndDateFormatted() {
        throw new UnsupportedOperationException(
                "Error: End date is not supported for " + getTaskType() + " tasks");
    }

    /**
     * Gets the due date of the task (for Deadline only).
     * Default implementation throws exception for non-Deadline tasks.
     *
     * @return The due date as formatted string
     * @throws UnsupportedOperationException if called on non-Deadline task
     */
    public String getDueDateFormatted() {
        throw new UnsupportedOperationException(
                "Error: Due date is not supported for " + getTaskType() + " tasks");
    }

    /**
     * Updates the task with new field values.
     * Subclasses should override to support their specific fields.
     *
     * @param description The new description (if null, keep original)
     * @param startDate The new start date for events (if null, keep original)
     * @param endDate The new end date for events (if null, keep original)
     * @param dueDate The new due date for deadlines (if null, keep original)
     * @return A new Task instance with updated values
     */
    public abstract Task createUpdatedTask(String description, String startDate,
                                           String endDate, String dueDate);

    /**
     * Abstract method to get the task type
     * @return Task type
     */
    public abstract TaskType getTaskType();

    /**
     * Abstract method to get the addition print message
     * @return Addition print message
     */
    public abstract String getAddPrint();

    /**
     * Abstract method to save the task to string
     * @return String representation of the task for saving
     */
    public abstract String saveToString();

    /**
     * String representation of the task
     * @return String representation of the task
     */
    @Override
    public String toString() {
        return "[" + (this.isComplete ? "X" : " ") + "] " + this.taskName;
    }
}
