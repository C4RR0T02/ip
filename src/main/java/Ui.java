import java.util.ArrayList;

public class Ui {

    private static final String SEPERATOR = "-".repeat(80);
    private static final int SEPERATOR_LENGTH = SEPERATOR.length();
    private boolean isExit;

    public Ui() {
        this.isExit = false;
    }

    public void printLine() {
        System.out.println(SEPERATOR);
    }

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

    public void printAddTask(Task task) {
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", task.getAddPrint());
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", task);
    }

    public void printDeleteTask(Task task) {
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", "Removed the task: " + task);
    }

    public void printTaskList(ArrayList<Task> list) {
        if (list.isEmpty()) {
            System.out.println("Empty");
            return;
        }
        for (int index = 1; index < (list.size() + 1); index++) {
            System.out.println("\t\t\t" + index + " " + list.get(index - 1));
        }
    }

    public void showInvalidCommands() {
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", "hmmmmm, I can't seem to find a command for that");
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", "Try screaming \"help\" for the full list of commands");
        printLine();
    }

    public void showLoadingError() {
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", "Failed to load task list from saved file");
    }

    public void exit() {
        isExit = true;
        System.out.printf("%" + SEPERATOR_LENGTH + "s%n", "Bye. Hope to see you again soon!");
        printLine();
    }

    public boolean isExit() {
        return this.isExit;
    }
}
