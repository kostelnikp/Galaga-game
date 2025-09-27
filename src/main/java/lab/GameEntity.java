package lab;

import javafx.geometry.Point2D;

public abstract class GameEntity implements DrawableSimulable {
    protected final Game game;
    protected Point2D position;


    public GameEntity(Game game, Point2D position) {
        this.game = game;
        this.position = position;
    }

}
