package carrot;

import java.util.ArrayList;

import carrot.task.Task;

/**
 * Manages a list of tasks, including adding, deleting, loading, and finding tasks.
 */
public class TaskList {

    private ArrayList<Task> tasks;
    private final Storage storage;

    /**
     * Constructor for TaskList class
     * @param storage Storage object for saving and loading tasks
     */
    public TaskList(Storage storage) {
        assert storage != null : "storage must not be null";
        this.storage = storage;
        this.tasks = new ArrayList<>();
    }

    /**
     * Gets the current list of tasks
     * @return ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        assert this.tasks != null : "tasks should not be null";
        return this.tasks;
    }

    /**
     * Adds a new task to the task list and saves it
     * @param newTask Task to be added
     */
    public void addTask(Task newTask) {
        assert newTask != null : "newTask must not be null";
        this.tasks.add(newTask);
        storage.save(this.tasks);
    }

    /**
     * Deletes a task from the task list by index and saves the updated list
     * @param index Index of the task to be deleted
     * @return The removed Task
     */
    public Task deleteTask(int index) {
        assert index >= 0 && index < this.tasks.size() : "index must be within bounds of task list";
        Task removable = this.tasks.get(index);
        assert removable != null : "task at index should not be null";
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
        assert this.tasks != null : "tasks should be properly loaded from storage";
    }

    /**
     * Finds tasks that contain the given keyword and adds them to the provided array list.
     *
     * @param keyword   The keyword to search for within task names.
     * @param arrayList The array list to store found tasks.
     */
    public void findTasks(String keyword, ArrayList<Task> arrayList) {
        assert keyword != null : "keyword must not be null";
        assert arrayList != null : "arrayList must not be null";
        assert this.tasks != null : "tasks should be initialized";
        for (Task task : this.getTasks()) {
            assert task != null : "task in tasks list should not be null";
            if (task.getTaskName().contains(keyword)) {
                arrayList.add(task);
            }
        }
    }

    /**
     * Updates a task at the specified index with a new task and saves the changes.
     *
     * @param index   The index of the task to be updated.
     * @param newTask The new task to replace the old one.
     */
    public void updateTask(int index, Task newTask) {
        assert index >= 0 && index < this.tasks.size() : "index must be within bounds of task list";
        assert newTask != null : "newTask must not be null";
        this.tasks.set(index, newTask);
        storage.save(this.tasks);
    }
}
