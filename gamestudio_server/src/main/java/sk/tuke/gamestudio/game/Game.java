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

package sk.tuke.gamestudio.game;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A game.
 * <p/>
 * Created by Steve on 14.03.2016.
 */
@Named
@RequestScoped
public class Game implements Serializable
{
	private String name;
	private String internalPathName;

	private static List<Game> allGames = new LinkedList<>();

	static
	{
		//AD-HOC solution to null minesweeper problem
		allGames.add(new Game("Tesserae"));
		allGames.add(new Game("Minesweeper", "mines"));

		//allGames.add(Tesserae.getGameStatic());
		//allGames.add(Minesweeper.getGameStatic());
	}

	public Game()
	{
	}

	//Full Game constructor
	public Game(String name, String internalPathName)
	{
		this.name = name;
		this.internalPathName = internalPathName;
	}

	//Implicit path name
	public Game(String name)
	{
		this(name, name.toLowerCase());
	}

	public String getName()
	{
		return name;
	}
	public String getInternalPathName()
	{
		return internalPathName;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public boolean equals(Object object)
	{
		return object instanceof Game && ((Game) object).getName().equals(getName());
	}

	//Sometimes JSF can only work with Strings
	public static Game getGameByName(String gameName)
	{
		for (Game game : allGamesStatic())
		{
			if (game.getName().equals(gameName))
			{
				return game;
			}
		}
		return null;
	}

	public static String getAddress(String gameName)
	{
		return getGameByName(gameName).getInternalPathName() + ".xhtml";
	}

	public static String getImageOverview(String gameName)
	{
		return "resources/images/" + getGameByName(gameName).getInternalPathName() + "/overview.png";
	}

	public Collection<Game> getAllGames()
	{
		return Game.allGamesStatic();
	}

	public static Collection<Game> allGamesStatic()
	{
		Collection<Game> games = Collections.unmodifiableCollection(Game.allGames);

		//Easy DEBUG hook with neutral action if everything is OK
		for (Game game : Game.allGames)
		{
			if (game == null)
			{
				throw new RuntimeException("GAME IN ALL GAMES IS NULL!");
			}
		}

		return games;
	}
}
