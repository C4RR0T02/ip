package carrot;

import java.util.ArrayList;
import java.util.Scanner;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

/**
 * The Parser class processes user input commands and executes the corresponding task operations.
 */
public class Parser {

    /**
     * Processes user input commands and executes the corresponding task operations.
     *
     * @param ui The user interface handler used to display feedback and messages.
     * @param input The Scanner source used to read the user's raw input strings.
     * @param taskList The manager containing the current list of tasks to be modified.
     * @param storage The storage handler used to persist task data after modifications.
     */
    public void command(Ui ui, Scanner input, TaskList taskList, Storage storage) {
        try {
            String userInput = input.nextLine();
            String[] split = userInput.split(" ", 2);
            String rootCmd = split[0];
            String args = (split.length > 1) ? split[1] : "";
            ArrayList<Task> tasks = taskList.getTasks();

            switch (rootCmd) {
            case "bye":
                ui.setExit();
                break;
            case "list":
                ui.printTaskList(tasks);
                ui.printLine();
                break;
            case "mark":
                mark(ui, tasks, args, storage);
                break;
            case "unmark":
                unmark(ui, tasks, args, storage);
                break;
            case "delete":
                deleteTask(ui, taskList, args, storage);
                break;
            case "event":
                addEvent(ui, args, taskList, storage);
                break;
            case "deadline":
                addDeadline(ui, args, taskList, storage);
                break;
            case "todo":
                addTodo(ui, args, taskList, storage);
                break;
            case "find":
                findTask(ui, args, taskList);
                break;
            case "help":
                ui.printHelp();
                break;
            default:
                ui.showInvalidCommands();
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ui.showInvalidCommands();
        }
    }

    /**
     * Marks a task as completed based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task modification.
     */
    public void mark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        try {
            int index = getIndex(args, taskListSize);
            taskList.get(index).markCompleted();
            ui.printTaskList(taskList);
            storage.save(taskList);
        } catch (NumberFormatException e) {
            throw new CarrotException("Error: The index to mark was not specified. Please type 'mark [task number]'");
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'mark [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first");
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'mark [task number]'");
        } finally {
            ui.printLine();
        }
    }

    /**
     * Unmarks a task as completed based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task modification.
     */
    public void unmark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        try {
            int index = getIndex(args, taskListSize);
            taskList.get(index).markIncomplete();
            ui.printTaskList(taskList);
            storage.save(taskList);
        } catch (NumberFormatException e) {
            String message = "Error: The index to unmark was not specified. Please type 'unmark [task number]'";
            throw new CarrotException(message);
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'unmark [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first");
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'unmark [task number]'");
        } finally {
            ui.printLine();
        }
    }

    /**
     * Converts user input into a valid task index.
     *
     * @param args              The user input string representing the task index.
     * @param taskListSize      The current size of the task list.
     * @return                  The zero-based index of the task.
     * @throws CarrotException  If the index is out of range or negative.
     */
    private static int getIndex(String args, int taskListSize) throws CarrotException {
        int index = Integer.parseInt(args) - 1;
        if (taskListSize < index) {
            throw new CarrotException("Number out of Range");
        }
        if (index < 0) {
            throw new CarrotException("Negative Number Detected");
        }
        return index;
    }

    /**
     * Adds an event task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain event details.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public void addEvent(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Event requires event name, event start date, and event end date");
            }
            String[] taskSplit = args.split("/from ", 2);
            String eventName = taskSplit[0].trim();
            String[] timeFrame = taskSplit[1].split("/to ", 2);
            String from = timeFrame[0].trim();
            String to = timeFrame[1].trim();
            Task newTask = new Event(eventName, from, to);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTasks());
        } catch (IndexOutOfBoundsException e) {
            String message = "Error: Too many or too few arguments. "
                    + "Please type 'event [task] /from [start date] /to [end date]'";
            throw new CarrotException(message);
        } finally {
            ui.printLine();
        }
    }

    /**
     * Adds a deadline task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain deadline details.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public void addDeadline(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Deadline requires a task name, and deadline date");
            }
            String[] taskSplit = args.split("/by ", 2);
            String eventName = taskSplit[0].trim();
            String deadline = taskSplit[1].trim();
            Task newTask = new Deadline(eventName, deadline);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTasks());
        } catch (IndexOutOfBoundsException e) {
            String message = "Error: Too many or too few arguments. Please type 'deadline [task] /by [due date]'";
            throw new CarrotException(message);
        } finally {
            ui.printLine();
        }
    }

    /**
     * Adds a todo task to the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param args              The arguments provided by the user, expected to contain the todo task name.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task addition.
     */
    public void addTodo(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Todo requires a task name");
            }
            Task newTask = new Todo(args);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTasks());
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'todo [task name]'");
        } finally {
            ui.printLine();
        }
    }

    /**
     * Deletes a task from the task list based on user input.
     *
     * @param ui                The user interface handler used to display feedback and messages.
     * @param taskList          The manager containing the current list of tasks to be modified.
     * @param args              The arguments provided by the user, expected to contain the task index.
     * @param storage           The storage handler used to persist task data after modification.
     * @throws CarrotException  If there are issues with the input or task deletion.
     */
    public void deleteTask(Ui ui, TaskList taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.getTasks().size();
        try {
            int index = getIndex(args, taskListSize);
            Task removable = taskList.deleteTask(index);
            ui.printDeleteTask(removable);
            storage.save(taskList.getTasks());
        } catch (NumberFormatException e) {
            System.out.println("Error: The index to delete was not specified. Please type 'delete [task number]'");
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'delete [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first");
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'delete [task number]'");
        } finally {
            ui.printLine();
        }
    }

    /**
     * Finds tasks in the task list that match the given keyword.
     *
     * @param ui                The user interface to print messages.
     * @param args              The keyword to search for.
     * @param taskList          The task list to search within.
     * @throws CarrotException  If there is an error with the input arguments.
     */
    public void findTask(Ui ui, String args, TaskList taskList) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Find requires a keyword to search for");
            }
            ArrayList<Task> foundTasks = new ArrayList<>();
            taskList.findTasks(args, foundTasks);
            ui.printTaskList(foundTasks);
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'find [keyword]'");
        } finally {
            ui.printLine();
        }
    }
}
