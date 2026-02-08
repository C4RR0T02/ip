package Carrot;

import Carrot.Task.Task;

import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;
    private final Storage storage;

    /**
     * Constructor for TaskList class
     * @param storage Storage object for saving and loading tasks
     */
    public TaskList(Storage storage) {
        this.storage = storage;
        this.tasks = new ArrayList<>();
    }

    /**
     * Gets the current list of tasks
     * @return ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds a new task to the task list and saves it
     * @param newTask Task to be added
     */
    public void addTask(Task newTask) {
        this.tasks.add(newTask);
        storage.save(this.tasks);
    }

    /**
     * Deletes a task from the task list by index and saves the updated list
     * @param index Index of the task to be deleted
     * @return The removed Task
     */
    public Task deleteTask(int index) {
        Task removable = this.tasks.get(index);
        this.tasks.remove(removable);
        storage.save(this.tasks);
        return removable;
    }

    /**
     * Loads the task list from storage
     * @throws CarrotException if there is an error loading the tasks
     */
    public void loadTaskList() throws CarrotException {
        this.tasks = storage.load();
    }
}
