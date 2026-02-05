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
        return removable;
    }

    public void loadTaskList() throws CarrotException {
        this.taskList = storage.load();
    }
}
