/*********************************************************************
 * Zadanie na predmet Komponentove Programovanie
 * <p/>
 * scsc
 * Technicka univerzita v Kosiciach, Fakulta elektrotechniky a informatiky
 * <p/>
 * Copyright: Volny softver, Open-Source GNU GPL v3+
 * Vseobecna verejna licencia. Program je dovolene volne sirit a upravovat.
 * Upraveny program / cast programu moze ktokolvek vyuzit ako na osobne,
 * tak aj komercne ucely, ale nemoze ho vydat s vlastnym copyrightom,
 * ktory nie je kompatibilny s GNU GPL v3+. < gnu.org/licenses/gpl-faq.html >
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see < http://www.gnu.org/licenses/ >.
 */

package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import junit.framework.TestCase;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.service.DatabaseException;
import sk.tuke.gamestudio.support.Utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Steve on 15.03.2016.
 */
public class Oracle11gDatabaseServiceImplTest extends TestCase
{
	private FavoriteGameDatabaseService service = null;

	private FavoriteGameDatabaseService newService()
	{
		this.service = new Oracle11gDatabaseServiceImpl();
		return this.service;
	}

	public void setUp() throws Exception
	{
		super.setUp();

		newService().recreateTable();
	}

	public void tearDown() throws Exception
	{
		super.tearDown();

		newService().recreateTable();
	}

	public void testAddFavorite() throws Exception
	{
		newService();
		List<FavoriteGameEntity> favorites;

		favorites = service.getFavorites("player1");
		assertEquals(favorites.size(), 0);
		service.addFavorite("player1", new Game("game1"));

		favorites = service.getFavorites("player1");
		assertEquals(favorites.size(), 1);
		assertEquals(favorites.get(0).getGame(), new Game("game1"));
		assertEquals(favorites.get(0).getPlayer(), "player1");
	}
	public void testAddFavorite1() throws Exception
	{
		newService();
		List<FavoriteGameEntity> favorites;

		favorites = service.getFavorites("player1");
		assertEquals(favorites.size(), 0);
		service.addFavorite(new FavoriteGameEntity("player1", new Game("game1"), new Timestamp(0)));
		favorites = service.getFavorites("player1");
		assertEquals(favorites.size(), 1);
		assertEquals(favorites.get(0).getGame(), new Game("game1"));
		assertEquals(favorites.get(0).getPlayer(), "player1");
		assertEquals(favorites.get(0).getChosenOn(), new Timestamp(0));
	}
	public void testRemoveFavorites() throws Exception
	{
		String player = "Asdf";

		String game1String = "Gh";
		Game game1 = new Game(game1String);

		String game2String = "Ij";
		Game game2 = new Game(game2String);

		Timestamp time = Utility.getCurrentSqlTimestamp();
		System.out.println(time);
		List<FavoriteGameEntity> list;

		newService();
		try
		{
			//Adding using entity and explicit date, including date comparison
			service.addFavorite(new FavoriteGameEntity(player, game1, time));
			list = service.getFavorites(player);

			//First game will be the first one
			assertTrue(list.get(0).getGame().equals(game1));
			assertTrue(list.get(0).getPlayer().equals(player));
			//Equality in a full format after string conversion
			assertEquals
				(
					new SimpleDateFormat("EE MMM dd hh:mm:ss z yyyy").format(list.get(0).getChosenOn()),
					new SimpleDateFormat("EE MMM dd hh:mm:ss z yyyy").format(time)
				);

			//Direct data equality check just to be sure
			assertTrue(list.get(0).getChosenOn().equals(time));
			assertEquals(list.size(), 1);

			//Adding using implicit date and direct objects
			service.addFavorite(player, game2);
			list = service.getFavorites(player);
			assertEquals(list.size(), 2);

			service.removeFavorite(player, new Game(game1String));
			list = service.getFavorites(player);

			//After removal, second game wil be the first one and there will be only one
			assertTrue(list.get(0).getGame().equals(game2));
			assertTrue(list.get(0).getPlayer().equals(player));
			assertEquals(list.size(), 1);
		}
		catch (Exception e)
		{
			throw new DatabaseException(e);
		}
	}
	public void testGetFavorites() throws Exception
	{
		String player = "Asdf";
		String game = "Gh";
		Timestamp time = Utility.getCurrentSqlTimestamp();

		newService();
		try
		{
			service.addFavorite(new FavoriteGameEntity(player, new Game(game), time));

			List<FavoriteGameEntity> list = service.getFavorites(player);

			assertTrue(list.get(0).getGame().equals(new Game(game)));
			assertTrue(list.get(0).getPlayer().equals(player));
			assertTrue(list.get(0).getChosenOn().equals(time));
			assertEquals(list.size(), 1);
		}
		catch (Exception e)
		{
			throw new DatabaseException(e);
		}
	}
	public void testRecreateTable() throws Exception
	{
		newService();
		service.recreateTable();
	}
	public void testCreateTable() throws Exception
	{
		try
		{
			newService();
			service.dropTable();
		}
		catch (Exception e)
		{
		}

		newService();
		service.createTable();
	}
	public void testDropTable() throws Exception
	{
		try
		{
			newService();
			service.createTable();
		}
		catch (Exception e)
		{
		}

		newService();
		service.dropTable();
	}
}
