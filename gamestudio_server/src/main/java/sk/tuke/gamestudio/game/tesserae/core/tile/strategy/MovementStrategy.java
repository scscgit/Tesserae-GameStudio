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

import sk.tuke.gamestudio.game.tesserae.core.TesseraeRuntimeException;
import sk.tuke.gamestudio.game.tesserae.core.field.Direction;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import sk.tuke.gamestudio.game.tesserae.core.tile.TileCannotMoveException;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract interface for calling movement strategies on tiles.
 * <p/>
 * Created by Steve on 22.2.2016.
 */
public abstract class MovementStrategy
{
	//Fields
	private Field field;
	private int tileRow;
	private int tileColumn;

	public MovementStrategy(Field field, int tileRow, int tileColumn)
	{
		this.field = field;
		this.tileRow = tileRow;
		this.tileColumn = tileColumn;
	}

	protected final Field getField()
	{
		return this.field;
	}
	protected final int getTileRow()
	{
		return this.tileRow;
	}
	protected final int getTileColumn()
	{
		return this.tileColumn;
	}
	protected final Tile getTile(int row, int column)
	{
		return getField().getTile(row, column);
	}

	//Implicit operations on current tile
	protected final Tile getCurrentTile()
	{
		return getTile(this.tileRow, this.tileColumn);
	}
	protected final Set<Tile.Type> getCurrentType()
	{
		return getCurrentTile().getType();
	}

	protected void assertCanMove(Direction direction)
	{
		if (!canMove().contains(direction))
		{
			throw new TileCannotMoveException
				(
					"Tile cannot move to direction " +
					direction.getHorizontal().toString() + " " +
					direction.getVertical().toString() + ".\n" +
					"It can move to " + canMove().size() + " directions" +
					(
						canMove().size() == 0 ?
							""
							:
								(
									", e.g. " +
									canMove().toArray(new Direction[]{})[0].getHorizontal().toString()
									+ " " +
									canMove().toArray(new Direction[]{})[0].getVertical().toString()
								)
					)
					+ "."
				);
		}
	}

	protected abstract boolean canMove(int rowMoveOver, int columnMoveOver, int rowMoveTo, int columnMoveTo);

	//Returns all legal movement directions of movement
	public final Set<Direction> canMove()
	{
		Set<Direction> result = new HashSet<Direction>();

		//Upwards movements
		if (canMove(getTileRow() - 1, getTileColumn() - 1, getTileRow() - 2, getTileColumn() - 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.LEFT, Direction.VerticalDirection.UP));
		}
		if (canMove(getTileRow() - 1, getTileColumn() + 1, getTileRow() - 2, getTileColumn() + 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.RIGHT, Direction.VerticalDirection.UP));
		}
		if (canMove(getTileRow() - 1, getTileColumn(), getTileRow() - 2, getTileColumn()))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.NONE, Direction.VerticalDirection.UP));
		}

		//Downwards movements
		if (canMove(getTileRow() + 1, getTileColumn() - 1, getTileRow() + 2, getTileColumn() - 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.LEFT, Direction.VerticalDirection.DOWN));
		}
		if (canMove(getTileRow() + 1, getTileColumn() + 1, getTileRow() + 2, getTileColumn() + 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.RIGHT, Direction.VerticalDirection.DOWN));
		}
		if (canMove(getTileRow() + 1, getTileColumn(), getTileRow() + 2, getTileColumn()))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.NONE, Direction.VerticalDirection.DOWN));
		}

		//Only left or right
		if (canMove(getTileRow(), getTileColumn() - 1, getTileRow(), getTileColumn() - 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.LEFT, Direction.VerticalDirection.NONE));
		}
		if (canMove(getTileRow(), getTileColumn() + 1, getTileRow(), getTileColumn() + 2))
		{
			result.add(Direction.getDirection(Direction.HorizontalDirection.RIGHT, Direction.VerticalDirection.NONE));
		}

		return result;
	}

	//Moves to a (legal) direction
	protected void move(int rowMoveOver, int columnMoveOver, int rowMoveTo, int columnMoveTo)
	{
		Tile moveOverTile = getTile(rowMoveOver, columnMoveOver);
		Tile moveToTile = getTile(rowMoveTo, columnMoveTo);

		//If any position is not valid (e.g. outside the map), don't move (afaik this should not happen thx to assert)
		if (moveOverTile == null || moveToTile == null)
		{
			throw new TesseraeRuntimeException("Attempted to move a Tile to a an invalid location.");
		}

		if (canMove(rowMoveOver, columnMoveOver, rowMoveTo, columnMoveTo))
		{
			moveOverTile.jumpedOverBy(getCurrentTile());
			moveToTile.jumpedOnBy(getCurrentTile());

			//Old tile gets its Type reset
			getTile(getTileRow(), getTileColumn()).getType().clear();

			//After jumping on a new position, current position gets modified.
			getField().selectTile(rowMoveTo, columnMoveTo);
		}
	}

	//Moves to a (legal) direction, dispatches to coordinates over an overloaded method
	public final void move(Direction direction)
	{
		assertCanMove(direction);

		//Upwards movements
		if (direction.getVertical().equals(Direction.VerticalDirection.UP))
		{
			if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
			{
				move(getTileRow() - 1, getTileColumn() - 1, getTileRow() - 2, getTileColumn() - 2);
			}
			else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
			{
				move(getTileRow() - 1, getTileColumn() + 1, getTileRow() - 2, getTileColumn() + 2);
			}
			else
			{
				move(getTileRow() - 1, getTileColumn(), getTileRow() - 2, getTileColumn());
			}
		}
		//Downwards movements
		else if (direction.getVertical().equals(Direction.VerticalDirection.DOWN))
		{
			if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
			{
				move(getTileRow() + 1, getTileColumn() - 1, getTileRow() + 2, getTileColumn() - 2);
			}
			else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
			{
				move(getTileRow() + 1, getTileColumn() + 1, getTileRow() + 2, getTileColumn() + 2);
			}
			else
			{
				move(getTileRow() + 1, getTileColumn(), getTileRow() + 2, getTileColumn());
			}
		}
		//Only left or right
		else if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
		{
			move(getTileRow(), getTileColumn() - 1, getTileRow(), getTileColumn() - 2);
		}
		else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
		{
			move(getTileRow(), getTileColumn() + 1, getTileRow(), getTileColumn() + 2);
		}
		//No action on empty direction
	}
}
