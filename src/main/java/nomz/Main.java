package nomz;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nomz.ui.MainWindow;

/**
 * A GUI for nomz using FXML.
 */
public class Main extends Application {

    private final Nomz nomz = new Nomz("./data/tasks.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("chat with nomz!");
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setNomz(nomz); // inject the Nomz instance
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }
}
