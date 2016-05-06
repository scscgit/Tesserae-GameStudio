package sk.tuke.gamestudio.webservice.favorites;

import sk.tuke.gamestudio.service.DatabaseException;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Rest assured, it will work.
 * <p>
 * Created by Steve on 12.04.2016.
 */
@Path ("/favoritegame")
public class FavoriteGameRestService
{
	@EJB
	private FavoriteGameDatabaseService favoriteGameService;

	@GET
	@Path ("/player/{player}")
	// The Java method will produce content identified by this MIME Media type
	@Produces ("application/json")
	public String getFavoriteGamesOfPlayer(@PathParam("player") String player) throws DatabaseException
	{
		try
		{
			return favoriteGameService.getFavorites(player).toString();
		}
		catch (FavoriteException e)
		{
			return e.toString();
		}
	}
}
