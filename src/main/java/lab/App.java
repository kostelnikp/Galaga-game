package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private MainMenuController mainMenuController;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/MainMenu.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.resizableProperty().set(false);
            primaryStage.setTitle("Galaga");
            primaryStage.show();

            mainMenuController = loader.getController();
            mainMenuController.startGame();

            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}