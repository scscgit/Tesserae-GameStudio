package sk.tuke.gamestudio.controller;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.entity.Score;
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
	public List<FavoriteGameEntity> getFavorites() throws FavoriteException
	{
		return favoriteGameService.getFavorites(userController.getLoggedUser().getName());
	}
}
