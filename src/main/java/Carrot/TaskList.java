package Carrot;

import Carrot.Task.Task;

import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;
    private final Storage storage;

    public TaskList(Storage storage) {
        this.storage = storage;
        this.tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task newTask) {
        this.tasks.add(newTask);
        storage.save(this.tasks);
    }

    public Task deleteTask(int index) {
        Task removable = this.tasks.get(index);
        this.tasks.remove(removable);
        storage.save(this.tasks);
        return removable;
    }

    public void loadTaskList() throws CarrotException {
        this.tasks = storage.load();
    }
}
