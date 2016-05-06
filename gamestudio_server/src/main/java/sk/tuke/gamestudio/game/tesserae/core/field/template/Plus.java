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

/**
 * Plus pattern.
 * <p/>
 * Created by Steve on 6.3.2016.
 */
public class Plus extends FieldTemplate
{
	protected int columnTiles;
	protected int middleColumn;
	protected int columnFreeLeftTiles;

	protected int rowTiles;
	protected int middleRow;
	protected int rowFreeTopTiles;

	public Plus(int rows, int columns)
	{
		super(rows, columns);

		//Not including the centered positions, there will be 2 columns and tiles added
		this.columnTiles = (getColumns() / 6) + 2;
		this.middleColumn = getColumns() / 2 - 1;

		//Empty tiles on the left can be pre-calculated for easier calculations
		this.columnFreeLeftTiles = (getColumns() - this.columnTiles) / 2;

		//Same with rows
		this.rowTiles = (getRows() / 6) + 2;
		this.middleRow = getRows() / 2 - 1;
		this.rowFreeTopTiles = (getColumns() - this.columnTiles) / 2;

		loadTileUsage();
	}

	@Override
	public boolean usingTile(int row, int column)
	{
		if
			(
			//Too small dimensions
			(this.columnTiles == 0 || this.rowTiles == 0)
			||
			//Middle row and column
			(row == this.middleRow && column == this.middleColumn)
			||
			//One of the plus rows
			(row >= this.rowFreeTopTiles && row < this.rowFreeTopTiles + this.rowTiles)
			||
			//One of the plus columns
			(column >= this.columnFreeLeftTiles && column < this.columnFreeLeftTiles + this.columnTiles)
			)
		{
			return true;
		}
		return false;
	}

	@Override
	public Setting getTemplateType()
	{
		return Setting.PLUS;
	}
}
