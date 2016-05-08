package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.List;

public interface GameServices
{
	void saveScore(String game, int points) throws ScoreException;
	public List<Score> getBestScores(String game) throws ScoreException;
	List<Score> getMyAllScores() throws ScoreException;

	List<FavoriteGameEntity> getFavorites() throws FavoriteException;
}
