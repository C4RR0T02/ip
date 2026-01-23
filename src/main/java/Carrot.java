import java.util.Scanner;

public class Carrot {

    public static final int LISTSIZE = 100;
    public static int currentSize = 0;
    public static boolean exit = false;
    public static String seperator = "------------------------------------------";
    public static Task[] taskList = new Task[LISTSIZE];

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
            System.out.println("\t" + index + " " + taskList[index - 1].toString());
        }
    }

    public static void argsCheck(String[] inputs, int numArgs) {
        if (inputs.length != numArgs) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void echo() {
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        if (userInput.equalsIgnoreCase("bye")) {
            exit = true;
            printExit();
        } else if (userInput.equalsIgnoreCase("list")) {
            printList();
            printLine();
        } else if (userInput.startsWith("mark ") || userInput.startsWith("unmark ")) {
            String[] inputs = userInput.split(" ");
            argsCheck(inputs, 2);
            try {
                int index = Integer.parseInt(inputs[1]) - 1;
                if (inputs[0].startsWith("mark")) {
                    taskList[index].markCompleted();
                } else {
                    taskList[index].markIncomplete();
                }
                printList();
            } catch (NumberFormatException e) {
                System.out.printf("Error: The index to mark/unmark was not specified. Please type '%s [number]'.%n", inputs[0]);
            } catch (NullPointerException e) {
                System.out.printf("Error: There is %d items currently in the list. Please type '%s [number]' or add to the list first.%n", currentSize, inputs[0]);
            } catch (IndexOutOfBoundsException e) {
                System.out.printf("Error: Too many or too few arguments. Please type '%s [number]'.%n", inputs[0]);
            } finally {
                printLine();
            }
        } else {
            taskList[currentSize] = new Task(userInput);
            userInput = "added: " + userInput;
            System.out.printf("%" + seperator.length() + "s%n", userInput);
            printLine();
            currentSize++;
        }
    }

    public static void main(String[] args) {
        printStart();
        while (!exit) {
            echo();
        }

    }
}
