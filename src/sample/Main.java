package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("VocabFast");
        stage.setScene(new Scene(root));
        Image appIcon = new Image("sample/logo1.png");
        stage.getIcons().add(appIcon);


        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
