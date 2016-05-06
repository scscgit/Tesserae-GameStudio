package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;
import sk.tuke.gamestudio.webservice.favorites.FavoriteGameService;
import sk.tuke.gamestudio.webservice.favorites.FavoriteGameWebService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 15.04.2016.
 */
public class FavoriteGameWebServiceClient implements FavoriteGameDatabaseService
{
	private FavoriteGameWebService favoriteGameService = new FavoriteGameService().getFavoriteGameServicePort();

	@Override
	public void addFavorite(String player, Game game) throws FavoriteException
	{
		try
		{
			sk.tuke.gamestudio.webservice.favorites.Game gameWeb = new sk.tuke.gamestudio.webservice.favorites.Game();
			gameWeb.setName(game.toString());
			favoriteGameService.addFavorite2(player, gameWeb);
		}
		catch (Exception e)
		{
			throw new FavoriteException(e);
		}
	}
	@Override
	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		try
		{
			sk.tuke.gamestudio.webservice.favorites.FavoriteGameEntity favoriteWeb =
				new sk.tuke.gamestudio.webservice.favorites.FavoriteGameEntity();
			favoriteWeb.setId(favorite.getId());
			sk.tuke.gamestudio.webservice.favorites.Game gameWeb =
				new sk.tuke.gamestudio.webservice.favorites.Game();
			gameWeb.setName(favorite.getGame().toString());
			favoriteWeb.setGame(gameWeb);
			favoriteWeb.setPlayer(favorite.getPlayer());
			favoriteWeb.setChosenOn(Utility.dateToXmlGregorianCalendar(favorite.getChosenOn()));
			favoriteGameService.addFavorite1(favoriteWeb);
		}
		catch (Exception e)
		{
			throw new FavoriteException(e);
		}
	}
	@Override
	public void removeFavorite(String player, Game game) throws FavoriteException
	{
		try
		{
			sk.tuke.gamestudio.webservice.favorites.Game gameWeb = new sk.tuke.gamestudio.webservice.favorites.Game();
			gameWeb.setName(game.toString());
			favoriteGameService.removeFavorite(player, gameWeb);
		}
		catch (Exception e)
		{
			throw new FavoriteException(e);
		}
	}
	@Override
	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		List<FavoriteGameEntity> favoriteGameEntities = new ArrayList<>();
		try
		{
			for (sk.tuke.gamestudio.webservice.favorites.FavoriteGameEntity favoriteEntityWeb :
				favoriteGameService.getFavorites(player))
			{
				FavoriteGameEntity favoriteGameEntity = new FavoriteGameEntity();
				favoriteGameEntity.setId(favoriteEntityWeb.getId());
				//getGame() needs to be accessed by getName(), not by toString(), because it is different class
				//(yep, I lost one hour of my life trying to debug it)
				favoriteGameEntity.setGame(new Game(favoriteEntityWeb.getGame().getName()));
				favoriteGameEntity.setPlayer(favoriteEntityWeb.getPlayer());
				favoriteGameEntity
					.setChosenOn(new Date(Utility.XMLGregorianCalendarToTime(favoriteEntityWeb.getChosenOn())));
				favoriteGameEntities.add(favoriteGameEntity);
			}
			return favoriteGameEntities;
		}
		catch (Exception e)
		{
			throw new FavoriteException(e);
		}
	}
}