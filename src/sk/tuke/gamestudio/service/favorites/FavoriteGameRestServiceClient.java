package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Steve on 19.04.2016.
 */
public class FavoriteGameRestServiceClient implements FavoriteGameDatabaseService
{
	private static final String URL = "http://localhost:8080/gamestudio_server/webapi/favoritegame";
	//private static final String URL = "http://localhost:8080/gamestudio_server/pokus";

	@Override
	public void addFavorite(String player, Game game) throws FavoriteException
	{
		addFavorite(new FavoriteGameEntity(player, game.getName(), Utility.getCurrentSqlTimestamp()));
	}
	@Override
	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		try
		{
			Client client = ClientBuilder.newClient();
			Response response = client.target(URL)
			                          .request(MediaType.APPLICATION_JSON)
			                          .post(Entity.entity(favorite, MediaType.APPLICATION_JSON), Response.class);
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error saving favorite game: " + e.getMessage(), e);
		}
	}
	@Override
	public void removeFavorite(String player, Game game) throws FavoriteException
	{
		//Out of scope
		throw new NotImplementedException();
	}
	@Override
	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		try
		{
			Client client = ClientBuilder.newClient();
			return client.target(URL)
			             .path("/player/" + player)
			             .request(MediaType.APPLICATION_JSON)
			             .get(new GenericType<List<FavoriteGameEntity>>()
			             {
			             });
		}
		catch (Exception e)
		{
			throw new FavoriteException("Error loading favorite games: " + e.getMessage(), e);
		}
	}
}
