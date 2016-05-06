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
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;

/**
 * Strategy for primary Tiles.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class PrimaryStrategy extends MovementStrategy
{
	public PrimaryStrategy(Field field, int tileRow, int tileColumn)
	{
		super(field, tileRow, tileColumn);
	}

	@Override
	protected boolean canMove(int rowMoveOver, int columnMoveOver, int rowMoveTo, int columnMoveTo)
	{
		Tile moveOverTile = getTile(rowMoveOver, columnMoveOver);
		Tile moveToTile = getTile(rowMoveTo, columnMoveTo);

		return
			//If any position is not valid (e.g. outside the map), movement will not be possible
			!(
				moveOverTile == null || moveToTile == null
			)

			//1. category: Primary jumping over another primary and landing on a blank space
			//2. category: Primary jumping over a secondary
			//3. category: Primary jumping over a tertiary
			//4. category: Primaries that land on other primaries, after making a legal jump
			//5. category: Primaries that land on secondaries after making a legal jump

			//Primaries can only land on a blank space, other primaries, or secondaries that do not already
			//contain that primary.

			//Current jumping Tile must have all types that a Tile being jumped over has, or be jumping over primary,
			//in which case the Tile jumped over will lose its type.

			//Tile can land on an empty Tile, it can "combine" with a same primary.
			//It can also get the type added to a different primary or secondary.
			&&
			(
				moveOverTile.getType().containsAll(getCurrentType())
				||
				moveOverTile.isPrimary()
			)
			&&
			(
				moveToTile.getType().isEmpty()
				||
				moveToTile.isPrimary()
				||
				(
					moveToTile.isSecondary()
					&&
					!moveToTile.getType().containsAll(getCurrentType())
				)
			);
	}
}
