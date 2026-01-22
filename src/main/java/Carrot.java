import java.util.Locale;
import java.util.Scanner;

public class Carrot {

    public static final int LISTSIZE = 100;
    public static int currentSize = 0;
    public static boolean exit = false;
    public static String seperator = "------------------------------------------";
    public static String[] taskList = new String[LISTSIZE];

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

    public static void printExit() {
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    public static void printList() {
        for (int index = 1; index < (currentSize + 1); index++) {
            System.out.println("\t" + index + " " + taskList[index]);
        }
    }

    public static void echo() {
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        switch (userInput.toLowerCase()) {
            case "bye":
                exit = true;
                printExit();
                break;
            case "list":
                printList();
                break;
            default:
                taskList[currentSize + 1] = userInput;
                userInput = "added: " + userInput;
                System.out.printf("%" + seperator.length() + "s%n", userInput);
                printLine();
                currentSize++;
                break;
        }
    }

    public static void main(String[] args) {
        printStart();
        while (!exit) {
            echo();
        }

    }
}
