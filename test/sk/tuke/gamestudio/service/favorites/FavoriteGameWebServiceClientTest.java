package sk.tuke.gamestudio.service.favorites;

import com.sun.tools.ws.resources.WebserviceapMessages;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

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

	}
	@Test
	public void addFavorite() throws Exception
	{
		List<FavoriteGameEntity> list = service.getFavorites("test");
		int oldSize = list.size();

		Date date = Utility.getCurrentSqlTimestamp();
		FavoriteGameEntity entity =  new FavoriteGameEntity("test", new Game("game1"), date);
		service.addFavorite(entity);

		//asserting the size increased
		list = service.getFavorites("test");
		assertEquals(oldSize+1, list.size());

		//downloading the last entity
		FavoriteGameEntity receivedEntity = list.get(list.size()-1);

		//comparing the entity
		assertEquals("game1", receivedEntity.getGame().toString());
		assertEquals("test", receivedEntity.getPlayer());
		assertEquals(date, receivedEntity.getChosenOn());
	}
	@Test
	public void addFavorite1() throws Exception
	{

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
