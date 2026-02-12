package carrot;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Carrot carrot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Pizza.png"));
    private Image carrotImage = new Image(this.getClass().getResourceAsStream("/images/Carrot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Carrot instance */
    public void setCarrot(Carrot carrot) {
        this.carrot = carrot;

        try {
            Image asciiArtImage = new Image(this.getClass().getResourceAsStream("/images/carrot-ascii-art.png"));
            ImageView asciiArtView = new ImageView(asciiArtImage);
            asciiArtView.setPreserveRatio(true);
            asciiArtView.setFitWidth(400);
            HBox imageContainer = new HBox(asciiArtView);
            imageContainer.setAlignment(Pos.CENTER);
            dialogContainer.getChildren().add(imageContainer);
        } catch (Exception e) {
            System.err.println("Could not load ASCII art image: " + e.getMessage());
        }

        dialogContainer.getChildren().add(
                DialogBox.getCarrotDialog(carrot.getUi().showWelcome(), carrotImage)
        );
    }


    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.trim().isEmpty()) {
            userInput.clear();
            return;
        }

        Response response = carrot.getParser()
                .command(carrot.getUi(),
                input,
                carrot.getTaskList(),
                carrot.getStorage());

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCarrotDialog(response.getMessage(), carrotImage)
        );
        userInput.clear();

        if (response.getCommandType() == Response.CommandType.BYE) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
