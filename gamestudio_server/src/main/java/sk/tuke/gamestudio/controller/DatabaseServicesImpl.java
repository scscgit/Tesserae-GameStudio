package sk.tuke.gamestudio.controller;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Delegating the methods to all database implementations.
 * Unlike database services themselves, this is a directly usable Named component.
 * <p>
 * Created by Steve on 13.05.2016.
 */
@Named ("databaseServices")
@RequestScoped
public class DatabaseServicesImpl
{
	@Inject
	private FavoriteGameDatabaseService favorites;

	@Inject
	private ScoreService scores;

	public void addFavorite(String player, String game) throws FavoriteException
	{
		favorites.addFavorite(player, game);
	}

	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		favorites.addFavorite(favorite);
	}

	public void removeFavorite(String player, String game) throws FavoriteException
	{
		favorites.removeFavorite(player, game);
	}

	public List<FavoriteGameEntity> getFavoriteGames(String player) throws FavoriteException
	{
		return favorites.getFavoriteGames(player);
	}

	public List<FavoriteGameEntity> getFavoriteGamePlayers(String game) throws FavoriteException
	{
		return favorites.getFavoriteGamePlayers(game);
	}

	public void addScore(Score score) throws ScoreException
	{
		scores.addScore(score);
	}

	public List<Score> getBestScoresForGame(String game) throws ScoreException
	{
		return scores.getBestScoresForGame(game);
	}

	public List<Score> getAllScoresForGame(String game) throws ScoreException
	{
		return scores.getAllScoresForGame(game);
	}
}
