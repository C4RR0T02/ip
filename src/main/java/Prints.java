import java.util.ArrayList;

public class Prints {
    public static String seperator = "------------------------------------------------------------------------------------";

    public static void printLine() {
        System.out.println(seperator);
    }

    public static void printStart() {
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

    public static void printHelp() {
        System.out.println("HELPPP is hereeee\nWhat do you need help with");
        System.out.println("Add a todo task: todo <task name>");
        System.out.println("Add a event: event <event name> /from <start date> /to <end date>");
        System.out.println("Add a deadline: deadline <deadline name> /by <deadline date>");
        System.out.println("Delete a Task: delete <task index>");
        System.out.println("List Tasks: list");
        System.out.println("Mark Task Completed: mark <task index>");
        System.out.println("Mark Task Incompleted: unmark <task index>");
        System.out.println("Exit the program: bye");
        printLine();
    }

    public static void printExit() {
        System.out.printf("%" + seperator.length() + "s%n", "Bye. Hope to see you again soon!");
        printLine();
    }

    public static void printTaskList(ArrayList<Task> list, int currentSize) {
        for (int index = 1; index < (currentSize + 1); index++) {
            System.out.println("\t\t\t" + index + " " + list.get(index - 1));
        }
    }

    public static void invalidCommands() {
        System.out.printf("%" + seperator.length() + "s%n", "hmmmmm, I can't seem to find a command for that");
        System.out.printf("%" + seperator.length() + "s%n", "Try screaming \"help\" for the full list of commands");
        printLine();
    }
}
