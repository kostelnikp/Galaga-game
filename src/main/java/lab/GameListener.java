package lab;

public interface GameListener {

    void stateChanged(int score);

    void stateHealth(int health);

    void shotsHits(int shots, int hits);

    void gameOver();

}
