public class Deadline extends Task {

    private String dueDate;

    public Deadline(String taskName, String dueDate) {
        super(taskName);
        this.dueDate = dueDate;
        System.out.printf("%" + Prints.seperator.length() + "s%n", "New Deadline Alert: " + this.taskName + " by " + this.dueDate);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.dueDate + ")";
    }
}
