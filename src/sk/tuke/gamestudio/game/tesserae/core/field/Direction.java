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

package sk.tuke.gamestudio.game.tesserae.core.field;

/**
 * 2D direction representation.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class Direction
{
	public enum HorizontalDirection
	{
		LEFT, NONE, RIGHT
	}

	public enum VerticalDirection
	{
		UP, NONE, DOWN
	}

	private static final Direction LEFT_UP = new Direction(HorizontalDirection.LEFT, VerticalDirection.UP);
	private static final Direction NONE_UP = new Direction(HorizontalDirection.NONE, VerticalDirection.UP);
	private static final Direction RIGHT_UP = new Direction(HorizontalDirection.RIGHT, VerticalDirection.UP);
	private static final Direction LEFT_DOWN = new Direction(HorizontalDirection.LEFT, VerticalDirection.DOWN);
	private static final Direction NONE_DOWN = new Direction(HorizontalDirection.NONE, VerticalDirection.DOWN);
	private static final Direction RIGHT_DOWN = new Direction(HorizontalDirection.RIGHT, VerticalDirection.DOWN);
	private static final Direction LEFT_NONE = new Direction(HorizontalDirection.LEFT, VerticalDirection.NONE);
	private static final Direction NONE_NONE = new Direction(HorizontalDirection.NONE, VerticalDirection.NONE);
	private static final Direction RIGHT_NONE = new Direction(HorizontalDirection.RIGHT, VerticalDirection.NONE);

	private HorizontalDirection horizontal;
	private VerticalDirection vertical;

	private Direction(HorizontalDirection horizontal, VerticalDirection vertical)
	{
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	//Direction seems a good reason for using a singleton
	public static Direction getDirection(HorizontalDirection horizontal, VerticalDirection vertical)
	{
		if (horizontal == null || vertical == null)
		{
			return NONE_NONE;
		}

		if (horizontal.equals(HorizontalDirection.LEFT))
		{
			return vertical.equals(VerticalDirection.UP) ? LEFT_UP
				: (vertical.equals(VerticalDirection.DOWN) ? LEFT_DOWN : LEFT_NONE);
		}
		else if (horizontal.equals(HorizontalDirection.RIGHT))
		{
			return vertical.equals(VerticalDirection.UP) ? RIGHT_UP
				: (vertical.equals(VerticalDirection.DOWN) ? RIGHT_DOWN : RIGHT_NONE);
		}
		else
		{
			return vertical.equals(VerticalDirection.UP) ? NONE_UP
				: (vertical.equals(VerticalDirection.DOWN) ? NONE_DOWN : NONE_NONE);
		}
	}

	//My implementation of equal relation in order to be used within Set.contains().
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Direction)
		{
			Direction direction = (Direction) obj;
			return direction.getHorizontal() == getHorizontal() && direction.getVertical() == getVertical();
		}
		else
		{
			return super.equals(obj);
		}
	}

	public HorizontalDirection getHorizontal()
	{
		return horizontal;
	}
	public VerticalDirection getVertical()
	{
		return vertical;
	}
}
