package lab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class MainMenu implements DrawableSimulable {
    private final double width;
    private final double height;

    private final Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("MainImage.gif")));


    public MainMenu(double width, double height) {
        this.width = width;
        this.height = height;
    }


    public void draw(GraphicsContext gc) {
        gc.drawImage(image, 0, 0, width, height);
    }

    public void simulate(double deltaT) {
    }


    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


}
