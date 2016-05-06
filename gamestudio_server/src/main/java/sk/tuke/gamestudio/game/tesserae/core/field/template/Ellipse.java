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
 * Uses some PI calculations.
 * I admit I used < http://math.stackexchange.com/questions/76457/check-if-a-point-is-within-an-ellipse >.
 * <p/>
 * Created by Steve on 23.2.2016.
 */
public class Ellipse extends FieldTemplate
{
	private double rowRadius;
	private double columnRadius;

	//Improvement over Math.pow() and less prone to programming mistakes than multiplying directly imo
	protected double square(double number)
	{
		return number * number;
	}

	public Ellipse(int rows, int columns)
	{
		super(rows, columns);

		this.rowRadius = getRows() / 2.0;
		this.columnRadius = getColumns() / 2.0;

		loadTileUsage();
	}

	@Override
	public boolean usingTile(int row, int column)
	{
		//To get a better shape of the ellipse, value 1.1 instead of 1 could be a good compromise
		double shapePosition = square(row - rowRadius) / square(rowRadius) +
		                       square(column - columnRadius) / square(columnRadius);
		return shapePosition < 1;
	}

	@Override
	public Setting getTemplateType()
	{
		return Setting.ELLIPSE;
	}
}
