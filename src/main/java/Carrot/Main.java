package carrot;

import static carrot.Carrot.DEFAULT_FILEPATH;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Carrot using FXML.
 */
public class Main extends Application {

    private final Carrot carrot = new Carrot(DEFAULT_FILEPATH);

    @Override
    public void start(Stage stage) {
        assert stage != null : "Stage should not be null";
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            assert ap != null : "Root AnchorPane should be loaded from FXML";

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Carrot Chatbot");
            stage.setMinHeight(400);
            stage.setMinWidth(500);

            MainWindow controller = fxmlLoader.getController();
            assert controller != null : "MainWindow controller should be loaded";
            controller.setCarrot(carrot);

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
        }
    }

}
