package lab;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HighScoresController {
    @FXML
    private Canvas canvas;

    @FXML
    private ListView<Score> PlayerScore;
    private final ObservableList<Score> scores;
    private AnimationTimer animationTimer;

    public HighScoresController() {
        scores = FXCollections.observableList(new ArrayList<Score>());
    }

    @FXML
    public void initialize() {
        loadScore();
    }

    public void startGame() {
        MainMenu mainMenu = new MainMenu(canvas.getWidth(), canvas.getHeight());
        PlayerScore.setItems(scores);

        animationTimer = new DrawingThread(canvas, mainMenu);
        animationTimer.start();
    }

    public void stopGame() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    @FXML
    public void MainMenuPressed() {
        stopGame();
        openMenuScreen();
    }

    public void loadScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while (null != (line = br.readLine())) {
                if (!line.isBlank()) {
                    String[] token = line.split(";");
                    scores.add(new Score(token[0], Integer.parseInt(token[1]), Integer.parseInt(token[2]), Integer.parseInt(token[3])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMenuScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            Stage primaryStage = (Stage) canvas.getScene().getWindow();
            primaryStage.setScene(scene);

            MainMenuController mainMenuController = loader.getController();
            mainMenuController.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
