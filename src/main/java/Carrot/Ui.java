package Carrot;

import Carrot.Task.Task;

import java.util.ArrayList;

public class Ui {

    private static final String SEPARATOR = "-".repeat(80);
    private static final int SEPARATOR_LENGTH = SEPARATOR.length();
    private boolean isExit;

    /**
     * Constructor for Ui class
     */
    public Ui() {
        this.isExit = false;
    }

    /**
     * Prints a separator line
     */
    public void printLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the welcome message with logo
     */
    public void showWelcome() {
        String logo = """
         ____                     _
        / ___| __ _ _ __ _ __ ___| |_
       | |    / _` | '__| '__/ _ \\ __|
       | |___| (_| | |  | | | (_) | |_
        \\____|\\__,_|_|  |_|  \\___/ \\__|
       """;
        System.out.println(logo);
        System.out.println("Hello! I'm C4RR0T\nWhat can I do for you?");
        printLine();
    }

    /**
     * Prints the help message with available commands
     */
    public void printHelp() {
        System.out.println("""
                HELPPP is hereeee
                What do you need help with
                - Add a todo task: todo <task name>
                - Add a event: event <event name> /from <start date> /to <end date>
                - Add a deadline: deadline <deadline name> /by <deadline date>
                - Delete a Task: delete <task index>
                - List Tasks: list
                - Mark Task Completed: mark <task index>
                - Mark Task Incompleted: unmark <task index>
                - Exit the program: bye""");
        printLine();
    }

    /**
     * Prints the message when a task is added
     * @param task Task that was added
     */
    public void printAddTask(Task task) {
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", task.getAddPrint());
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", task);
    }

    /**
     * Prints the message when a task is deleted
     * @param task Task that was deleted
     */
    public void printDeleteTask(Task task) {
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", "Removed the task: " + task);
    }

    /**
     * Prints the list of tasks
     * @param list List of tasks to be printed
     */
    public void printTaskList(ArrayList<Task> list) {
        if (list.isEmpty()) {
            System.out.println("Empty");
            return;
        }
        for (int index = 1; index < (list.size() + 1); index++) {
            System.out.println("\t\t\t" + index + " " + list.get(index - 1));
        }
    }

    /**
     * Prints the message when commands are invalid
     */
    public void showInvalidCommands() {
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", "hmmmmm, I can't seem to find a command for that");
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", "Try screaming \"help\" for the full list of commands");
        printLine();
    }

    /**
     * Prints the message when there is an error loading the task list
     */
    public void showLoadingError() {
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", "Failed to load task list from saved file");
    }

    /**
     * Prints the exit message and sets the exit flag to true
     */
    public void exit() {
        isExit = true;
        System.out.printf("%" + SEPARATOR_LENGTH + "s%n", "Bye. Hope to see you again soon!");
        printLine();
    }

    /**
     * Toggles the exit flag
     */
    public void setExit() {
        this.isExit = !this.isExit;
    }

    /**
     * Checks if the exit flag is set
     * @return true if exit flag is set, false otherwise
     */
    public boolean isExit() {
        return this.isExit;
    }
}
