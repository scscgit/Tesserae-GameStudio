package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;
import sk.tuke.sorm.ISORM;
import sk.tuke.sorm.SORM;
import sk.tuke.sorm.SORM2;

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

	//SORM2 constructor
	@Deprecated
	public FavoriteGameServiceImplSorm(String url, String login, String password)
	{
		this.sorm = new SORM2(url, login, password);
	}

	@Override
	public void addFavorite(String player, Game game) throws FavoriteException
	{
		try
		{
			sorm.insert(new FavoriteGameEntity(player, game.toString(), Utility.getCurrentSqlTimestamp()));
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error adding a favorite game: " + e.getMessage(), e);
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
			throw new FavoriteException("Error adding a favorite game: " + e.getMessage(), e);
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
				if (entity.getGame().equals(game.getName()))
				{
					try
					{
						sorm.delete(new FavoriteGameEntity(player, game.getName(), entity.getChosenOn()));
					}
					catch (Exception e)
					{
						throw new FavoriteException(
							"Error removing favorite game '" + game + "' entry of player '" + player + "': " +
							e.getMessage(), e);
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
			throw new FavoriteException("Error loading favorite games: " + e.getMessage(), e);
		}
	}
}
