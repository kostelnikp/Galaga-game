package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game implements DrawableSimulable {
    private final double width;
    private final double height;
    private final Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Game.gif")));
    private List<DrawableSimulable> entities = new ArrayList<>();
    private final List<ShipBullet> shipBullets = new ArrayList<>();
    private final List<AlienBullet> alienBullets = new ArrayList<>();
    private final List<Alien> redAliens = new ArrayList<>();
    private boolean redAdd = false;
    private final List<Alien> yellowAliens = new ArrayList<>();
    private boolean yellowAdd = false;
    private final Ship ship;
    private boolean alienColor = true;
    private int score;
    private int shots;
    private int hits;
    private final Random random = new Random();
    private GameListener gameListener = new EmptyGameListener();
    private Instant lastFireTime = Instant.now();
    private boolean gameOver = false;


    public Game(double width, double height) {
        this.width = width;
        this.height = height;
        ship = new Ship(this, new Point2D(230, 500));
        createAlienGroup(9, width, 30, new Point2D(40, 0));
        createAlienGroup(7, width, 90, new Point2D(40, 0));
        createAlienGroup(4, width, 60, new Point2D(40, 0));
        createAlienGroup(3, width, 120, new Point2D(40, 0));
        entities.add(ship);
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, 0, 0, width, height);
        for (DrawableSimulable entity : entities) {
            entity.draw(gc);
        }

    }

    public void simulate(double deltaT) {
        if (gameOver) {
            return;
        } else {
            handleAlienFiring();
            for (DrawableSimulable entity1 : entities) {
                entity1.simulate(deltaT);
                if (entity1 instanceof Collisionable ce) {
                    for (DrawableSimulable entity2 : entities) {
                        if (entity1 != entity2) {
                            ce.checkCollision(entity2);
                        }
                    }
                }
            }
        }
    }

    private void createAlienGroup(int count, double gameWidth, double height, Point2D speed) {
        List<Alien> aliens = new ArrayList<>();
        double groupWidth = count * (double) 30;
        double startX = (gameWidth - groupWidth) / 2;
        for (int i = 0; i < count; i++) {
            aliens.add(createAlien(new Point2D(startX + i * (double) 40, height), speed));
            updateEntitiesList();
        }
        alienColor = !alienColor;
    }

    public void addRedAliensIfEmpty() {
        createAlienGroup(5, width, 30, new Point2D(40, 0));
        updateEntitiesList();
    }

    public void addYellowAliensIfEmpty() {
        createAlienGroup(4, width, 90, new Point2D(40, 0));
        updateEntitiesList();
    }

    private Alien createAlien(Point2D position, Point2D speed) {
        Alien alien = new Alien(this, position, speed, alienColor);
        if (alienColor) {
            redAliens.add(alien);
        } else {
            yellowAliens.add(alien);
        }
        return alien;
    }

    public void removeAlien(Alien alien) {
        if (alien.getColor()) {
            redAliens.remove(alien);
        } else {
            yellowAliens.remove(alien);
        }
        if (!redAdd && redAliens.isEmpty()) {
            addRedAliensIfEmpty();
            redAdd = true;
        }
        if (!yellowAdd && yellowAliens.isEmpty()) {
            addYellowAliensIfEmpty();
            yellowAdd = true;
        }
    }


    public void changeAlienGroupDirection(boolean alienColor) {
        if (alienColor) {
            for (Alien alien : redAliens) {
                Point2D currentSpeed = alien.getSpeed();
                alien.setSpeed(new Point2D(-currentSpeed.getX(), currentSpeed.getY()));
            }
        } else {
            for (Alien alien : yellowAliens) {
                Point2D currentSpeed = alien.getSpeed();
                alien.setSpeed(new Point2D(-currentSpeed.getX(), currentSpeed.getY()));
            }
        }

    }

    public void setShipPosition(double value) {
        ship.setPosition(value);
    }

    public synchronized void addBullet(Collisionable bullet) {
        if (bullet instanceof AlienBullet) {
            alienBullets.add((AlienBullet) bullet);
            updateEntitiesList();
            ((AlienBullet) bullet).setHitListener(new HitListener() {
                @Override
                public void hit() {
                    gameListener.stateHealth(getShip().getHealth());
                    gameListener.shotsHits(shots, hits);
                    if (getShip().getHealth() <= 0) {
                        gameOver = true;
                        gameListener.gameOver();
                    }

                }
            });
        } else if (bullet instanceof ShipBullet) {
            shipBullets.add((ShipBullet) bullet);
            updateEntitiesList();
            ((ShipBullet) bullet).setHitListener(new HitListener() {
                @Override
                public void hit() {
                    score = score + 20;
                    hits = hits + 1;
                    gameListener.stateChanged(score);
                    gameListener.shotsHits(shots, hits);
                    if (redAliens.isEmpty() && yellowAliens.isEmpty()) {
                        gameOver = true;
                        gameListener.gameOver();
                    }
                }
            });
        }


    }

    public void removeBullet(Collisionable bullet) {
        if (bullet instanceof ShipBullet) {
            shipBullets.remove((ShipBullet) bullet);
        }
        if (bullet instanceof AlienBullet) {
            alienBullets.remove((AlienBullet) bullet);
        }

        updateEntitiesList();
    }

    public void handleAlienFiring() {
        Instant currentTime = Instant.now();
        double timeSinceLastFire = (currentTime.toEpochMilli() - lastFireTime.toEpochMilli()) / 1000.0;
        double cooldownDuration = 3;

        if (timeSinceLastFire > cooldownDuration) {
            for (Alien alien : redAliens) {
                if (shouldAlienFire()) {
                    alien.fire();
                    lastFireTime = currentTime;
                }
            }

            for (Alien alien : yellowAliens) {
                if (shouldAlienFire()) {
                    alien.fire();
                    lastFireTime = currentTime;
                }
            }
        }
    }

    private boolean shouldAlienFire() {
        int probability = 1;
        return random.nextInt(500) < probability;
    }

    public void fire() {
        Instant currentTime = Instant.now();
        double timeSinceLastFire = (currentTime.toEpochMilli() - lastFireTime.toEpochMilli()) / 1000.0;
        double cooldownDuration = 0.3;
        if (timeSinceLastFire > cooldownDuration) {
            ship.fire();
            shots = shots + 1;
            lastFireTime = currentTime;
        }

    }


    private void updateEntitiesList() {
        if (!gameOver) {
            List<DrawableSimulable> updatedEntities = new ArrayList<>(alienBullets);
            updatedEntities.addAll(shipBullets);
            updatedEntities.add(ship);
            updatedEntities.addAll(redAliens);
            updatedEntities.addAll(yellowAliens);
            entities = new ArrayList<>(updatedEntities);
        } else {
            entities = List.of(ship);
        }
    }


    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Ship getShip() {
        return ship;
    }

    public Point2D getGamePoint(Point2D gamePoint) {
        return new Point2D(gamePoint.getX(), gamePoint.getY());
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }


}
