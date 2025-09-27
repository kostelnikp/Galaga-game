package lab;

public class Score {
    private final String name;
    private final int score;
    private final int hits;
    private final int shots;

    public Score(String name, int score, int hits, int shots) {
        this.name = name;
        this.score = score;
        this.hits = hits;
        this.shots = shots;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }


    public String toString() {
        return this.name + ";" + this.score + ";" + this.hits + ";" + this.shots;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    ;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Score s) {
            return s.getName().equals(this.name);
        }
        return false;
    }
}
