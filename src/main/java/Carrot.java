import java.util.ArrayList;
import java.util.Scanner;

public class Carrot {

    public static final int LISTSIZE = 100;
    public static int currentSize = 0;
    public static boolean exit = false;
    public static ArrayList<Task> taskList = new ArrayList<Task>(LISTSIZE);

    public static void argsCheck(String[] inputs, int numArgs) {
        if (inputs.length != numArgs) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void echo(Scanner input) throws CarrotException {
        try {
            String userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("bye")) {
                exit = true;
            } else if (userInput.equalsIgnoreCase("list")) {
                Prints.printTaskList(taskList, currentSize);
                Prints.printLine();
            } else if (userInput.startsWith("mark ") || userInput.startsWith("unmark ")) {
                String[] inputs = userInput.split(" ");
                try {
                    argsCheck(inputs, 2);
                    int index = Integer.parseInt(inputs[1]) - 1;
                    if (index > currentSize) {
                        throw new NullPointerException();
                    }
                    if (inputs[0].startsWith("mark")) {
                        taskList.get(index).markCompleted();
                    } else {
                        taskList.get(index).markIncomplete();
                    }
                    Prints.printTaskList(taskList, currentSize);
                } catch (NumberFormatException e) {
                    System.out.printf("Error: The index to mark/unmark was not specified. Please type '%s [task number]'%n", inputs[0]);
                } catch (NullPointerException e) {
                    System.out.printf("Error: There is %d items currently in the list. Please type '%s [task number]' where task number is less than %d or add to the list first%n", currentSize, inputs[0], currentSize);
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Error: Too many or too few arguments. Please type '%s [task number]'%n", inputs[0]);
                } finally {
                    Prints.printLine();
                }
            } else if (userInput.startsWith("event ")) {
                String inputs = userInput.substring(6);
                try {
                    String[] taskSplit = inputs.split(" /from ");
                    argsCheck(taskSplit, 2);
                    String[] dateSplit = taskSplit[1].split(" /to ");
                    argsCheck(dateSplit, 2);
                    taskList.add(new Event(taskSplit[0], dateSplit[0], dateSplit[1]));
                    System.out.printf("%" + Prints.seperator.length() + "s%n", taskList.get(currentSize).toString());
                    currentSize++;
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Error: Too many or too few arguments. Please type 'event [event name] /from [start date] /to [end date]'%n");
                } finally {
                    Prints.printLine();
                }
            } else if (userInput.startsWith("deadline ")) {
                String inputs = userInput.substring(9);
                try {
                    String[] taskSplit = inputs.split(" /by ");
                    argsCheck(taskSplit, 2);
                    taskList.add(new Deadline(taskSplit[0], taskSplit[1]));
                    System.out.printf("%" + Prints.seperator.length() + "s%n", taskList.get(currentSize).toString());
                    currentSize++;
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Error: Too many or too few arguments. Please type 'deadline [task] /by [due date]'%n");
                } finally {
                    Prints.printLine();
                }
            } else if (userInput.startsWith("todo ")) {
                String inputs = userInput.substring(5);
                try {
                    taskList.add(new Todo(inputs));
                    System.out.printf("%" + Prints.seperator.length() + "s%n", taskList.get(currentSize).toString());
                    currentSize++;
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Error: Too many or too few arguments. Please type 'todo [task]'%n");
                } finally {
                    Prints.printLine();
                }
            } else if (userInput.startsWith("delete ")) {
                String inputs = userInput.substring(7);
                try {
                    String[] splitInput = inputs.split(" ");
                    argsCheck(splitInput, 1);
                    int index = Integer.parseInt(inputs) - 1;
                    Task removable = taskList.get(index);
                    taskList.remove(index);
                    System.out.printf("%" + Prints.seperator.length() + "s%n", "Removed the task: " + removable.toString());
                    currentSize--;
                } catch (NumberFormatException e) {
                    System.out.println("Error: The index to delete was not specified. Please type 'delete [task number]'");
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Error: Too many or too few arguments. Please type 'delete [task number]'%n");
                } finally {
                    Prints.printLine();
                }
            } else {
                throw new CarrotException();
            }
        } catch (CarrotException e) {
            Prints.invalidCommands();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            Prints.printStart();
            while (!exit) {
                echo(input);
            }
        } catch (Exception e) {
            System.out.println("An error has occurred, exiting program");
        } finally {
            Prints.printExit();
        }
    }
}
