package sk.tuke.gamestudio.webservice.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.service.DatabaseException;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Rest assured, it will work. (Or will it?)
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
	//@Produces ("text/xml")
	public List<FavoriteGameEntity> getFavoriteGamesOfPlayer(@PathParam ("player") String player)
		throws DatabaseException
	{
		try
		{
			List<FavoriteGameEntity> favorites = favoriteGameService.getFavorites(player);
			return favorites;
		}
		catch (FavoriteException e)
		{
			throw new FavoriteException("Could not get the list of favorite games: " + e.getMessage());
		}
	}

	@POST
	@Consumes ("application/json")
	//@Consumes ("text/xml")
	public Response addFavoriteGame(FavoriteGameEntity favoriteGame) throws DatabaseException
	{
		try
		{
			favoriteGameService.addFavorite(favoriteGame);
			return Response.ok().build();
		}
		catch (FavoriteException e)
		{
			return Response.serverError().build();
		}
	}
}
