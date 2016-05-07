package sk.tuke.gamestudio.controller;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.service.GameServices;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Named ("gameServices")
@RequestScoped
public class GameServicesImpl implements GameServices
{
	@Inject
	private UserController userController;

	@EJB
	private ScoreService scoreService;

	@EJB
	private FavoriteGameDatabaseService favoriteGameService;

	@Override
	public void saveScore(String game, int points) throws ScoreException
	{
		if (userController.getLoggedUser().isLogged())
		{
			scoreService.addScore(new Score(userController.getLoggedUser().getName(), game, points, new Date()));
		}
	}

	@Override
	public List<Score> getBestScores(String game) throws ScoreException
	{
		if (userController.getLoggedUser().isLogged())
		{
			return scoreService.getBestScoresForGame(game);
		}
		else
		{
			return null;
		}
	}

	@Override
	public List<Score> getMyAllScores() throws ScoreException
	{
		try
		{
			if (userController.getLoggedUser().isLogged())
			{
				final String currentPlayer = userController.getLoggedUser().getName();
				LinkedList<Score> myAllScores = new LinkedList<>();
				for (Game game : Game.allGames())
				{
					//Downloads all scores for a single game
					String gameName = game.getName();
					List<Score> allScoresForGame = scoreService.getAllScoresForGame(gameName);

					//Removes all scores of other players
					//Lambda one-liner version:
					//allScoresForGame.removeIf((score) -> !score.getPlayer().equals(currentPlayer));

					for (Iterator<Score> iterator = allScoresForGame.iterator(); iterator.hasNext(); )
					{
						Score score = iterator.next();
						if (!score.getPlayer().equals(currentPlayer))
						{
							iterator.remove();
						}
					}

					//Adds the scores to the list of the current player
					myAllScores.addAll(allScoresForGame);
				}
				return myAllScores;
			}
		}
		//This function is called on every page of a logged user,
		//we will encapsulate errors into returned null value.
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<FavoriteGameEntity> getFavorites() throws FavoriteException
	{
		if (userController.getLoggedUser().isLogged())
		{
			return favoriteGameService.getFavorites(userController.getLoggedUser().getName());
		}
		else
		{
			return null;
		}
	}
}
