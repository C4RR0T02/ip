package Carrot.Task;

public class Todo extends Task {

    public Todo(String taskName) {
        super(taskName);
    }

    @Override
    public String getAddPrint() {
        return  "New Todo Alert: " + this.taskName;
    }

    @Override
    public String saveToString() {
        return "T|" + (this.isComplete?"1":"0") + "|" + this.taskName;
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
