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

import sk.tuke.gamestudio.game.tesserae.core.field.template.FieldTemplate;

import java.io.Serializable;

/**
 * All settings of a game (Field) represented in a single object.
 * Indirectly compatible with FieldBuilder.
 * <p/>
 * Created by Steve on 1.3.2016.
 */
public class Settings implements Serializable
{
	//Singleton versions
	public static final Settings SIMPLE_GAME = new Settings(7, 7, Field.DifficultySetting.EASY,
	                                                        FieldTemplate.Setting.RECTANGLE);

	//Fields
	private int rows, columns;
	private Field.DifficultySetting difficulty;
	private FieldTemplate.Setting template;

	public Settings(int rows, int columns, Field.DifficultySetting difficulty,
	                FieldTemplate.Setting template)
	{
		this.rows = rows;
		this.columns = columns;
		this.difficulty = difficulty;
		this.template = template;
	}

	public int getRows()
	{
		return this.rows;
	}
	public int getColumns()
	{
		return this.columns;
	}
	public Field.DifficultySetting getDifficulty()
	{
		return this.difficulty;
	}
	public FieldTemplate.Setting getFieldTemplate()
	{
		return this.template;
	}
}
