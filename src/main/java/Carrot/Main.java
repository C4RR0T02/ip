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
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Carrot Chatbot");

            stage.setMinHeight(400);
            stage.setMinWidth(500);

            fxmlLoader.<MainWindow>getController().setCarrot(carrot);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
