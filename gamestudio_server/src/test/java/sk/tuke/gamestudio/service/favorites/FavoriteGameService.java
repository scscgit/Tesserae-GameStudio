package sk.tuke.gamestudio.service.favorites;

import static org.junit.Assert.*;

/**
 * Created by Steve on 18.04.2016.
 */
public class FavoriteGameService
{
	private FavoriteGameDatabaseService service;

	@org.junit.Before
	public void setUp() throws Exception
	{
		service = new FavoriteGameServiceJPA();
	}
	@org.junit.After
	public void tearDown() throws Exception
	{

	}
	@org.junit.Test
	public void addFavorite() throws Exception
	{
		service.addFavorite("test", "asdf");
		assertEquals(service.getFavorites("test").size(), 1);

		service.removeFavorite("test", "asdf");
		assertEquals(service.getFavorites("test").size(), 0);
	}
}
