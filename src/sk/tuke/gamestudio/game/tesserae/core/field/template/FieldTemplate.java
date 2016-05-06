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

package sk.tuke.gamestudio.game.tesserae.core.field.template;

import sk.tuke.gamestudio.game.tesserae.core.TesseraeRuntimeException;

/**
 * Template for designing Field.
 * <p/>
 * Responsibility of this class:
 * Dynamically deciding on a custom, extendable template of a Field.
 * <p/>
 * Created by Steve on 23.2.2016.
 */
public abstract class FieldTemplate
{
	//All of the templates (a.k.a TemplateSettings, but name FieldTemplate.FieldSettings seems redundant)
	public enum Setting
	{
		RECTANGLE, ELLIPSE, PLUS, RING
	}

	//Fields
	private int rows;
	private int columns;
	private boolean[][] tileSpace;

	public FieldTemplate(int rows, int columns)
	{
		changeSize(rows, columns);
	}

	public final void changeSize(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		this.tileSpace = new boolean[rows][columns];
		resetTileSpace();
	}

	//Resets the tile usage to false for the entire field
	protected final void resetTileSpace()
	{
		for (int i = 0; i < rows * columns; i++)
		{
			this.tileSpace[i / columns][i % columns] = false;
		}
	}

	//Sets the tile space using the usingTile() method
	protected final void loadTileUsage()
	{
		for (int i = 0; i < rows * columns; i++)
		{
			if (usingTile(i / columns, i % columns))
			{
				markTile(i / columns, i % columns);
			}
		}

		//Or in other words:
		//for (int row = 0; row < getRows(); row++)
		//	for (int column = 0; column < getColumns(); column++)
		//		if (usingTile(row, column))
		//			markTile(row, column);
	}

	//Lets subclass implement used tile space as a callback, returns true if a tile is to be used
	public abstract boolean usingTile(int row, int column);

	//Template will be using tile on a concrete row and column
	protected final void markTile(int row, int column)
	{
		if (row < 0 || row >= getRows() || column < 0 || column >= getColumns())
		{
			throw new TesseraeRuntimeException("Attempt at marking a wrong tile.");
		}
		this.tileSpace[row][column] = true;
	}

	//Subclass should identify itself as one of the types
	public abstract Setting getTemplateType();

	public final int getColumns()
	{
		return this.columns;
	}
	public final int getRows()
	{
		return this.rows;
	}
}
