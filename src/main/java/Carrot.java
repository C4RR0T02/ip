import java.util.Scanner;

public class Carrot {

    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;
    private final Ui ui;

    public Carrot(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        this.taskList = new TaskList(storage);
        try {
            taskList.loadTaskList();
        } catch (CarrotException e) {
            ui.showLoadingError();
        }
    }

    public static void main(String[] args) {
        Carrot carrot = new Carrot("data/taskList.txt");
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
