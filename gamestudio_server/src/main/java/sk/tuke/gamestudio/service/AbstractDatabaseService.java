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

package sk.tuke.gamestudio.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Protected "interface" for inheritance purposes.
 * Defines a common set of methods for Database connection classes.
 * <p/>
 * Created by Steve on 10.03.2016.
 */
public abstract class AbstractDatabaseService
{
	private Connection connection = null;

	protected String url;
	protected String login;
	protected String password;

	protected AbstractDatabaseService(String url, String login, String password)
	{
		this.url = url;
		this.login = login;
		this.password = password;
	}

	//Static method delegation for a private (singleton-like) instance
	protected Connection getConnection(String url, String login, String password) throws SQLException
	{
		if (this.connection == null)
		{
			this.connection = DriverManager.getConnection(url, login, password);
		}
		return this.connection;
	}

	//Implicit inputs are constants
	protected Connection getConnection() throws SQLException
	{
		return getConnection(this.url, this.login, this.password);
	}

	protected void closeConnection()
	{
		try
		{
			if (this.connection != null && !this.connection.isClosed())
			{
				this.connection.close();
				this.connection = null;
			}
		}
		//closeConnection() is usually used in finally block, so throwing exception may not be the best idea
		catch (SQLException e)
		{
			System.out.println("Database could not be closed.");
		}
	}

	//Safely tries to drop table, does nothing if it did not exist
	protected void dropTable(Connection connection, String dropStatement) throws DatabaseException
	{
		try
		{
			connection.prepareStatement("DROP TABLE " + dropStatement).executeUpdate();
		}
		catch (SQLException e)
		{
			//Error 942 means the table did not already exist, in that case we catch exception and do nothing
			if (e.getErrorCode() != 942)
			{
				DatabaseException exception = new DatabaseException(
					"Problem executing DROP TABLE, error code " + e.getErrorCode(), e);
				exception.setErrorCode(e.getErrorCode());
				throw exception;
			}
		}
	}

	protected void createTable(Connection connection, String createStatement) throws DatabaseException
	{
		try
		{
			connection.prepareStatement("CREATE TABLE " + createStatement).executeUpdate();
		}
		catch (SQLException e)
		{
			DatabaseException exception = new DatabaseException(
				"Problem executing CREATE TABLE, error code " + e.getErrorCode(), e);
			exception.setErrorCode(e.getErrorCode());
			throw exception;
		}
	}

	//Drops and creates table, but does so atomically
	protected void recreateTable(String dropStatement, String createStatement) throws DatabaseException
	{
		try
		{
			Connection connection = getConnection();
			connection.setAutoCommit(false);
			dropTable(connection, dropStatement);
			createTable(connection, createStatement);
			connection.commit();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
		finally
		{
			closeConnection();
		}
	}
}
