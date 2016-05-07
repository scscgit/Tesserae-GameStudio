package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.score.ScoreException;

import java.util.List;

public interface GameServices
{
	void saveScore(String game, int points) throws ScoreException;

	List<FavoriteGameEntity> getFavorites() throws FavoriteException;
}
