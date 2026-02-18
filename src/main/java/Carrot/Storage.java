package carrot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import carrot.task.Deadline;
import carrot.task.Event;
import carrot.task.Task;
import carrot.task.Todo;

/**
 * Handles loading and saving of tasks to a file
 */
public class Storage {

    private final String filePath;

    /**
     * Constructor for Storage class
     * @param filePath Path to the file where tasks are stored
     */
    public Storage(String filePath) {
        assert filePath != null : "filePath must not be null";
        this.filePath = filePath;
    }

    /**
     * Loads the task list from the file
     * @return Tasks loaded from the file
     * @throws CarrotException     if there is an error reading the file
     */
    public ArrayList<Task> load() throws CarrotException {
        ArrayList<Task> taskList = new ArrayList<>();
        File file = new File(this.filePath);
        if (file.exists()) {
            read(taskList);
        }
        return taskList;
    }

    /**
     * Saves the task list to the file
     * @param taskList List of tasks to be saved
     */
    public void save(ArrayList<Task> taskList) {
        assert taskList != null : "taskList must not be null";
        File file = new File(this.filePath);
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            write(taskList);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Writes the task list to the file
     * @param taskList List of tasks to be written
     */
    private void write(ArrayList<Task> taskList) {
        assert taskList != null : "taskList must not be null";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            for (Task t : taskList) {
                assert t != null : "task in taskList should not be null";
                fileWriter.write(t.saveToString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Reads the task list from the file
     * @param taskList List of tasks to be populated
     * @throws CarrotException if there is an error reading the file
     */
    private void read(ArrayList<Task> taskList) throws CarrotException {
        assert taskList != null : "taskList must not be null";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                String[] dataBlocks = data.split("\\|");
                assert dataBlocks.length >= 3 : "data blocks should have at least 3 elements";
                String isComplete = dataBlocks[1];
                switch (dataBlocks[0]) {
                case "E":
                    assert dataBlocks.length >= 5 : "event data should have at least 5 blocks";
                    Task newEvent = new Event(dataBlocks[2], dataBlocks[3], dataBlocks[4]);
                    checkAndSetTaskStatus(newEvent, isComplete);
                    taskList.add(newEvent);
                    break;
                case "T":
                    Task newTask = new Todo(dataBlocks[2]);
                    checkAndSetTaskStatus(newTask, isComplete);
                    taskList.add(newTask);
                    break;
                case "D":
                    assert dataBlocks.length >= 4 : "deadline data should have at least 4 blocks";
                    Task newDeadline = new Deadline(dataBlocks[2], dataBlocks[3]);
                    checkAndSetTaskStatus(newDeadline, isComplete);
                    taskList.add(newDeadline);
                    break;
                default:
                    throw new CarrotException("Corrupted data file");
                }
            }
        } catch (IOException e) {
            throw new CarrotException("Failed to read saved tasks from file");
        }
    }

    /**
     * Checks and sets the task status based on the saved data
     * @param task Task to be updated
     * @param taskStatus Status of the task in string format
     */
    private void checkAndSetTaskStatus(Task task, String taskStatus) {
        assert task != null : "task must not be null";
        assert taskStatus != null : "taskStatus must not be null";
        if (Objects.equals(taskStatus, "1")) {
            task.markCompleted();
        } else {
            task.markIncomplete();
        }
    }
}
