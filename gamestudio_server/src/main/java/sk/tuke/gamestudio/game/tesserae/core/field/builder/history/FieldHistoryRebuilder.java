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

package sk.tuke.gamestudio.game.tesserae.core.field.builder.history;

import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;

import java.util.LinkedList;

/**
 * Put on your Fez and go back to your box, it's timetravel time.
 * This builder is not your ordinary builder, it's an actual RE-builder.
 * <p>
 * It is implemented using linked lists and every time before the Field changes, it saves its (Field's) old state.
 * The last position will always contain the current state, so that a simple update callback is sufficient for
 * confirmation of saving the state, without needing to save "before" the movement request or as a first function.
 * <p>
 * Created by Steve on 05.04.2016.
 */
public class FieldHistoryRebuilder extends FieldBuilder
{
	private LinkedList<Tile[][]> history;

	public FieldHistoryRebuilder(int rows, int columns)
	{
		super(rows, columns);
		this.history = new LinkedList<Tile[][]>();
	}

	public void saveState(Field field)
	{
		Tile[][] tilesState = new Tile[getRows()][getColumns()];
		for (int row = 0; row < getRows(); row++)
		{
			for (int column = 0; column < getColumns(); column++)
			{
				//Makes an (almost) exact copy of the Tile, that will live its own life
				//(getType() returns mutable Set, so you must never copy it directly!)
				//Safe copying is by using setType(), or just straight within the constructor
				Tile tile = field.getTile(row, column);
				if (tile == null)
				{
					tilesState[row][column] = null;
					continue;
				}
				tilesState[row][column] = new Tile(tile.getType());
			}
		}
		this.history.addLast(tilesState);
	}

	//Returns an entire timeline of the game since it began
	public LinkedList<Field> getTimeline()
	{
		LinkedList<Field> timeline = new LinkedList<Field>();
		//Anonymously generating a field for each movement in the history
		for (final Tile[][] tiles : this.history)
		{
			timeline.addLast
				(
					new FieldBuilder(getRows(), getColumns())
					{
						@Override
						public Tile[][] makeTiles()
						{
							return tiles;
						}
						@Override
						public Field getField()
						{
							return new Field(this);
						}
					}.getField()
				);
		}
		return timeline;
	}

	public int getSizeOfTimeline()
	{
		return this.history.size();
	}

	//Returns a new set of tiles of a previous state by doing an actual timetravel.
	//A.K.A Pop();Top();
	@Override
	public Tile[][] makeTiles()
	{
		//getLast() would probably complain with a loud exception if it, literally, was the last one, pun intended
		if (getSizeOfTimeline() < 2)
		{
			throw new FieldHistoryRebuilderNoHistoryException();
		}
		this.history.removeLast();
		return this.history.getLast();
	}

	//Returns a previous state by doing an actual timetravel.
	//A.K.A Pop();Top();
	@Override
	public Field getField()
	{
		return new Field(this);
	}
}
