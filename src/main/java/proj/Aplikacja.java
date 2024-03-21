package proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class Aplikacja extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/elasticDeformationDisplay.fxml"));
            BorderPane root = loader.load();
            stage.setTitle("Elastic Deformation Differential Equation Solver by Jakub Worek");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/elasticDeformationDisplay.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
