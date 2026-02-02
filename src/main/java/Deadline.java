import java.time.LocalDateTime;

public class Deadline extends Task {

    private LocalDateTime dueDate;

    public Deadline(String taskName, String dueDate) {
        super(taskName);
        this.dueDate = LocalDateTime.parse(dueDate, DateFormatter.FORMATTER);
        System.out.printf("%" + Prints.seperator.length() + "s%n", "New Deadline Alert: " + this.taskName + " by " + this.dueDate.format(DateFormatter.OUTPUT));
    }

    public Deadline(String taskName, String dueDate, boolean printStatus) {
        super(taskName);
        this.dueDate = LocalDateTime.parse(dueDate, DateFormatter.OUTPUT);
        if (printStatus) {
            System.out.printf("%" + Prints.seperator.length() + "s%n", "New Deadline Alert: " + this.taskName + " by " + this.dueDate.format(DateFormatter.OUTPUT));
        }
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.dueDate.format(DateFormatter.OUTPUT) + ")";
    }
}
