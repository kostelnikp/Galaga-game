package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingThread extends AnimationTimer {

    private final GraphicsContext gc;
    private final DrawableSimulable drawable;
    private double lastTime;

    public DrawingThread(Canvas canvas, DrawableSimulable drawable) {
        this.gc = canvas.getGraphicsContext2D();
        this.drawable = drawable;
    }

    @Override
    public void handle(long now) {
        if (lastTime > 0) {
            double deltaT = (now - lastTime) / 1e9;
            this.drawable.simulate(deltaT);
        }
        this.drawable.draw(gc);
        lastTime = now;
    }
}
