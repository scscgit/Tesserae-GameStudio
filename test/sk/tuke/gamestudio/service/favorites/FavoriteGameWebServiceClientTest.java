package sk.tuke.gamestudio.service.favorites;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Steve on 15.04.2016.
 */
public class FavoriteGameWebServiceClientTest
{
	private FavoriteGameDatabaseService service;

	@Before
	public void setUp() throws Exception
	{
		this.service = new FavoriteGameWebServiceClient();
	}
	@After
	public void tearDown() throws Exception
	{
		//inefficient but whatever
		removeFavorite();
	}
	@Test
	public void addFavorite() throws Exception
	{
		List<FavoriteGameEntity> list = service.getFavorites("test");
		int oldSize = list.size();

		Date date = Utility.getCurrentSqlTimestamp();
		FavoriteGameEntity entity = new FavoriteGameEntity("test", new Game("game1"), date);
		service.addFavorite(entity);

		//asserting the size increased
		list = service.getFavorites("test");
		assertEquals(oldSize + 1, list.size());

		//downloading the last entity
		FavoriteGameEntity receivedEntity = list.get(list.size() - 1);

		//comparing the entity
		assertEquals("game1", receivedEntity.getGame().toString());
		assertEquals("test", receivedEntity.getPlayer());
		assertEquals(new Timestamp(date.getTime()), new Timestamp(receivedEntity.getChosenOn().getTime()));
	}
	@Test
	public void addFavorite1() throws Exception
	{
		List<FavoriteGameEntity> list = service.getFavorites("test");
		int oldSize = list.size();

		service.addFavorite("test", new Game("game1"));

		//asserting the size increased
		list = service.getFavorites("test");
		assertEquals(oldSize + 1, list.size());

		//downloading the last entity
		FavoriteGameEntity receivedEntity = list.get(list.size() - 1);

		//comparing the entity
		assertEquals("game1", receivedEntity.getGame().toString());
		assertEquals("test", receivedEntity.getPlayer());
	}
	@Test
	public void removeFavorite() throws Exception
	{
		service.addFavorite("test", new Game("game1"));
		assertTrue(service.getFavorites("test").size() > 0);
		service.removeFavorite("test", new Game("game1"));
		//player test wont have any favorites left
		assertTrue(service.getFavorites("test").size() == 0);
	}
	@Test
	public void getFavorites() throws Exception
	{

	}
}
