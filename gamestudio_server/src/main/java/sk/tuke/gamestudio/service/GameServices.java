package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.service.score.ScoreException;

public interface GameServices {
    void saveScore(String game, int points) throws ScoreException;
}
