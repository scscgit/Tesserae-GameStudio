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

package sk.tuke.gamestudio.game.tesserae.core.field.builder;

import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.template.*;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;

/**
 * Builder that generates Fields (based on direct attributes, it is useful to use it in conjunction with Settings).
 * <p/>
 * Responsibility of this class:
 * Fully creating a new Field instance.
 * <p/>
 * Created by Steve on 23.2.2016.
 */
public abstract class FieldBuilder
{
	//Fields
	private Integer rows = 0;
	private Integer columns = 0;
	private FieldTemplate.Setting template = null;
	private Field.DifficultySetting difficulty = null;

	//Empty builder
	public FieldBuilder()
	{
	}

	//Initialize a new builder only with the size of a Field
	public FieldBuilder(int rows, int columns)
	{
		setSize(rows, columns);
	}

	//Build directly from game settings
	public FieldBuilder(Settings settings)
	{
		setSize(settings.getRows(), settings.getColumns())
			.setDifficulty(settings.getDifficulty())
			.setTemplate(settings.getFieldTemplate());
	}

	//Part of the builder process
	public final FieldBuilder setSize(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		return this;
	}

	//Part of the builder process
	public final FieldBuilder setDifficulty(Field.DifficultySetting difficulty)
	{
		this.difficulty = difficulty;
		return this;
	}

	//Part of the builder process
	public final FieldBuilder setTemplate(FieldTemplate.Setting template)
	{
		this.template = template;
		return this;
	}

	//Returns true if the builder is ready to construct a field
	public boolean isReady()
	{
		return getTemplate() != null && getDifficulty() != null && getColumns() > 0 && getRows() > 0;
	}

	//Converts all current builder values into a new Settings object
	public Settings getSettings()
	{
		return new Settings(getRows(), getColumns(), getDifficulty(), getTemplate());
	}

	public final int getRows()
	{
		return rows;
	}
	public final int getColumns()
	{
		return columns;
	}

	//Returns the current Template Setting
	public final FieldTemplate.Setting getTemplate()
	{
		return template;
	}
	//Returns the current Difficulty Setting
	public final Field.DifficultySetting getDifficulty()
	{
		return difficulty;
	}

	//Factory method for generating a new FieldTemplate object based on a requested Template, general implementation
	public FieldTemplate makeFieldTemplate(FieldTemplate.Setting template)
	{
		switch (template)
		{
			case ELLIPSE:
				return new Ellipse(getRows(), getColumns());
			case RING:
				return new Ring(getRows(), getColumns());
			case PLUS:
				return new Plus(getRows(), getColumns());
			case RECTANGLE:
			default:
				return new Rectangle(getRows(), getColumns());
		}
	}

	//Generates and returns a new array of Tiles, needed by Field during its construction
	public abstract Tile[][] makeTiles();

	//The main access point, creates a new Field using this builder
	public abstract Field getField();
}
