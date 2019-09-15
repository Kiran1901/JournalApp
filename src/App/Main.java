package App;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFiles/Main.fxml"));
        primaryStage.setTitle("My Journal");
        primaryStage.setScene(new Scene(root, 720, 600));
        primaryStage.show();

        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> Platform.exit());
    }


   public static void main(String[] args) {
        launch(args);
    }
}
