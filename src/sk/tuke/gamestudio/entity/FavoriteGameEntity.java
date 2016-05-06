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

package sk.tuke.gamestudio.entity;

import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.support.Utility;

import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * A single entity representing a relation of a user to a game.
 * <p/>
 * Created by Steve on 14.03.2016.
 */
public class FavoriteGameEntity implements Serializable, Comparable<FavoriteGameEntity>
{
	@Id
	private Integer id;
	private String player;
	private Game game;
	private java.util.Date chosenOn;

	public FavoriteGameEntity()
	{
	}

	public FavoriteGameEntity(String player, Game game, java.util.Date chosenOn)
	{
		this.player = player;
		this.game = game;
		this.chosenOn = chosenOn;
	}

	public FavoriteGameEntity(Integer id, String player, Game game, java.util.Date chosenOn)
	{
		this.id = id;
		this.player = player;
		this.game = game;
		this.chosenOn = chosenOn;
	}

	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getPlayer()
	{
		return player;
	}
	public void setPlayer(String player)
	{
		this.player = player;
	}
	public Game getGame()
	{
		return game;
	}
	public void setGame(Game game)
	{
		this.game = game;
	}
	@Deprecated
	public Date getChosenOnDate()
	{
		return new Date(chosenOn.getTime());
	}
	@Deprecated
	public Time getChosenOnTime()
	{
		return new Time(getChosenOn().getTime());
	}

	public Timestamp getChosenOnTimestamp()
	{
		return new Timestamp(chosenOn.getTime());
	}
	public java.util.Date getChosenOn()
	{
		return this.chosenOn;
	}
	public void setChosenOn(Date chosenOn)
	{
		this.chosenOn = chosenOn;
	}

	public String toJSON()
	{
		return
			"FavoriteGame{"
			+ "id = " + getId()
			+ "player = " + getPlayer()
			+ "game = " + getGame()
			+ "chosenOn = " + getChosenOn()
			+ "}";
	}

	public String toStringHumanReadable()
	{
		return "Player "
		       + getPlayer()
		       + " has game "
		       + getGame().toString()
		       + " added as favorite since "
		       + Utility.formatDate(getChosenOn())
		       + ".";
	}

	@Override
	public String toString()
	{
		return toJSON();
	}

	@Override
	public int compareTo(FavoriteGameEntity o)
	{
		return getChosenOn().compareTo(o.getChosenOn());
	}
}
