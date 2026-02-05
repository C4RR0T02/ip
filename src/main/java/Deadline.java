import java.time.LocalDateTime;

public class Deadline extends Task {

    private final LocalDateTime dueDate;

    public Deadline(String taskName, String dueDate) {
        super(taskName);
        this.dueDate = LocalDateTime.parse(dueDate, DateFormatter.FORMATTER);
    }

    @Override
    public String getAddPrint() {
        return "New Deadline Alert: " + this.taskName + " by " + this.dueDate.format(DateFormatter.OUTPUT);
    }

    @Override
    public String saveToString() {
        return "D|" + (this.isComplete?"1":"0") + "|" + this.taskName + "|" + this.dueDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.dueDate.format(DateFormatter.OUTPUT) + ")";
    }
}
