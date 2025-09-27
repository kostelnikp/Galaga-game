package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class AlienBullet extends GameEntity implements Collisionable {

    private final double size = 10;
    private final Point2D speed = new Point2D(0, 100);
    private final Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("AlienBullet.png")));
    private HitListener hitListener = new EmptyHitListener();

    public AlienBullet(Game game, Point2D position) {
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
        if (position.getY() > 700) {
            game.removeBullet(this);
        }
    }


    @Override
    public void hit(Collisionable that) {
        if (that instanceof Ship) {
            hitListener.hit();
            game.removeBullet(this);
        }

    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size / 2, size / 2);
    }

    public void setHitListener(HitListener hitListener) {
        this.hitListener = hitListener;
    }
}
