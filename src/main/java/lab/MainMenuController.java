package lab;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Canvas canvas;
    private AnimationTimer animationTimer;

    @FXML
    private TextField PlayerName;

    private String StringPlayerName;


    public void startGame() {
        MainMenu mainMenu = new MainMenu(canvas.getWidth(), canvas.getHeight());

        animationTimer = new DrawingThread(canvas, mainMenu);
        animationTimer.start();
    }

    public void stopGame() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    @FXML
    public void ExitGamePressed() {
        stopGame();
        System.exit(0);
    }

    @FXML
    public void StartGamePressed() {
        StringPlayerName = PlayerName.getText();
        stopGame();
        openGameScreen();
    }

    @FXML
    public void HighScoresPressed() {
        stopGame();
        openScoresScreen();
    }

    private void openGameScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            Stage primaryStage = (Stage) canvas.getScene().getWindow();
            primaryStage.setScene(scene);

            GameScreenController gameScreenController = loader.getController();
            gameScreenController.startGame(StringPlayerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openScoresScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HighScores.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            Stage primaryStage = (Stage) canvas.getScene().getWindow();
            primaryStage.setScene(scene);

            HighScoresController highScoresController = loader.getController();
            highScoresController.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
