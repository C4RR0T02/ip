import java.time.LocalDateTime;

public class Event extends Task {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Event(String taskName, String startDate, String endDate) {
        super(taskName);
        this.startDate = LocalDateTime.parse(startDate, DateFormatter.FORMATTER);
        this.endDate = LocalDateTime.parse(endDate, DateFormatter.FORMATTER);
        System.out.printf("%" + Prints.seperator.length() + "s%n", "New Event Alert: " + this.taskName + " occurring from " + this.startDate.format(DateFormatter.OUTPUT) + " to " + this.endDate.format(DateFormatter.OUTPUT));
    }

    public Event(String taskName, String startDate, String endDate, boolean printStatus) {
        super(taskName);
        this.startDate = LocalDateTime.parse(startDate, DateFormatter.OUTPUT);
        this.endDate = LocalDateTime.parse(endDate, DateFormatter.OUTPUT);
        if (printStatus) {
            System.out.printf("%" + Prints.seperator.length() + "s%n", "New Event Alert: " + this.taskName + " occurring from " + this.startDate.format(DateFormatter.OUTPUT) + " to " + this.endDate.format(DateFormatter.OUTPUT));
        }
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + this.startDate.format(DateFormatter.OUTPUT) + " to: " + this.endDate.format(DateFormatter.OUTPUT) + ")";
    }
}
