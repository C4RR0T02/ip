public abstract class Task {

    protected boolean isComplete;
    protected String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isComplete = false;
    }

    public void markCompleted() {
        this.isComplete = true;
    }

    public void markIncomplete() {
        this.isComplete = false;
    }

    @Override
    public String toString() {
        return "[" + (this.isComplete?"X":" ") + "] " + this.taskName;
    }
}
