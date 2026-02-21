package carrot;

import java.util.Scanner;

/**
 * Main class for the Carrot application
 */
public class Carrot {

    public static final String DEFAULT_FILEPATH = "data/Carrot.txt";
    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;
    private final Ui ui;

    /**
     * Constructor for Carrot class
     * @param filePath Path to the file where tasks are stored
     */
    public Carrot(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        String effectiveFilePath = (!filePath.isEmpty()) ? filePath : DEFAULT_FILEPATH;
        this.storage = new Storage(effectiveFilePath);
        this.taskList = new TaskList(storage);

        try {
            taskList.loadTaskList();
        } catch (CarrotException e) {
            ui.showLoadingError();
        }
    }

    /**
     * Gets the TaskList object
     * @return TaskList object
     */
    public TaskList getTaskList() {
        return this.taskList;
    }

    /**
     * Gets the Ui object
     * @return Ui object
     */
    public Ui getUi() {
        return this.ui;
    }

    /**
     * Gets the Parser object
     * @return Parser object
     */
    public Parser getParser() {
        return this.parser;
    }

    /**
     * Gets the Storage object
     * @return Storage object
     */
    public Storage getStorage() {
        return this.storage;
    }

    /**
     * Initializes the Carrot application with Scanner and displays welcome message
     * @param scanner The scanner for reading user input
     */
    private void initialize(Scanner scanner) {
        assert scanner != null : "scanner must not be null";
        ui.showWelcome();
        startCommandLoop(scanner);
    }

    /**
     * Starts the main command loop for processing user input
     * @param scanner The scanner for reading user input
     */
    private void startCommandLoop(Scanner scanner) {
        assert scanner != null : "scanner must not be null";
        try {
            while (!ui.isExit()) {
                String userInput = scanner.nextLine();
                parser.command(ui, userInput, taskList, storage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ui.exit();
        }
    }

    /**
     * Main method to create a Carrot object and start the command loop
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String path = (args.length > 0) ? args[0] : "";
        Carrot carrot = new Carrot(path);
        try (Scanner input = new Scanner(System.in)) {
            carrot.initialize(input);
        }
    }
}
