package lab;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class EndGameController {

    @FXML
    private Canvas canvas;
    @FXML
    private Label shotsFired;
    @FXML
    private Label numberOfHits;
    @FXML
    private Label hitMissRatio;
    @FXML
    private Button saveScore;
    private AnimationTimer animationTimer;
    private int score;
    private int shots;
    private int hits;
    private String playerName;


    public EndGameController() {
    }

    public void startGame(int shots, int hits, int score, String playerName) {
        EndGame endGame = new EndGame(canvas.getWidth(), canvas.getHeight());

        animationTimer = new DrawingThread(canvas, endGame);
        this.shotsFired.setText("" + shots);
        this.numberOfHits.setText("" + hits);
        double ratio = ((double) hits / shots) * 100;
        BigDecimal ratioRound = BigDecimal.valueOf(0);
        if (shots != 0 && hits != 0) {
            ratioRound = new BigDecimal(ratio).setScale(2, RoundingMode.HALF_UP);
        }
        this.shots = shots;
        this.hits = hits;
        this.score = score;
        this.playerName = playerName;
        this.hitMissRatio.setText(ratioRound + "%");
        animationTimer.start();
    }

    public void stopGame() {
        animationTimer.stop();
    }


    @FXML
    public void MainMenuPressed() {
        stopGame();
        openMainMenu();
    }

    @FXML
    public void SaveScorePressed() {
        this.saveScore.setDisable(true);
        Score newScore = new Score(playerName, score, hits, shots);

        try (FileWriter fw = new FileWriter("data.csv", true)) {
            fw.write(newScore.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openMainMenu() {
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
