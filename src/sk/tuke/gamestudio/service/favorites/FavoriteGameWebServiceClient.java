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
			favoriteGameService.addFavorite2(player, game.toString());
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
			favoriteWeb.setGame(favorite.getGame());
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
			favoriteGameService.removeFavorite(player, game.toString());
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
				favoriteGameEntity.setGame(favoriteEntityWeb.getGame());
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
