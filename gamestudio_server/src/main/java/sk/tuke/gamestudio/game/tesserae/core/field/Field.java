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

import sk.tuke.gamestudio.game.tesserae.core.TesseraeRuntimeException;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import sk.tuke.gamestudio.game.tesserae.core.tile.strategy.*;
import sk.tuke.gamestudio.game.tesserae.cui.ColorMode;
import sk.tuke.gamestudio.game.tesserae.cui.FieldManager;
import sk.tuke.gamestudio.support.Utility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Field of Tiles, represents a running game during its playing cycle.
 * <p/>
 * Responsibility of this class:
 * Representing an entire instance of a gameplay.
 * <p/>
 * Created by Steve on 22.2.2016.
 */
public class Field implements Serializable
{
	public class FieldInvalidTileSelectionException extends TesseraeRuntimeException
	{
		public FieldInvalidTileSelectionException(int row, int column)
		{
			super("Cannot select a tile on row " + (row + 1) + ", column " + (column + 1) + ".");
		}

		public FieldInvalidTileSelectionException(String message)
		{
			super(message);
		}
	}

	//State of gameplay
	public enum GameState
	{
		PAUSED, PLAYING, WON, LOST
	}

	//Difficulty of a generated field
	public enum DifficultySetting
	{
		EASY, MEDIUM, HARD
	}

	//Fields
	//Size of the Field
	private int rows;
	private int columns;

	//Functionality of selecting a Tile
	private Tile selectedTile;
	private int selectedRow;
	private int selectedColumn;
	private Map<Integer, Boolean> allowedDirections = null;

	//Objects
	private Tile[][] tiles;
	private GameState state;

	//A field can be built using a builder, directly or indirectly
	public Field(FieldBuilder builder)
	{
		this.rows = builder.getRows();
		this.columns = builder.getColumns();
		this.tiles = builder.makeTiles();

		//Initializing a game state
		this.state = GameState.PLAYING;
		updateGameState();
	}

	//Updates game state
	private void updateGameState()
	{
		if (getState() == GameState.PLAYING)
		{
			if (isSolved())
			{
				this.state = GameState.WON;
			}
			else if (!isSolvable())
			{
				this.state = GameState.LOST;
			}
		}
	}

	//Safe accessor for tiles within a field, should simply return null when the access is not legal
	public Tile getTile(int row, int column)
	{
		if (row >= 0 && row < getRows() && column >= 0 && column < getColumns())
		{
			return this.tiles[row][column];
		}
		return null;
	}
	public int getRows()
	{
		return this.rows;
	}
	public int getColumns()
	{
		return this.columns;
	}

	//Uses HashMap, which was loaded during Tile selection, to decode whether the selected tile can move to coordinates
	private boolean isAllowedMovement(int row, int column)
	{
		if (this.allowedDirections == null)
		{
			return false;
		}
		Boolean result = this.allowedDirections.get(row * getRows() + column);
		return result == null ? false : result;
	}

	//Puts allowed movement directions in format of row*getRows()+column into a HashMap
	private void loadAllowedDirections(Set<Direction> directions)
	{
		this.allowedDirections = new HashMap<Integer, Boolean>();
		for (Direction direction : directions)
		{
			//Upwards movements
			if (direction.getVertical().equals(Direction.VerticalDirection.UP))
			{
				if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
				{
					this.allowedDirections.put((getSelectedRow() - 2) * getRows() + getSelectedColumn() - 2, true);
				}
				else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
				{
					this.allowedDirections.put((getSelectedRow() - 2) * getRows() + getSelectedColumn() + 2, true);
				}
				else
				{
					this.allowedDirections.put((getSelectedRow() - 2) * getRows() + getSelectedColumn(), true);
				}
			}
			//Downwards movements
			else if (direction.getVertical().equals(Direction.VerticalDirection.DOWN))
			{
				if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
				{
					this.allowedDirections.put((getSelectedRow() + 2) * getRows() + getSelectedColumn() - 2, true);
				}
				else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
				{
					this.allowedDirections.put((getSelectedRow() + 2) * getRows() + getSelectedColumn() + 2, true);
				}
				else
				{
					this.allowedDirections.put((getSelectedRow() + 2) * getRows() + getSelectedColumn(), true);
				}
			}
			//Only left or right
			else if (direction.getHorizontal().equals(Direction.HorizontalDirection.LEFT))
			{
				this.allowedDirections.put((getSelectedRow()) * getRows() + getSelectedColumn() - 2, true);
			}
			else if (direction.getHorizontal().equals(Direction.HorizontalDirection.RIGHT))
			{
				this.allowedDirections.put((getSelectedRow()) * getRows() + getSelectedColumn() + 2, true);
			}
			//No action on empty direction
		}
	}

	public boolean isSolvable()
	{
		//If at least one tile can move, return true
		for (int row = 0; row < getRows(); row++)
		{
			for (int column = 0; column < getColumns(); column++)
			{
				if (!canMove(row, column).isEmpty())
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean isSolved()
	{
		//If only one tile is remaining, return true
		boolean found = false;
		for (Tile[] tileRow : this.tiles)
		{
			for (Tile tile : tileRow)
			{
				if (tile != null && !tile.getType().isEmpty())
				{
					//If 2 were found, its not solved yet
					if (found)
					{
						return false;
					}
					found = true;
				}
			}
		}
		//If only one was found, it is solved
		return found;
	}
	protected void setState(GameState state)
	{
		this.state = state;
	}
	public GameState getState()
	{
		return this.state;
	}

	//Called when the user decides to end the game
	public void forfeitGame()
	{
		this.state = GameState.LOST;
	}

	//Returns a Field movement strategy of a target Tile
	protected MovementStrategy getMovementStrategy(int tileRow, int tileColumn)
	{
		Tile tile = getTile(tileRow, tileColumn);

		if (tile == null)
		{
			return new EmptyStrategy(this, tileRow, tileColumn);
		}

		Set<Tile.Type> type = tile.getType();

		switch (type.size())
		{
			case 3:
				return new TertiaryStrategy(this, tileRow, tileColumn);
			case 2:
				return new SecondaryStrategy(this, tileRow, tileColumn);
			case 1:
				return new PrimaryStrategy(this, tileRow, tileColumn);
			case 0:
				return new EmptyStrategy(this, tileRow, tileColumn);
			default:
				throw new TesseraeRuntimeException(
					"Wrong type (" + type.size() + ") of Tile, cannot find a proper strategy.");
		}
	}

	//Selects a Tile
	public void selectTile(int row, int column)
	{
		Tile tile = getTile(row, column);

		if (tile != null)
		{
			this.selectedTile = tile;
			this.selectedRow = row;
			this.selectedColumn = column;

			//Loads all allowed directions of the selected tile
			loadAllowedDirections(canMove(row, column));
		}
		else
		{
			throw new FieldInvalidTileSelectionException(row, column);
		}
	}
	public Tile getSelectedTile()
	{
		return this.selectedTile;
	}
	public int getSelectedRow()
	{
		return this.selectedRow;
	}
	public int getSelectedColumn()
	{
		return this.selectedColumn;
	}

	//Lists all possible directions in which a tile can move
	public Set<Direction> canMove(int tileRow, int tileColumn)
	{
		return getMovementStrategy(tileRow, tileColumn).canMove();
	}

	//Moves the selected Tile in a new direction
	public void move(Direction direction)
	{
		move(getSelectedRow(), getSelectedColumn(), direction);
	}

	//Moves a tile in a new direction
	protected void move(int tileRow, int tileColumn, Direction direction)
	{
		//Moves using the relevant strategy
		getMovementStrategy(tileRow, tileColumn).move(direction);

		//Updates the current game state
		gameUpdateCallback();
	}

	//Pauses the game, returns true if the request was handled successfully
	public boolean pause()
	{
		if (getState().equals(GameState.PLAYING))
		{
			setState(GameState.PAUSED);
			return true;
		}
		return false;
	}
	//Unpauses a paused game, returns true if the request was handled successfully
	public boolean unpause()
	{
		if (getState().equals(GameState.PAUSED))
		{
			setState(GameState.PLAYING);
			return true;
		}
		return false;
	}

	//This callback should be called when the state of Field (of any Tile) changed
	protected void gameUpdateCallback()
	{
		updateGameState();
	}

	//Appends spaces in places of zeroes in row number to compensate for character positions in StringBuilder output
	//@param row Displayed row number, don't forget to increment it by one for better user format
	private void appendRowPowerSpace(StringBuilder fieldDrawing, int row)
	{
		//<Old version>
		//Only for all numbers except zero, following is true:
		//Number multiplied by ten has its power of ten increased by 1
		//if (row == 0)
		//{
		//	row++;
		//}
		//old bad version: for(;getRows() >= row * 10; row *= 10)
		//</Old version>

		//What power of ten does row count have? Our goal is the same amount of positions in spaces
		int goalPower = Utility.whatPowerOfTen(getRows());

		//We have to make current row the same power on the output
		for (int rowPower = Utility.whatPowerOfTen(row); rowPower < goalPower; rowPower++)
		{
			fieldDrawing.append(" ");
		}
	}

	private void appendTileRow(StringBuilder fieldDrawing, int row, String graphicSelected,
	                           String graphicAllowedMovement, String graphicNormal)
	{
		boolean isFirst = true;

		for (int column = 0; column < getColumns(); column++)
		{
			//Draws space before each Tile except first one
			if (!isFirst)
			{
				fieldDrawing.append(" ");
			}
			//Writes a space for row number in the first column
			else
			{
				//Append two spaces for graphics around the number and one for a generic single-digit number
				fieldDrawing.append("   ");
				//Append as many spaces as a single-digit number 0 would require for alignment
				appendRowPowerSpace(fieldDrawing, 0);
				isFirst = false;
			}

			//Skip null tiles by drawing spaces
			if (getTile(row, column) == null)
			{
				//Space for one box-ascii-art-character are two of these special spaces: <    >
				fieldDrawing.append("        ");
				continue;
			}

			boolean isSelected = getSelectedTile() != null && getSelectedRow() == row &&
			                     getSelectedColumn() == column;

			fieldDrawing.append(isSelected ? graphicSelected
				                    : (isAllowedMovement(row, column) ? graphicAllowedMovement : graphicNormal));
		}
		fieldDrawing.append("\n");
	}

	//Implicitly colorless
	private void appendTileValuesRow(StringBuilder fieldDrawing, int row, String graphicSelectedLeft,
	                                 String graphicAllowedMovementLeft, String graphicNormalLeft,
	                                 String graphicSelectedRight, String graphicAllowedMovementRight,
	                                 String graphicNormalRight)
	{
		appendTileValuesRow(fieldDrawing, row, graphicSelectedLeft, graphicAllowedMovementLeft, graphicNormalLeft,
		                    graphicSelectedRight, graphicAllowedMovementRight, graphicNormalRight, ColorMode.BLACK);
	}

	//Appends the (graphical) value of a middle row, including all decorative strings
	private void appendTileValuesRow(StringBuilder fieldDrawing, int row, String graphicSelectedLeft,
	                                 String graphicAllowedMovementLeft, String graphicNormalLeft,
	                                 String graphicSelectedRight, String graphicAllowedMovementRight,
	                                 String graphicNormalRight, ColorMode color)
	{
		boolean isFirst = true;

		for (int column = 0; column < getColumns(); column++)
		{
			//Draws space before each Tile except first one
			if (!isFirst)
			{
				fieldDrawing.append(" ");
			}
			//Writes a row number in the first column
			else
			{
				fieldDrawing.append("<");
				//Spaces in case a one-more-than-the-digital-representation number is not as large as the max number
				appendRowPowerSpace(fieldDrawing, row + 1);

				//The written value is one more than the digital representation
				fieldDrawing.append(String.valueOf(row + 1)).append(">");
				isFirst = false;
			}

			//Skip null tiles by drawing spaces
			if (getTile(row, column) == null)
			{
				//Space for one box-ascii-art-character are two of these special spaces: <    >
				fieldDrawing.append("        ");
				continue;
			}

			boolean isSelected = getSelectedTile() != null && getSelectedRow() == row &&
			                     getSelectedColumn() == column;

			//Draws the Tile
			fieldDrawing.append(isSelected ? graphicSelectedLeft
				                    : (isAllowedMovement(row, column) ? graphicAllowedMovementLeft
					                       : graphicNormalLeft));
			fieldDrawing.append(getTile(row, column).toString(color));
			fieldDrawing.append(isSelected ? graphicSelectedRight
				                    : (isAllowedMovement(row, column) ? graphicAllowedMovementRight
					                       : graphicNormalRight));
		}
		fieldDrawing.append("\n");
	}

	//Representation of an entire Field, implicitly colorless
	@Override
	public String toString()
	{
		return toString(ColorMode.BLACK);
	}

	//Optional colored field
	public String toString(ColorMode color)
	{
		//Draws the entire Field as a String
		StringBuilder fieldDrawing = new StringBuilder();

		//Before each line, including the column description line, we need to make enough space for row descriptions
		//The max number is one more than the last row number, because output starts at 1 and not at 0; -1+1 cancels out
		appendRowPowerSpace(fieldDrawing, getRows());
		fieldDrawing.append("  ");

		//Column description line
		for (int column = 0; column < getColumns(); column++)
		{
			fieldDrawing.append("  ┌").append(columnToString(column)).append("┐ "); //Old: ╭╮
		}
		fieldDrawing.append("\n");

		//Every single row represented with top and bottom decoration
		for (int row = 0; row < getRows(); row++)
		{
			appendTileRow(fieldDrawing, row, "╔===╗", "╔---╗", "┌---┐");
			appendTileValuesRow(fieldDrawing, row, "╠", "│", "│", "╣", "│", "│", color); //alternative could be ║
			appendTileRow(fieldDrawing, row, "╚===╝", "╚---╝", "└---┘");
		}

		return fieldDrawing.toString();
	}

	private String columnToString(int column)
	{
		StringBuilder columnString = new StringBuilder();

		column++;
		while (column > 0)
		{
			columnString.append((char) ('A' + (column - 1) % 26));
			column /= 26;
		}

		return columnString.toString();
	}

	//Creates a Virtual Field Manager using Factory Method to implement FieldManager for external simplified purposes
	public static FieldManager createVirtualManager(final String player)
	{
		return new FieldManager()
		{
			private Field field;
			@Override
			public Field getManagedField()
			{
				return field;
			}
			@Override
			public void setManagedField(Field field)
			{
				this.field = field;
			}
			@Override
			public String getPlayer()
			{
				return player;
			}
			@Override
			public ColorMode getFieldColor()
			{
				return ColorMode.BLACK;
			}
			@Override
			public void setFieldColor(ColorMode color)
			{
			}
			@Override
			public void fieldUpdatedCallback()
			{
			}
			@Override
			public void goBackInTime()
			{
			}
			@Override
			public LinkedList<Field> getTimeline()
			{
				return null;
			}
		};
	}
}
