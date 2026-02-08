package Carrot;

import Carrot.Task.Task;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskList;
    private final Storage storage;

    public TaskList(Storage storage) {
        this.storage = storage;
        this.taskList = new ArrayList<>();
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    public void addTask(Task newTask) {
        this.taskList.add(newTask);
        storage.save(this.taskList);
    }

    public Task deleteTask(int index) {
        Task removable = this.taskList.get(index);
        this.taskList.remove(removable);
        storage.save(this.taskList);
        return removable;
    }

    public void loadTaskList() throws CarrotException {
        this.taskList = storage.load();
    }

    /**
     * Finds tasks that contain the given keyword and adds them to the provided array list.
     *
     * @param keyword   The keyword to search for within task names.
     * @param arrayList The array list to store found tasks.
     */
    public void findTasks(String keyword, ArrayList<Task> arrayList) {
        for (Task task : this.taskList) {
            if (task.getTaskName().contains(keyword)) {
                arrayList.add(task);
            }
        }
    }
}
