package lab;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class GameScreenController {

    @FXML
    private Canvas canvas;

    @FXML
    private Slider slider;

    @FXML
    private Label score;

    @FXML
    private Label highScore;

    @FXML
    private ImageView health1;
    @FXML
    private ImageView health2;
    @FXML
    private ImageView health3;

    private Game game;

    private AnimationTimer animationTimer;

    private final GameListenerImpl gameListener = new GameListenerImpl();

    private int shots;
    private int hits;
    private int scorePlayer;

    private String playerName;


    @FXML
    public void initialize() {
        startGame(playerName);
        loadHighScore();
        canvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                ShipFire(newScene);

            }
        });
    }

    public void startGame(String playerName) {
        this.game = new Game(canvas.getWidth(), canvas.getHeight());
        this.playerName = playerName;

        slider.valueProperty().addListener((o, oldValue, newValue) -> this.game.setShipPosition(newValue.doubleValue()));

        animationTimer = new DrawingThread(canvas, game);
        animationTimer.start();

        game.setGameListener(gameListener);
    }

    public void stopGame() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }


    public void ShipFire(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                game.fire();
            }
        });
    }

    private void loadHighScore() {
        int maxScore = Integer.MIN_VALUE;

        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while (null != (line = br.readLine())) {
                if (!line.isBlank()) {
                    String[] token = line.split(";");

                    int score = Integer.parseInt(token[1]);
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        highScore.setText(String.valueOf(maxScore));
    }


    private void openEndScreen() {
        stopGame();

        Scene currentScene = canvas.getScene();

        if (currentScene != null && currentScene.getWindow() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
                BorderPane root = loader.load();

                Scene scene = new Scene(root);

                Stage primaryStage = (Stage) currentScene.getWindow();
                primaryStage.setScene(scene);

                EndGameController endGameController = loader.getController();
                endGameController.startGame(shots, hits, scorePlayer, playerName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class GameListenerImpl implements GameListener {


        public GameListenerImpl() {
        }

        @Override
        public void stateChanged(int score) {
            GameScreenController.this.score.setText("" + score);
            GameScreenController.this.scorePlayer = score;
        }

        @Override
        public void stateHealth(int health) {
            if (health <= 0) {
                health1.setVisible(false);
            } else if (health == 1) {
                health2.setVisible(false);
            } else {
                health3.setVisible(false);
            }
        }

        @Override
        public void shotsHits(int shots, int hits) {
            GameScreenController.this.shots = shots;
            GameScreenController.this.hits = hits;
        }

        @Override
        public void gameOver() {
            stopGame();
            openEndScreen();
        }
    }
}
