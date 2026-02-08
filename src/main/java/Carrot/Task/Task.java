package Carrot.Task;

public abstract class Task {

    protected boolean isComplete;
    protected String taskName;

    /**
     * Constructor for Task class
     * @param taskName Name of the task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isComplete = false;
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
        return "[" + (this.isComplete?"X":" ") + "] " + this.taskName;
    }
}
