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

import java.util.Collections;

/**
 * Strategy for secondary Tiles.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class SecondaryStrategy extends MovementStrategy
{
	public SecondaryStrategy(Field field, int tileRow, int tileColumn)
	{
		super(field, tileRow, tileColumn);
	}

	@Override
	protected void addScore()
	{
		getField().addScoreBeforeModification(10);
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

			//6. category: Secondary can jump over an identical secondary or a tertiary
			//7. category: Secondaries that land on primaries after making a legal jump:
			//The secondary that is moved will be added to the tile that it lands on to make a new tertiary

			//Secondaries can only land on a blank space, identical secondaries, or on primaries that are not already
			//contained in that secondary
			&&
			(
				moveOverTile.getType().containsAll(getCurrentType())
			)
			&&
			(
				//Can land on a blank space
				moveToTile.getType().isEmpty()
				||
				//Can land on primaries that are not already contained in that secondary
				(
					moveToTile.isPrimary()
					&&
					Collections.disjoint(getCurrentType(), moveToTile.getType())
				)
				||
				//Can land on identical secondaries
				(
					moveToTile.isSecondary()
					&&
					moveToTile.getType().equals(getCurrentType())
				)
			);
	}
}
