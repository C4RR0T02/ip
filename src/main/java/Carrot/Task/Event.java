package carrot.task;

import java.time.LocalDateTime;

import carrot.DateFormatter;

/**
 * Represents an event task with a specific start date and time to a specific end date and time.
 * Extends the Task class to add event-specific functionality.
 */
public class Event extends Task {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    /**
     * Constructor for Event class
     * @param taskName Name of the event task
     * @param startDate Start date and time of the event in "yyyy-MM-dd HH:mm" format
     * @param endDate End date and time of the event in "yyyy-MM-dd HH:mm" format
     */
    public Event(String taskName, String startDate, String endDate) {
        super(taskName);
        this.startDate = LocalDateTime.parse(startDate, DateFormatter.FORMATTER);
        this.endDate = LocalDateTime.parse(endDate, DateFormatter.FORMATTER);
    }

    /**
     * Gets the start date of the event
     * @return The start date as LocalDateTime
     */
    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    /**
     * Gets the end date of the event
     * @return The end date as LocalDateTime
     */
    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public String getAddPrint() {
        return "New Event Alert: "
                + this.taskName
                + " occurring from "
                + this.startDate.format(DateFormatter.OUTPUT)
                + " to "
                + this.endDate.format(DateFormatter.OUTPUT);
    }

    @Override
    public String saveToString() {
        return "E|"
                + (this.isComplete ? "1" : "0")
                + "|"
                + this.taskName
                + "|"
                + this.startDate.format(DateFormatter.FORMATTER)
                + "|"
                + this.endDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public String toString() {
        return "[E] "
                + super.toString()
                + " (from: "
                + this.startDate.format(DateFormatter.OUTPUT)
                + " to: "
                + this.endDate.format(DateFormatter.OUTPUT) + ")";
    }

    @Override
    public String getStartDateFormatted() {
        return this.startDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public String getEndDateFormatted() {
        return this.endDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public Task createUpdatedTask(String description, String startDate, String endDate,
                                 String dueDate) {
        String newDescription = (description != null) ? description : this.taskName;
        String newStartDate = (startDate != null) ? startDate
                : this.startDate.format(DateFormatter.FORMATTER);
        String newEndDate = (endDate != null) ? endDate
                : this.endDate.format(DateFormatter.FORMATTER);
        return new Event(newDescription, newStartDate, newEndDate);
    }
}
