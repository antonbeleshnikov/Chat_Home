package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Home Chat");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    /*
    Фанзиль, добрый день! К сожалению, никак не успеваю сдать 2-ое д/з вовремя, вы вроде говорили, что можно сдать пустой пул-реквест и потом в нем же написать д/з, сделаю так.
     */

    public static void main(String[] args) {
        launch(args);
    }
}
