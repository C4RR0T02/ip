package carrot;

import java.util.Scanner;

/**
 * Main class for the Carrot application
 */
public class Carrot {

    private static final String DEFAULT_FILEPATH = "data/carrot.txt";
    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;
    private final Ui ui;

    /**
     * Constructor for Carrot class
     * @param filePath Path to the file where tasks are stored
     */
    private Carrot(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        if (!filePath.isEmpty()) {
            this.storage = new Storage(filePath);
        } else {
            this.storage = new Storage(DEFAULT_FILEPATH);
        }
        this.taskList = new TaskList(storage);
        try {
            taskList.loadTaskList();
        } catch (CarrotException e) {
            ui.showLoadingError();
        }
    }

    /**
     * Main method to run the Carrot application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String path = (args.length > 0) ? args[0] : "";
        Carrot carrot = new Carrot(path);
        Scanner input = new Scanner(System.in);
        carrot.ui.showWelcome();
        try {
            while (!carrot.ui.isExit()) {
                carrot.parser.command(carrot.ui, input, carrot.taskList, carrot.storage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            carrot.ui.exit();
        }
    }
}
