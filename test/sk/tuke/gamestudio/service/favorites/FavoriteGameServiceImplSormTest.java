package sk.tuke.gamestudio.service.favorites;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.sorm.SORM2;

import static org.junit.Assert.assertEquals;

/**
 * Created by Steve on 18.04.2016.
 */
public class FavoriteGameServiceImplSormTest
{
	//FavoriteGameDatabaseService service1;
	FavoriteGameDatabaseService service2;

	@Before
	public void setUp() throws Exception
	{
		String url = "jdbc:oracle:oci:@localhost:1521:xe";
		String login = "gamestudio";
		String password = "gamestudio";

		//service1 = new FavoriteGameServiceImplSorm(new SORM(url, login, password));
		service2 = new FavoriteGameServiceImplSorm(new SORM2(url, login, password));

		try
		{
			//service1.removeFavorite("test", new Game("asdf"));
		}
		catch (Exception e)
		{
		}

		try
		{
			service2.removeFavorite("test", new Game("asdf"));
		}
		catch (Exception e)
		{
		}
	}
	@After
	public void tearDown() throws Exception
	{

	}

	/*@Test
	public void addFavorite11() throws Exception
	{
		int oldSize = service1.getFavorites("test").size();

		service1.addFavorite("test", new Game("asdf"));
		assertEquals(service1.getFavorites("test").size(), oldSize+1);

		service1.removeFavorite("test", new Game("asdf"));
		assertEquals(service1.getFavorites("test").size(), oldSize);
	}
	@Test
	public void addFavorite12() throws Exception
	{

	}*/

	@Test
	public void addFavorite21() throws Exception
	{
		int oldSize = service2.getFavorites("test").size();

		service2.addFavorite("test", new Game("asdf"));
		assertEquals(service2.getFavorites("test").size(), oldSize+1);

		service2.removeFavorite("test", new Game("asdf"));
		assertEquals(service2.getFavorites("test").size(), oldSize);
	}
	@Test
	public void removeFavorite() throws Exception
	{

	}
	@Test
	public void getFavorites() throws Exception
	{

	}
}
