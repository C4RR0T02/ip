package carrot;

import java.util.ArrayList;

import carrot.task.Task;

/**
 * Handles user interface interactions, including displaying messages and prompts.
 */
public class Ui {

    protected boolean isExit;

    /**
     * Constructor for Ui class
     */
    public Ui() {
        this.isExit = false;
    }

    /**
     * Shows the welcome message
     * @return String representation of the welcome message
     */
    public String showWelcome() {
        return "Salut! Je suis C4RR0T" + System.lineSeparator() + "What can I do for you?";
    }

    /**
     * Shows the help message
     * @return String representation of the help message
     */
    public String printHelp() {
        return ("""
                Aide is hereeee
                What do you need help with
                - Add a todo task: todo <task name>
                - Add a event: event <event name> /from <start date> /to <end date>
                - Add a deadline: deadline <deadline name> /by <deadline date>
                - Delete a Task: delete <task index>
                - Update a Task: update <task index> <task arguments>
                                    /d <new task description>)
                                    /by <new deadline date>)
                                    /from <new start date>
                                    /to <new end date>
                - List Tasks: list
                - Mark Task Completed: mark <task index>
                - Mark Task Incompleted: unmark <task index>
                - Find Tasks: find <keyword>
                - Print Help: help
                - Exit the program: bye"""
                + System.lineSeparator());
    }

    /**
     * Shows the added task message
     * @param task Task that was added
     * @return String representation of the added task message
     */
    public String printAddTask(Task task) {
        return task.getAddPrint() + System.lineSeparator() + task;
    }

    /**
     * Shows the deleted task message
     * @param task Task that was deleted
     * @return String representation of the deleted task message
     */
    public String printDeleteTask(Task task) {
        return "Removed the task: " + task;
    }

    /**
     * Shows the list of tasks
     * @param list ArrayList of tasks to be printed
     * @return String representation of the list of tasks
     */
    public String printTaskList(ArrayList<Task> list) {
        if (list.isEmpty()) {
            return "Empty Results ʕ•́ᴥ•̀ʔっ";
        }
        String string = "";
        for (int index = 1; index < (list.size() + 1); index++) {
            string = string + index + " " + list.get(index - 1) + System.lineSeparator();
        }
        return string;
    }

    /**
    * Shows the updated task message
    * @param oldTask Task before the update
    * @param newTask Task after the update
    * @return String representation of the updated task message
    */
    public String printUpdateMessage(Task oldTask, Task newTask) {
        return "Updated the task"
                + System.lineSeparator()
                + "from: "
                + oldTask
                + System.lineSeparator()
                + " to: "
                + newTask;
    }

    /**
     * Shows invalid command message
     * @return String representation of the invalid command message
     */
    public String showInvalidCommands() {
        return "hmmmmm, I can't seem to find a command for that"
                + System.lineSeparator()
                + "Try screaming \"help\" for the full list of commands";
    }

    /**
     * Shows loading error message
     * @return String representation of the loading error message
     */
    public String showLoadingError() {
        return "Failed to load task list from saved file";
    }

    /**
     * Shows the exit message
     * @return String representation of the exit message
     */
    public String exit() {
        isExit = true;
        return "Au revoir";
    }

    /**
     * Toggles the exit status
     */
    public void setExit() {
        this.isExit = !this.isExit;
    }

    /**
     * Gets the exit status
     * @return boolean indicating if the program is set to exit
     */
    public boolean isExit() {
        return this.isExit;
    }

    /**
     * Shows the clear message
     * @return String representation of the clear message
     */
    public String showClearMessage() {
        return "Cleared all tasks from the list";
    }
}
