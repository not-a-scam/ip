package nomz.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import nomz.Nomz;
import nomz.commands.Command;
import nomz.data.exception.NomzException;
import nomz.parser.Parser;

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

    private Nomz nomz;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image nomzImage = new Image(this.getClass().getResourceAsStream("/images/nomz.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Nomz instance */
    public void setNomz(Nomz n) {
        nomz = n;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Nomz's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        Command command = null;
        String response = "";

        try {
            command = Parser.parse(input);
            response = nomz.getResponse(command);
        } catch (NomzException e) {
            response = e.getMessage();
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNomzDialog(response, nomzImage)
        );

        if (command != null && command.isExit()) {
            Platform.exit();
        }
        userInput.clear();
    }
}
