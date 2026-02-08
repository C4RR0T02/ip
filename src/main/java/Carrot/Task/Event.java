package Carrot.Task;

import Carrot.DateFormatter;

import java.time.LocalDateTime;

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

    @Override
    public String getAddPrint() {
        return "New Event Alert: " + this.taskName + " occurring from " + this.startDate.format(DateFormatter.OUTPUT) + " to " + this.endDate.format(DateFormatter.OUTPUT);
    }

    @Override
    public String saveToString() {
        return "E|" + (this.isComplete?"1":"0") + "|" + this.taskName + "|" + this.startDate.format(DateFormatter.FORMATTER) + "|" + this.endDate.format(DateFormatter.FORMATTER);
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + this.startDate.format(DateFormatter.OUTPUT) + " to: " + this.endDate.format(DateFormatter.OUTPUT) + ")";
    }
}
