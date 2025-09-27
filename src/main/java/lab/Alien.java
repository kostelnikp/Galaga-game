package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;


public class Alien extends GameEntity implements Collisionable {

    private Point2D speed;
    private final double size = 30;

    private final Image redAlien = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("RedAlien.gif")));
    private final Image yellowAlien = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("YellowAlien.gif")));
    private final Image alienImage;


    public Alien(Game game, Point2D position, Point2D speed, boolean isRed) {
        super(game, position);
        this.speed = speed;
        this.alienImage = isRed ? redAlien : yellowAlien;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        Point2D gamePosition = game.getGamePoint(position);
        gc.drawImage(alienImage, gamePosition.getX(), gamePosition.getY(), size, size);
        gc.restore();
    }

    public void simulate(double deltaT) {
        if (game.isGameOver()) {
            return;
        }
        position = position.add(speed.multiply(deltaT));
        if (getPosition().getX() < 0 || getPosition().getX() > game.getWidth() - size) {
            game.changeAlienGroupDirection(getColor());
        }
    }

    public void hit(Collisionable that) {
        if (that instanceof ShipBullet) {
            game.removeAlien(this);
        }
    }

    public void fire() {
        Point2D bulletStartPosition = new Point2D(position.getX() + size / 3, position.getY());
        AlienBullet bullet = new AlienBullet(game, bulletStartPosition);
        game.addBullet(bullet);
    }

    public Point2D getPosition() {
        return position;
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size, size);
    }

    public double getSize() {
        return size;
    }

    public boolean getColor() {
        return this.alienImage == redAlien;
    }

    public Point2D getSpeed() {
        return speed;
    }

    public void setSpeed(Point2D speed) {
        this.speed = speed;
    }
}
