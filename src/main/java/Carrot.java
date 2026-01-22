import java.util.Scanner;

public class Carrot {

    public static boolean exit = false;
    public static String seperator = "------------------------------------------";

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

    public static void echo() {
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        if (userInput.equalsIgnoreCase("bye")) {
            exit = true;
            printExit();
        } else {
            System.out.printf("%" + seperator.length() + "s%n", userInput);
            printLine();
        }
    }

    public static void main(String[] args) {
        printStart();
        while (!exit) {
            echo();
        }

    }
}
