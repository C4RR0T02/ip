package Carrot;

import Carrot.Task.Deadline;
import Carrot.Task.Event;
import Carrot.Task.Task;
import Carrot.Task.Todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws CarrotException {
        ArrayList<Task> taskList = new ArrayList<>();
        File file = new File(this.filePath);
        if (file.exists()) {
            read(taskList);
        }
        return taskList;
    }

    public void save(ArrayList<Task> taskList) {
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

    private void write(ArrayList<Task> taskList) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            for (Task t : taskList) {
                fileWriter.write(t.saveToString() + "\n");
            }
        } catch(IOException e){
                System.out.println("An error occurred.");
        }
    }

    private void read(ArrayList<Task> taskList) throws CarrotException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                String[] dataBlocks = data.split("\\|");
                String isComplete = dataBlocks[1];
                switch (dataBlocks[0]) {
                case "E":
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
                    Task newDeadline = new Deadline(dataBlocks[2], dataBlocks[3]);
                    checkAndSetTaskStatus(newDeadline, isComplete);
                    taskList.add(newDeadline);
                    break;
                }
            }
        } catch (IOException e) {
            throw new CarrotException("Failed to read saved tasks from file");
        }
    }

    private void checkAndSetTaskStatus(Task task, String taskStatus) {
        if (Objects.equals(taskStatus, "1")) {
            task.markCompleted();
        } else {
            task.markIncomplete();
        }
    }
}