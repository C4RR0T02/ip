package carrot.task;

import java.time.LocalDateTime;

import carrot.DateFormatter;

/**
 * Represents a deadline task with a specific due date and time.
 * Extends the Task class to add deadline-specific functionality.
 */
public class Deadline extends Task {

    private final LocalDateTime dueDate;

    /**
     * Constructor for Deadline class
     * @param taskName Name of the deadline task
     * @param dueDate Due date and time of the task in "yyyy-MM-dd HH:mm" format
     */
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
        return "D|"
                + (this.isComplete ? "1" : "0")
                + "|"
                + this.taskName
                + "|"
                + this.dueDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + this.dueDate.format(DateFormatter.OUTPUT) + ")";
    }
}
