package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class ShipBullet extends GameEntity implements Collisionable {
    private final Point2D speed = new Point2D(0, -700);
    private final double size = 10;
    private final Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("ShipBullet.png")));
    private HitListener hitListener = new EmptyHitListener();

    public ShipBullet(Game game, Point2D position) {
        super(game, position);
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        Point2D gamePosition = game.getGamePoint(position);
        gc.drawImage(image, gamePosition.getX(), gamePosition.getY(), size, size);
        gc.restore();
    }

    public void simulate(double deltaT) {
        position = position.add(speed.multiply(deltaT));
        if (position.getY() < 0) {
            game.removeBullet(this);
        }
    }


    @Override
    public void hit(Collisionable that) {
        if (that instanceof Alien) {
            hitListener.hit();
            game.removeBullet(this);
        }
    }

    public void setHitListener(HitListener hitListener) {
        this.hitListener = hitListener;
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size, size);
    }
}
