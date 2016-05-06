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
import sk.tuke.gamestudio.service.AbstractDatabaseService;
import sk.tuke.gamestudio.service.DatabaseException;
import sk.tuke.gamestudio.service.StatementAttribute;
import sk.tuke.gamestudio.support.Utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A free database server that we work with in other class.
 * Oracle 11g running on localhost with almost-default settings, that is, the account and password are not default.
 * <p>
 * All database connections are atomical and utilize connect+close within their lifecycle.
 * <p/>
 * Modified by Steve on 10.03.2016.
 * Created by jaros_000 on 3.3.2016.
 */
public class Oracle11gDatabaseServiceImpl extends AbstractDatabaseService implements FavoriteGameDatabaseService
{
	private static final String URL = "jdbc:oracle:oci:@localhost:1521:xe";
	private static final String LOGIN = "gamestudio"; //default system
	private static final String PASSWORD = "gamestudio"; //default oracle

	private static final String DROP_STMT =
		"favorites";

	private static final String CREATE_STMT =
		"favorites ( player varchar(50) NOT NULL, game varchar(50) NOT NULL, chosen_on timestamp NOT NULL, PRIMARY KEY (player, game) )";

	private static final String INSERT_STMT =
		"favorites (player, game, chosen_on) VALUES (?, ?, ?)";

	private static final String DELETE_STMT =
		"favorites WHERE player = ? AND game = ?";

	private static final String SELECT_STMT =
		"player, game, chosen_on FROM favorites WHERE player = ? ORDER BY game DESC";

	public Oracle11gDatabaseServiceImpl()
	{
		super(URL, LOGIN, PASSWORD);
	}

	//Adds a new Favorite record of a game for a player implicitly with the current date
	public void addFavorite(String player, String game) throws FavoriteException
	{
		addFavorite(new FavoriteGameEntity(player, game, Utility.getCurrentSqlTimestamp()));
	}

	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		try
		{
			PreparedStatement ps = getConnection().prepareStatement("INSERT INTO " + INSERT_STMT);
			StatementAttribute.set(ps, 1, favorite.getPlayer());
			StatementAttribute.set(ps, 2, favorite.getGame());
			StatementAttribute.set(ps, 3, favorite.getChosenOnTimestamp());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			FavoriteException exception = new FavoriteException("Error adding a favorite game: " + e.getMessage(), e);
			exception.setErrorCode(e.getErrorCode());
			throw exception;
		}
		finally
		{
			closeConnection();
		}
	}

	public void removeFavorite(String player, String game) throws FavoriteException
	{
		try
		{
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM " + DELETE_STMT);
			StatementAttribute.set(ps, 1, player);
			StatementAttribute.set(ps, 2, game);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new FavoriteException("Error removing favorite games: " + e.getMessage(), e)
				.setErrorCode(e.getErrorCode());
		}
		finally
		{
			closeConnection();
		}
	}

	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		List<FavoriteGameEntity> favorites = new ArrayList<FavoriteGameEntity>();

		try
		{
			PreparedStatement ps = getConnection().prepareStatement("SELECT " + SELECT_STMT);
			StatementAttribute.set(ps, 1, player);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				favorites.add
					(
						new FavoriteGameEntity
							(
								rs.getString(1),
								rs.getString(2),
								rs.getTimestamp(3)
							)
					);
			}
		}
		catch (SQLException e)
		{
			throw new FavoriteException("Error loading favorite games: " + e.getMessage(), e);
		}

		return favorites;
	}

	public void recreateTable() throws FavoriteException
	{
		try
		{
			recreateTable(DROP_STMT, CREATE_STMT);
		}
		catch (DatabaseException e)
		{
			throw new FavoriteException(e);
		}
	}

	public void createTable() throws FavoriteException
	{
		try
		{
			createTable(getConnection(), CREATE_STMT);
		}
		catch (SQLException e)
		{
			throw new FavoriteException("Problem connecting to a database: " + e.getMessage(), e)
				.setErrorCode(e.getErrorCode());
		}
		catch (DatabaseException e)
		{
			throw new FavoriteException("Problem creating a table: " + e.getMessage(), e)
				.setErrorCode(e.getErrorCode());
		}
		finally
		{
			closeConnection();
		}
	}

	//Catches the exception if the table already existed
	public void createTableIfNone() throws FavoriteException
	{
		try
		{
			createTable();
		}
		catch (FavoriteException e)
		{
			if (e.getErrorCode() != 955)
			{
				throw e;
			}
		}
	}

	public void dropTable() throws FavoriteException
	{
		try
		{
			dropTable(getConnection(), DROP_STMT);
		}
		catch (SQLException e)
		{
			throw new FavoriteException("Problem connecting to a database: " + e.getMessage(), e)
				.setErrorCode(e.getErrorCode());
		}
		catch (DatabaseException e)
		{
			throw new FavoriteException("Problem dropping a table: " + e.getMessage(), e)
				.setErrorCode(e.getErrorCode());
		}
		finally
		{
			closeConnection();
		}
	}
}
