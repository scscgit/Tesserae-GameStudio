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

package sk.tuke.gamestudio.game.tesserae.core.tile.strategy;

import sk.tuke.gamestudio.game.tesserae.core.field.Field;

/**
 * Empty tile can never move.
 * <p/>
 * Created by Steve on 08.03.2016.
 */
public class EmptyStrategy extends MovementStrategy
{
	public EmptyStrategy(Field field, int tileRow, int tileColumn)
	{
		super(field, tileRow, tileColumn);
	}

	//Empty Tile can never move
	@Override
	protected boolean canMove(int rowMoveOver, int columnMoveOver, int rowMoveTo, int columnMoveTo)
	{
		return false;
	}

	//Empty Tile will not move (overriding to reduce CPU load)
	@Override
	protected void move(int rowMoveOver, int columnMoveOver, int rowMoveTo, int columnMoveTo)
	{
	}
}
