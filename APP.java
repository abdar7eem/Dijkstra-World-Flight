import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class APP extends Application {

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root;
        try {
            // Show FXML screen
            root = FXMLLoader.load(getClass().getResource("Screen.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setTitle("Air Traffic"); 
            primaryStage.getIcons().addAll(new Image("img\\logo2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}