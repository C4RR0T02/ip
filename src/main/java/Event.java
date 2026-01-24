public class Event extends Task {

    private String startDate;
    private String endDate;

    public Event(String taskName, String startDate, String endDate) {
        super(taskName);
        this.startDate = startDate;
        this.endDate = endDate;
        System.out.printf("%" + Prints.seperator.length() + "s%n", "New Event Alert: " + this.taskName + " occurring from " + this.startDate + " to " + this.endDate);
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + this.startDate + " to: " + this.endDate + ")";
    }
}
