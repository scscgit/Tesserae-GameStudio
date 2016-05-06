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

package sk.tuke.gamestudio.game.tesserae.core.tile;

import sk.tuke.gamestudio.game.tesserae.cui.ColorMode;
import sk.tuke.gamestudio.support.Utility;

import java.util.Set;

/**
 * A single Tesserae tile.
 * <p/>
 * Created by Steve on 22.2.2016.
 */
public class Tile
{
	public enum Type
	{
		PLUS, SQUARE, CIRCLE
	}

	//Fields
	private Set<Type> type;

	public Tile(Set<Type> type)
	{
		setType(type);
	}

	public Set<Type> getType()
	{
		return this.type;
	}

	@Deprecated
	public boolean includesType(Type type)
	{
		return getType().contains(type);
	}

	public boolean isPrimary()
	{
		return getType().size() == 1;
	}
	public boolean isSecondary()
	{
		return getType().size() == 2;
	}
	public boolean isTertiary()
	{
		return getType().size() == 3;
	}

	//Transformation of a tile into a new tile
	public void setType(Set<Type> type)
	{
		this.type = type;
	}

	//Other tile jumps over this tile. This method gets called during lifecycle of the original tile.
	public void jumpedOverBy(Tile tile)
	{
		if (isPrimary())
		{
			//Primary being jumped over by another primary is a special exception, in which Tile jumped over
			//loses its type. All other cases are handled in else branch
			getType().clear();
		}
		else
		{
			//We simply remove all the same types that also the calling tile has
			//Its nice that we don't need to setType, as removeAll() is an operational method
			getType().removeAll(tile.getType());
		}
	}

	//Other tile jumps on this tile. This method gets called during lifecycle of the original tile.
	public void jumpedOnBy(Tile tile)
	{
		//This will be probably enough, but ofc. todo check it later
		getType().addAll(tile.getType());
	}

	@Override
	public String toString()
	{
		return toString(ColorMode.BLACK);
	}

	public String toString(ColorMode color)
	{
		if (color.equals(ColorMode.COLORS))
		{
			return
				(getType().contains(Type.CIRCLE) ? Utility.redColor(" ") : " ") +
				(getType().contains(Type.SQUARE) ? Utility.blueColor(" ") : " ") +
				(getType().contains(Type.PLUS) ? Utility.greenColor(" ") : " ");
		}
		else if (color.equals(ColorMode.COLORED_TEXT))
		{
			return
				(getType().contains(Type.CIRCLE) ? Utility.redText("O") : " ") +
				(getType().contains(Type.SQUARE) ? Utility.blueText("#") : " ") +
				(getType().contains(Type.PLUS) ? Utility.greenText("+") : " ");
		}
		else
		{
			return
				(getType().contains(Type.CIRCLE) ? "O" : " ") +
				(getType().contains(Type.SQUARE) ? "#" : " ") +
				(getType().contains(Type.PLUS) ? "+" : " ");
		}
	}
}
