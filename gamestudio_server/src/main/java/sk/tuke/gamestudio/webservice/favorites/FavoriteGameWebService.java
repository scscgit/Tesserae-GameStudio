package sk.tuke.gamestudio.webservice.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

/**
 * Enterprise JavaBeans implementation (dispatch) of the FavoriteGameService.
 * <p>
 * Created by Steve on 15.04.2016.
 */
@WebService
	(
		serviceName = "FavoriteGameService",
		portName = "FavoriteGameServicePort"
	)
public class FavoriteGameWebService
{
	@EJB
	private FavoriteGameDatabaseService favoriteGameDatabaseService;

	public void addFavorite2(String player, String game) throws FavoriteException
	{
		favoriteGameDatabaseService.addFavorite(player, game);
	}
	public void addFavorite1(FavoriteGameEntity favorite) throws FavoriteException
	{
		favoriteGameDatabaseService.addFavorite(favorite);
	}
	public void removeFavorite(String player, String game) throws FavoriteException
	{
		favoriteGameDatabaseService.removeFavorite(player, game);
	}
	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		return favoriteGameDatabaseService.getFavorites(player);
	}
}
