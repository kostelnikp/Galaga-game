package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Ship extends GameEntity implements Collisionable {

    private Point2D position;
    private final double size = 35;
    private final Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Ship.png")));

    private int health = 3;

    public Ship(Game game, Point2D position) {
        super(game, position);
        this.position = position;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.drawImage(image, position.getX(), position.getY(), size, size);
        gc.restore();
    }

    public void simulate(double deltaT) {
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size / 2, size / 2);
    }

    public void hit(Collisionable that) {
        if (that instanceof AlienBullet) {
            health = health - 1;
        }
    }

    public void fire() {
        Point2D bulletStartPosition = new Point2D(position.getX() + size / 3, position.getY());
        ShipBullet bullet = new ShipBullet(game, bulletStartPosition);
        game.addBullet(bullet);
    }

    public double getPositionY() {
        return position.getY();
    }

    public double getPositionX() {
        return position.getX();
    }

    public void setPosition(double value) {
        this.position = new Point2D(value, this.getPositionY());
    }

    public int getHealth() {
        return health;
    }


}
