package carrot;

import java.util.ArrayList;
import java.time.LocalDateTime;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

/**
 * The Parser class processes user input commands and executes the corresponding task operations.
 */
public class Parser {

    /**
     * Parses the command string into a CommandType enum.
     *
     * @param commandStr The user input command string.
     * @param ui         The UI handler for displaying invalid command messages.
     * @return The parsed CommandType, or null if the command is invalid.
     */
    private Response tryParseCommand(String commandStr, Ui ui) {
        assert commandStr != null : "commandStr must not be null";
        assert ui != null : "ui must not be null";
        try {
            Response.CommandType.valueOf(commandStr);
            return null; // Successfully parsed
        } catch (IllegalArgumentException e) {
            return new Response("INVALID", ui.showInvalidCommands());
        }
    }

    /**
     * Executes the parsed command and returns the response.
     *
     * @param commandType The command type to execute.
     * @param ui          The UI handler.
     * @param args        The command arguments.
     * @param taskList    The task list manager.
     * @param storage     The storage handler.
     * @return The response from executing the command.
     * @throws CarrotException If there are issues executing the command.
     */
    private Response executeCommand(Response.CommandType commandType, Ui ui, String args,
                                    TaskList taskList, Storage storage) throws CarrotException {
        assert commandType != null : "commandType must not be null";
        assert ui != null : "ui must not be null";
        assert taskList != null : "taskList must not be null";
        assert storage != null : "storage must not be null";

        ArrayList<Task> tasks = taskList.getTasks();

        switch (commandType) {
        case BYE:
            ui.setExit();
            return new Response("BYE", ui.exit());
        case LIST:
            String message = ui.printTaskList(tasks) + System.lineSeparator();
            return new Response("LIST", message);
        case MARK:
            return mark(ui, tasks, args, storage);
        case UNMARK:
            return unmark(ui, tasks, args, storage);
        case DELETE:
            return deleteTask(ui, taskList, args, storage);
        case EVENT:
            return addEvent(ui, args, taskList, storage);
        case DEADLINE:
            return addDeadline(ui, args, taskList, storage);
        case TODO:
            return addTodo(ui, args, taskList, storage);
        case FIND:
            return findTask(ui, args, taskList);
        case UPDATE:
            return updateTask(ui, args, taskList, storage);
        case HELP:
            return new Response("HELP", ui.printHelp());
        default:
            return new Response("INVALID", ui.showInvalidCommands());
        }
    }

    /**
     * Processes the user input command and executes the corresponding action.
     *
     * @param ui        The user interface handler used to display feedback and messages.
     * @param input     The raw user input command string.
     * @param taskList  The manager containing the current list of tasks to be modified.
     * @param storage   The storage handler used to persist task data after modification.
     * @return          The response from executing the command.
     */
    public Response command(Ui ui, String input, TaskList taskList, Storage storage) {
        assert ui != null : "Ui should not be null";
        assert input != null : "Input should not be null";
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        try {
            String[] split = input.split(" ", 2);
            String commandStr = split[0].toUpperCase();
            String args = (split.length > 1) ? split[1] : "";

            Response parseError = tryParseCommand(commandStr, ui);
            if (parseError != null) {
                return parseError;
            }

            Response.CommandType commandType = Response.CommandType.valueOf(commandStr);
            return executeCommand(commandType, ui, args, taskList, storage);
        } catch (CarrotException e) {
            System.out.println(e.getMessage());
            return new Response("ERROR", e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return new Response("ERROR", "An unexpected error occurred");
        }
    }

    /**
     * Marks a task as completed based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after marking the task.
     * @throws CarrotException  If there are issues with the input or task modification.
     */
    public Response mark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        int index = getIndex("mark", args, taskListSize);
        assert index >= 0 && index < taskListSize : "Index should be within the bounds of the task list";
        assert taskList.get(index) != null : "Task at index should not be null";
        taskList.get(index).markCompleted();
        storage.save(taskList);
        String message = ui.printTaskList(taskList) + System.lineSeparator();
        return new Response("MARK", message);
    }

    /**
     * Unmarks a task as completed based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after unmarking the task.
     * @throws CarrotException  If there are issues with the input or task modification.
     */
    public Response unmark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        int index = getIndex("unmark", args, taskListSize);
        assert taskList.get(index) != null : "Task at index should not be null";
        taskList.get(index).markIncomplete();
        storage.save(taskList);
        String message = ui.printTaskList(taskList) + System.lineSeparator();
        return new Response("UNMARK", message);
    }

    /**
     * Converts user input into a valid task index.
     *
     * @param command        The command being executed (e.g., "mark", "unmark", "delete").
     * @param args           The arguments provided by the user, expected to contain the task index.
     * @param taskListSize   The current size of the task list.
     * @return               The zero-based index of the task.
     * @throws CarrotException If the input is invalid or out of range.
     */
    private static int getIndex(String command, String args, int taskListSize) throws CarrotException {
        assert args != null : "Arguments should not be null";
        assert command != null : "Command should not be null";
        assert taskListSize >= 0 : "Task list size should be non-negative";
        try {
            int index = Integer.parseInt(args) - 1;
            if (taskListSize <= index) {
                throw new CarrotException("Number out of Range");
            }
            if (index < 0) {
                throw new CarrotException("Negative Number Detected");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CarrotException("Error: '"
                    + args
                    + "' is not a valid index. There are "
                    + taskListSize
                    + " items in the list."
                    + System.lineSeparator()
                    + "Please use '" + command + " [number]'.");
        }
    }

    /**
     * Adds an event task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain event details.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after adding the event.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public Response addEvent(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        if (args == null || args.trim().isEmpty()) {
            throw new CarrotException("Error: Event details missing. "
                    + "Usage: event [name] /from [start] /to [end]");
        }
        if (!args.contains("/from") || !args.contains("/to")) {
            throw new CarrotException("Error: Missing time frame. "
                    + "Use /from for start date and /to for end date.");
        }
        try {
            Task newTask = getNewEvent(args);
            taskList.addTask(newTask);
            return new Response("EVENT", ui.printAddTask(newTask));
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Formatting incorrect. "
                    + "Please type 'event [task] /from [start] /to [end date]'");
        }
    }

    /**
     * Parses the user input to create a new Event task.
     *
     * @param args The raw arguments string containing the event details.
     * @return A new Event task created from the parsed details.
     * @throws CarrotException If the input format is incorrect or if required details are missing.
     */
    private static Task getNewEvent(String args) throws CarrotException {
        String[] taskSplit = args.split("/from ", 2);
        assert taskSplit.length == 2 : "Event details should contain both name and time frame";
        String eventName = taskSplit[0].trim();
        assert !eventName.isEmpty() : "Event name should not be empty";
        String[] timeFrame = taskSplit[1].split("/to ", 2);
        assert timeFrame.length == 2 : "Time frame should contain both start and end times";
        String from = timeFrame[0].trim();
        String to = timeFrame[1].trim();
        validateEventDateOrder(from, to);
        return new Event(eventName, from, to);
    }

    /**
     * Adds a deadline task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain deadline details.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after adding the deadline.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public Response addDeadline(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        if (args == null || args.trim().isEmpty()) {
            throw new CarrotException("Error: Deadline details are missing."
                    + "Please type 'deadline [task name] /by [date]'");
        }
        if (!args.contains("/by")) {
            throw new CarrotException("Error: Missing deadline date. Please use '/by' to specify the due date.");
        }
        try {
            Task newTask = getNewDeadline(args);
            taskList.addTask(newTask);
            return new Response("DEADLINE", ui.printAddTask(newTask));
        } catch (IndexOutOfBoundsException e) {
            String message = "Error: Too many or too few arguments. Please type 'deadline [task name] /by [due date]'";
            throw new CarrotException(message);
        }
    }

    /**
     * Parses the user input to create a new Deadline task.
     *
     * @param args The raw arguments string containing the deadline details.
     * @return A new Deadline task created from the parsed details.
     * @throws CarrotException If the input format is incorrect or if required details are missing.
     */
    private static Task getNewDeadline(String args) throws CarrotException {
        String[] taskSplit = args.split("/by ", 2);
        assert taskSplit.length == 2 : "Deadline details should contain both task name and deadline date";
        String eventName = taskSplit[0].trim();
        if (eventName.isEmpty()) {
            throw new CarrotException("Error: The description of a deadline cannot be empty. "
                    + "Please type 'deadline [task name] /by [due date]'");
        }
        String deadline = taskSplit[1].trim();
        return new Deadline(eventName, deadline);
    }

    /**
     * Adds a todo task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain the todo description.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after adding the todo.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public Response addTodo(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        if (args == null || args.trim().isEmpty()) {
            throw new CarrotException("Error: The description of a todo cannot be empty. "
                    + "Usage: todo [task name]");
        }
        Task newTask = new Todo(args.trim());
        taskList.addTask(newTask);
        return new Response("TODO", ui.printAddTask(newTask));
    }

    /**
     * Deletes a task from the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @return                  The response string after deleting the task.
     * @throws CarrotException  If there are issues with the input or task deletion.
     */
    public Response deleteTask(Ui ui, TaskList taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.getTasks().size();
        int index = getIndex("delete", args, taskListSize);
        Task removable = taskList.deleteTask(index);
        return new Response("DELETE", ui.printDeleteTask(removable));
    }

    /**
     * Finds tasks in the task list that match the given keyword.
     *
     * @param ui                The user interface to print messages.
     * @param args              The keyword to search for.
     * @param taskList          The task list to search within.
     * @return                  The response string after finding the tasks.
     * @throws CarrotException  If there is an error with the input arguments.
     */
    public Response findTask(Ui ui, String args, TaskList taskList) throws CarrotException {
        if (args == null || args.trim().isEmpty()) {
            throw new CarrotException("Error: The search keyword cannot be empty. Usage: 'find [keyword]'");
        }
        ArrayList<Task> foundTasks = new ArrayList<>();
        taskList.findTasks(args, foundTasks);
        return new Response("FIND", ui.printTaskList(foundTasks));
    }

    /**
     * Updates a task in the task list based on user input.
     * Supports partial updates - users can update individual fields.
     *
     * @param ui       The user interface handler used to display feedback and messages.
     * @param args     The arguments provided by the user, expected to contain index and update params.
     * @param taskList The manager containing the current list of tasks to be modified.
     * @param storage  The storage handler used to persist task data after modification.
     * @return The response string after updating the task.
     * @throws CarrotException If there are issues with the input or task update.
     */
    public Response updateTask(Ui ui, String args, TaskList taskList, Storage storage)
            throws CarrotException {
        assert ui != null : "Ui should not be null";
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";

        if (args == null || args.trim().isEmpty()) {
            throw new CarrotException("Error: Missing arguments. "
                    + "Usage: 'update [index] /d [new description]' for Todos, "
                    + "or 'update [index] /d [new description] /by [date]' for deadlines, "
                    + "or 'update [index] /d [new description] /from [start] /to [end]' for "
                    + "events");
        }

        String[] splitArgs = args.split(" ", 2);
        if (splitArgs.length < 2) {
            throw new CarrotException("Error: Missing update parameters. "
                    + "Usage: 'update [index] /d [description]' for Todos");
        }

        int index = getIndex("update", splitArgs[0], taskList.getTasks().size());
        Task oldTask = taskList.getTasks().get(index);
        assert oldTask != null : "Task at index should not be null";

        String updateArgs = splitArgs[1];
        Task.TaskType taskType = oldTask.getTaskType();

        // Validate parameters based on task type
        validateUpdateParameters(updateArgs, taskType);

        // Parse update parameters
        UpdateParameters params = parseUpdateParameters(updateArgs, taskType);

        if (taskType == Task.TaskType.EVENT) {
            String effectiveStart = params.getStartDate() != null
                    ? params.getStartDate()
                    : oldTask.getStartDateFormatted();
            String effectiveEnd = params.getEndDate() != null
                    ? params.getEndDate()
                    : oldTask.getEndDateFormatted();
            validateEventDateOrder(effectiveStart, effectiveEnd);
        }

        // Create updated task using polymorphic method
        Task newTask = oldTask.createUpdatedTask(params.getDescription(), params.getStartDate(),
                params.getEndDate(), params.getDueDate());

        taskList.updateTask(index, newTask);
        storage.save(taskList.getTasks());
        return new Response("UPDATE", ui.printUpdateMessage(oldTask, newTask));
    }

    /**
     * Validates update parameters based on task type.
     *
     * @param updateArgs The update arguments string.
     * @param taskType   The type of task being updated.
     * @throws CarrotException If invalid parameters are provided for the task type.
     */
    private void validateUpdateParameters(String updateArgs, Task.TaskType taskType)
            throws CarrotException {
        if (!updateArgs.contains("/d") && !updateArgs.contains("/by")
                && !updateArgs.contains("/from") && !updateArgs.contains("/to")) {
            throw new CarrotException("Error: No valid update parameters provided.");
        }

        if (taskType == Task.TaskType.TODO) {
            validateTodoParameters(updateArgs);
        } else if (taskType == Task.TaskType.DEADLINE) {
            validateDeadlineParameters(updateArgs);
        } else if (taskType == Task.TaskType.EVENT) {
            validateEventParameters(updateArgs);
        }
    }

    /**
     * Validates update parameters for Todo tasks.
     *
     * @param updateArgs The update arguments string.
     * @throws CarrotException If invalid parameters are provided for Todo.
     */
    private void validateTodoParameters(String updateArgs) throws CarrotException {
        if (!updateArgs.contains("/d")) {
            throw new CarrotException("Error: Todos only support /d parameter. "
                    + "Usage: 'update [index] /d [new description]'");
        }
        if (updateArgs.contains("/by") || updateArgs.contains("/from")
                || updateArgs.contains("/to")) {
            throw new CarrotException("Error: Todos do not support /by, /from, or /to parameters. "
                    + "Usage: 'update [index] /d [new description]'");
        }
    }

    /**
     * Validates update parameters for Deadline tasks.
     *
     * @param updateArgs The update arguments string.
     * @throws CarrotException If invalid parameters are provided for Deadline.
     */
    private void validateDeadlineParameters(String updateArgs) throws CarrotException {
        if (updateArgs.contains("/from") || updateArgs.contains("/to")) {
            throw new CarrotException("Error: Deadlines do not support /from or /to parameters. "
                    + "Usage: 'update [index] /d [description] /by [date]'");
        }
    }

    /**
     * Validates update parameters for Event tasks.
     *
     * @param updateArgs The update arguments string.
     * @throws CarrotException If invalid parameters are provided for Event.
     */
    private void validateEventParameters(String updateArgs) throws CarrotException {
        if (updateArgs.contains("/by")) {
            throw new CarrotException("Error: Events do not support /by parameter. "
                    + "Usage: 'update [index] /d [description] /from [start] /to [end]'");
        }
    }

    /**
     * Parses update parameters from the argument string.
     * Delegates to specific helper methods for each parameter type.
     *
     * @param updateArgs The update arguments string.
     * @param taskType   The type of task being updated.
     * @return UpdateParameters object with parsed values.
     * @throws CarrotException If the update format is invalid.
     */
    private UpdateParameters parseUpdateParameters(String updateArgs,
                                                   Task.TaskType taskType)
            throws CarrotException {
        String description = parseDescription(updateArgs);
        String startDate = parseStartDate(updateArgs, taskType);
        String endDate = parseEndDate(updateArgs, taskType);
        String dueDate = parseDueDate(updateArgs, taskType);

        return new UpdateParameters(description, startDate, endDate, dueDate);
    }

    /**
     * Parses the description field from update arguments.
     *
     * @param updateArgs The update arguments string.
     * @return The parsed description, or null if not provided.
     * @throws CarrotException If the description is empty.
     */
    private String parseDescription(String updateArgs) throws CarrotException {
        if (!updateArgs.contains("/d")) {
            return null;
        }

        String[] descriptionSplit = updateArgs.split("/d ", 2);
        String desc = descriptionSplit[1].trim();

        if (desc.isEmpty()) {
            throw new CarrotException("Error: Description cannot be empty.");
        }

        String[] parts = desc.split("\\s+/", 2);
        String description = parts[0].trim();

        if (description.isEmpty()) {
            throw new CarrotException("Error: Description cannot be empty.");
        }

        return description;
    }

    /**
     * Parses the start date field from update arguments (Event only).
     *
     * @param updateArgs The update arguments string.
     * @param taskType   The type of task being updated.
     * @return The parsed start date, or null if not provided.
     * @throws CarrotException If the date is empty or invalid for task type.
     */
    private String parseStartDate(String updateArgs, Task.TaskType taskType)
            throws CarrotException {
        if (!updateArgs.contains("/from")) {
            return null;
        }

        if (taskType != Task.TaskType.EVENT) {
            throw new CarrotException("Error: /from parameter is only supported for Event tasks. "
                    + "You are updating a " + taskType.name().toLowerCase() + " task.");
        }

        String[] fromSplit = updateArgs.split("/from ", 2);
        String fromContent = fromSplit[1].trim();
        String[] toSplit = fromContent.split("\\s+/to", 2);
        String startDate = toSplit[0].trim();

        if (startDate.isEmpty()) {
            throw new CarrotException("Error: Start date cannot be empty. "
                    + "Usage: 'update [index] /from [date]'");
        }

        return startDate;
    }

    /**
     * Parses the end date field from update arguments (Event only).
     *
     * @param updateArgs The update arguments string.
     * @param taskType   The type of task being updated.
     * @return The parsed end date, or null if not provided.
     * @throws CarrotException If the date is empty or invalid for task type.
     */
    private String parseEndDate(String updateArgs, Task.TaskType taskType)
            throws CarrotException {
        if (!updateArgs.contains("/to")) {
            return null;
        }

        if (taskType != Task.TaskType.EVENT) {
            throw new CarrotException("Error: /to parameter is only supported for Event tasks. "
                    + "You are updating a " + taskType.name().toLowerCase() + " task.");
        }

        String[] toSplit = updateArgs.split("/to ", 2);
        String toContent = toSplit[1].trim();
        String[] nextParamSplit = toContent.split("\\s+/", 2);
        String endDate = nextParamSplit[0].trim();

        if (endDate.isEmpty()) {
            throw new CarrotException("Error: End date cannot be empty. "
                    + "Usage: 'update [index] /to [date]'");
        }

        return endDate;
    }

    /**
     * Parses the due date field from update arguments (Deadline only).
     *
     * @param updateArgs The update arguments string.
     * @param taskType   The type of task being updated.
     * @return The parsed due date, or null if not provided.
     * @throws CarrotException If the date is empty or invalid for task type.
     */
    private String parseDueDate(String updateArgs, Task.TaskType taskType)
            throws CarrotException {
        if (!updateArgs.contains("/by")) {
            return null;
        }

        if (taskType != Task.TaskType.DEADLINE) {
            throw new CarrotException("Error: /by parameter is only supported for Deadline tasks. "
                    + "You are updating a " + taskType.name().toLowerCase() + " task.");
        }

        String[] bySplit = updateArgs.split("/by ", 2);
        String byContent = bySplit[1].trim();
        String[] nextParamSplit = byContent.split("\\s+/", 2);
        String dueDate = nextParamSplit[0].trim();

        if (dueDate.isEmpty()) {
            throw new CarrotException("Error: Due date cannot be empty. "
                    + "Usage: 'update [index] /by [date]'");
        }

        return dueDate;
    }

    /**
     * Validates that the event start date is not after the end date.
     *
     * @param startDate The start date string to validate.
     * @param endDate   The end date string to validate.
     * @throws CarrotException If the date order is invalid.
     */
    private static void validateEventDateOrder(String startDate, String endDate)
            throws CarrotException {
        LocalDateTime start = LocalDateTime.parse(startDate, DateFormatter.FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endDate, DateFormatter.FORMATTER);
        if (start.isAfter(end)) {
            throw new CarrotException("Error: Event start date must be the same as or earlier than end date.");
        }
    }
}
