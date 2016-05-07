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

import sk.tuke.gamestudio.game.mines.Mines;
import sk.tuke.gamestudio.game.tesserae.Tesserae;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

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

	private static List<Game> allGames = new LinkedList<>();

	static
	{
		allGames.add(Tesserae.getGame());
		allGames.add(Mines.getGame());
	}

	public Game()
	{
	}

	public Game(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
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

	public static String getAddress(String game)
	{
		return game.toLowerCase() + ".xhtml";
	}

	public static String getImageOverview(String game)
	{
		return "resources/images/" + game.toLowerCase() + "/overview.png";
	}

	public static Collection<Game> allGames()
	{
		return Collections.unmodifiableCollection(Game.allGames);
	}
}
