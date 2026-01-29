import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {
    private String filePath;
    private static final Pattern TASK_PATTERN = Pattern.compile("\\(([^)]+)\\)$");

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void save(ArrayList<Task> taskList) {
        System.out.println("inside Save");
        File file = new File(this.filePath);
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            write(taskList);
            System.out.println("Write to " + file.getAbsolutePath());
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
}