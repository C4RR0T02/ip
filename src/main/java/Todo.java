public class Todo extends Task {

    public Todo(String taskName) {
        super(taskName);
        System.out.printf("%" + Prints.seperator.length() + "s%n", "New Todo Alert: " + this.taskName);
    }

    public Todo(String taskName, boolean printStatus) {
        super(taskName);
        if (printStatus) {
            System.out.printf("%" + Prints.seperator.length() + "s%n", "New Todo Alert: " + this.taskName);
        }
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
