package carrot;

/**
 * Helper class to hold parsed update parameters for task updates.
 * Contains description, start date, end date, and due date fields with accessor methods.
 */
public class UpdateParameters {
    private final String description;
    private final String startDate;
    private final String endDate;
    private final String dueDate;

    /**
     * Constructor for UpdateParameters class
     * @param description The new description (if null, keep original)
     * @param startDate The new start date for events (if null, keep original)
     * @param endDate The new end date for events (if null, keep original)
     * @param dueDate The new due date for deadlines (if null, keep original)
     */
    public UpdateParameters(String description, String startDate, String endDate,
                           String dueDate) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dueDate = dueDate;
    }

    /**
     * Gets the description
     * @return The description, or null if not provided
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the start date
     * @return The start date, or null if not provided
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date
     * @return The end date, or null if not provided
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Gets the due date
     * @return The due date, or null if not provided
     */
    public String getDueDate() {
        return dueDate;
    }
}
