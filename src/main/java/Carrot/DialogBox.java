package carrot;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the profile picture
 * and a label containing input from the user or response form Carrot.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img, boolean isUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
        if (text.toLowerCase().contains("error") || text.toLowerCase().contains("failed")
            || text.toLowerCase().contains("invalid") || text.toLowerCase().contains("cannot")
            || text.toLowerCase().contains("missing") || text.toLowerCase().contains("wrong")) {
            dialog.getStyleClass().add("error-label");
        } else if (isUser) {
            dialog.getStyleClass().add("user-label");
        } else {
            dialog.getStyleClass().add("bot-label");
        }

        Circle clip = new Circle(displayPicture.getFitWidth() / 2, displayPicture.getFitHeight() / 2,
                                 displayPicture.getFitWidth() / 2);
        displayPicture.setClip(clip);

        this.getStylesheets().add(getClass().getResource("/css/dialog-box.css").toExternalForm());
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a dialog box for the user.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img, true);
    }

    /**
     * Creates a dialog box for Carrot.
     */
    public static DialogBox getCarrotDialog(String text, Image img) {
        var db = new DialogBox(text, img, false);
        db.flip();
        return db;
    }
}
