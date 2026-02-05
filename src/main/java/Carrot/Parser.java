package Carrot;

import Carrot.Task.Deadline;
import Carrot.Task.Event;
import Carrot.Task.Task;
import Carrot.Task.Todo;

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    public void command(Ui ui, Scanner input, TaskList taskList, Storage storage) throws CarrotException {
        try {
            String userInput = input.nextLine();
            String[] split = userInput.split(" ", 2);
            String rootCmd = split[0];
            String args = (split.length > 1) ? split[1] : "";
            ArrayList<Task> tasks = taskList.getTaskList();

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
            case "help":
                ui.printHelp();
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ui.showInvalidCommands();
        }
    }

    public void mark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        try {
            int index = getIndex(args, taskListSize);
            taskList.get(index).markCompleted();
            ui.printTaskList(taskList);
            storage.save(taskList);
        } catch (NumberFormatException e) {
            throw new CarrotException("Error: The index to mark was not specified. Please type 'mark [task number]'%n");
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'mark [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first%n");
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'mark [task number]'%n");
        } finally {
            ui.printLine();
        }
    }

    public void unmark(Ui ui, ArrayList<Task> taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.size();
        try {
            int index = getIndex(args, taskListSize);
            taskList.get(index).markIncomplete();
            ui.printTaskList(taskList);
            storage.save(taskList);
        } catch (NumberFormatException e) {
            throw new CarrotException("Error: The index to unmark was not specified. Please type 'unmark [task number]'%n");
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'unmark [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first%n");
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'unmark [task number]'%n");
        } finally {
            ui.printLine();
        }
    }

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

    public void addEvent(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Event requires event name, event start date, and event end date%n");
            }
            String[] taskSplit = args.split("/from ", 2);
            String eventName = taskSplit[0].trim();
            String[] timeFrame = taskSplit[1].split("/to ", 2);
            String from = timeFrame[0].trim();
            String to = timeFrame[1].trim();
            Task newTask = new Event(eventName, from, to);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTaskList());
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'event [event name] /from [start date] /to [end date]'%n");
        } finally {
            ui.printLine();
        }
    }

    public void addDeadline(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Deadline requires a task name, and deadline date%n");
            }
            String[] taskSplit = args.split("/by ", 2);
            String eventName = taskSplit[0].trim();
            String deadline = taskSplit[1].trim();
            Task newTask = new Deadline(eventName, deadline);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTaskList());
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'deadline [task] /by [due date]'%n");
        } finally {
            ui.printLine();
        }
    }

    public void addTodo(Ui ui, String args, TaskList taskList, Storage storage) throws CarrotException {
        try {
            if (args.isEmpty()) {
                throw new CarrotException("Todo requires a task name%n");
            }
            Task newTask = new Todo(args);
            taskList.addTask(newTask);
            ui.printAddTask(newTask);
            storage.save(taskList.getTaskList());
        } catch (IndexOutOfBoundsException e) {
            throw new CarrotException("Error: Too many or too few arguments. Please type 'todo [task name]'%n");
        } finally {
            ui.printLine();
        }
    }

    public void deleteTask(Ui ui, TaskList taskList, String args, Storage storage) throws CarrotException {
        int taskListSize = taskList.getTaskList().size();
        try {
            int index = getIndex(args, taskListSize);
            Task removable = taskList.deleteTask(index);
            ui.printDeleteTask(removable);
            storage.save(taskList.getTaskList());
        } catch (NumberFormatException e) {
            System.out.println("Error: The index to delete was not specified. Please type 'delete [task number]'");
        } catch (NullPointerException e) {
            throw new CarrotException("Error: There is "
                    + taskListSize
                    + "items currently in the list. Please type 'delete [task number]' where task number is less than "
                    + taskListSize
                    + " or add to the list first%n");
        }catch (IndexOutOfBoundsException e) {
            System.out.printf("Error: Too many or too few arguments. Please type 'delete [task number]'%n");
        } finally {
            ui.printLine();
        }
    }

}
