package carrot;

import static carrot.Carrot.DEFAULT_FILEPATH;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
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

            // Load and apply the CSS with font resources
            String cssResource = Main.class.getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(cssResource);

            String fontFamily = resolveFontFamily();
            scene.getRoot().setStyle("-fx-font-family: '" + fontFamily + "';");

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

    private String resolveFontFamily() {
        String osName = System.getProperty("os.name").toLowerCase();
        String defaultFont = Font.getDefault().getFamily();
        String[] preferredFonts;

        if (osName.contains("mac")) {
            preferredFonts = new String[] { "Marker Felt", "Bradley Hand", defaultFont };
        } else if (osName.contains("win")) {
            preferredFonts = new String[] { "Segoe UI", "Segoe Print", "Lucida Handwriting", defaultFont };
        } else {
            preferredFonts = new String[] { "DejaVu Sans", "Liberation Sans", defaultFont };
        }

        for (String font : preferredFonts) {
            if (Font.getFamilies().contains(font)) {
                return font;
            }
        }

        return defaultFont;
    }
}
