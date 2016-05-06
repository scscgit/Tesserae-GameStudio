package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;
import sk.tuke.sorm.ISORM;
import sk.tuke.sorm.SORM;

import java.util.List;

/**
 * Created by Steve on 18.04.2016.
 */
public class FavoriteGameServiceImplSorm implements FavoriteGameDatabaseService
{
	private ISORM sorm;

	//Custom SORM constructor
	public FavoriteGameServiceImplSorm(ISORM sorm)
	{
		this.sorm = sorm;
	}

	//SORM1 constructor
	public FavoriteGameServiceImplSorm(String url, String login, String password)
	{
		this.sorm = new SORM(url, login, password);
	}

	@Override
	public void addFavorite(String player, Game game) throws FavoriteException
	{
		try
		{
			sorm.insert(new FavoriteGameEntity(player, game, Utility.getCurrentSqlTimestamp()));
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error adding a favorite game", e);
		}
	}
	@Override
	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		try
		{
			sorm.insert(favorite);
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error adding a favorite game", e);
		}
	}
	@Override
	public void removeFavorite(String player, Game game) throws FavoriteException
	{
		List<FavoriteGameEntity> list = getFavorites(player);
		if (list.size() > 0)
		{
			for (FavoriteGameEntity entity : list)
			{
				if (entity.getGame().equals(game))
				{
					try
					{
						sorm.delete(new FavoriteGameEntity(player, game, entity.getChosenOn()));
					}
					catch (Exception e)
					{
						throw new FavoriteException(
							"Error removing favorite game '" + game + "' entry of player '" + player + "'", e);
					}
				}
			}
		}
		else
		{
			throw new FavoriteException("There was no favorite game to be removed");
		}
	}
	@Override
	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		try
		{
			return (List<FavoriteGameEntity>) sorm.select(FavoriteGameEntity.class, "player = '" + player + "'");
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error loading favorite games", e);
		}
	}
}
