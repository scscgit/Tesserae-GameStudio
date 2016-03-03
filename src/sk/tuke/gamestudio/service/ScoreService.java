package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.util.List;

/**
 * Created by jaros_000 on 3.3.2016.
 */
public interface ScoreService {
    void addScore(Score score) throws ScoreException;

    List<Score> getBestScoresForGame(String game) throws ScoreException;
}
