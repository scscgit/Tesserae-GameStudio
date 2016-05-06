package sk.tuke.gamestudio.service;

public interface GameServices {
    void saveScore(String game, int points) throws ScoreException;
}
