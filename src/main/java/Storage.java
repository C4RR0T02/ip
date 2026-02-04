import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {
    private String filePath;
    private static final Pattern TASK_PATTERN = Pattern.compile("\\(([^)]+)\\)$");

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void load(ArrayList<Task> taskList) {
        File file = new File(this.filePath);
        if (file.exists()) {
            read(taskList);
        }
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

    private String format(Task currentTask) {
        String taskString = currentTask.toString();
        Matcher matcher = TASK_PATTERN.matcher(taskString);

        String line = "";
        String timeInfo;
        char taskType = taskString.charAt(1);
        String status = (taskString.charAt(4) == 'X') ? "1" : "0";

        if (matcher.find() && taskType != 'T') {
            if (taskType == 'D') {
                timeInfo = matcher.group(1).replace("by: ", "");
                line = taskType + "|" + status + "|" + currentTask.taskName + "|" + timeInfo.trim() + "\n";
            } else if (taskType == 'E') {
                timeInfo = matcher.group(1).replace("from: ", "");
                String[] timeInfos = timeInfo.split("to: ");
                line = taskType + "|" + status + "|" + currentTask.taskName + "|" + timeInfos[0].trim() + "|" + timeInfos[1].trim() + "\n";
            }
        } else {
            line = taskType + "|" + status + "|" + currentTask.taskName + "\n";
        }
        return line;
    }

    private void write(ArrayList<Task> taskList) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            for (Task t : taskList) {
                fileWriter.write(format(t));
            }
        } catch(IOException e){
                System.out.println("An error occurred.");
        }
    }

    private void read(ArrayList<Task> taskList) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                String[] dataBlocks = data.split("\\|");
                String isComplete = dataBlocks[1];
                switch (dataBlocks[0]) {
                case "E":
                    Task newEvent = new Event(dataBlocks[2], dataBlocks[3], dataBlocks[4], false);
                    checkAndSetTaskStatus(newEvent, isComplete);
                    taskList.add(newEvent);
                    break;
                case "T":
                    Task newTask = new Todo(dataBlocks[2], false);
                    checkAndSetTaskStatus(newTask, isComplete);
                    taskList.add(newTask);
                    break;
                case "D":
                    Task newDeadline = new Deadline(dataBlocks[2], dataBlocks[3], false);
                    checkAndSetTaskStatus(newDeadline, isComplete);
                    taskList.add(newDeadline);
                    break;
                }
            }
        } catch(IOException e){
            System.out.println("An error occurred trying to read the saved tasks.");
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