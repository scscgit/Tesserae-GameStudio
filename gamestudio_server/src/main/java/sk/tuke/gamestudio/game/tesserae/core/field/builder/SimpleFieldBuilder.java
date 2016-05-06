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
import sk.tuke.gamestudio.game.tesserae.core.field.template.FieldTemplate;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import sk.tuke.gamestudio.support.Utility;

import java.util.EnumSet;
import java.util.Set;

/**
 * FieldBuilder implementation.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class SimpleFieldBuilder extends FieldBuilder
{
	//Constants
	//Percentage chances within difficulties of secondary or ternary pieces
	public static final int EASY_SECONDARY_PERCENT = 20;
	public static final int EASY_TERNARY_PERCENT = 0;
	public static final int MEDIUM_SECONDARY_PERCENT = 35;
	public static final int MEDIUM_TERNARY_PERCENT = 10;
	public static final int HARD_SECONDARY_PERCENT = 60;
	public static final int HARD_TERNARY_PERCENT = 20;

	//Empty builder
	public SimpleFieldBuilder()
	{
	}

	//Initialize a new builder only with the size of a Field
	public SimpleFieldBuilder(int rows, int columns)
	{
		super(rows, columns);
	}

	//Build directly from game Settings
	public SimpleFieldBuilder(Settings settings)
	{
		super(settings);
	}

	//Decides on a type for a next Tile based on expected chances of the current settings:
	//primary + secondary + ternary = 100 percent
	//Implementation that isn't 100% mathematically correct, by selecting one type we decrease chance of another type
	//because of method returning (though I seem to have fixed that, but I'm not sure anymore)
	protected Set<Tile.Type> nextTileType(int secondaryChanceInPercent, int ternaryChanceInPercent)
	{
		//Let us fix a wrong input for the user
		if (secondaryChanceInPercent + ternaryChanceInPercent > 100)
		{
			secondaryChanceInPercent = secondaryChanceInPercent % 100;
			ternaryChanceInPercent = 100 - secondaryChanceInPercent;
		}

		int tileLuckInPercent = Utility.randomInt(100);
		Set<Tile.Type> types;

		if (tileLuckInPercent < secondaryChanceInPercent)
		{
			//Two types (we simply choose one not to include in a set) = Secondary
			switch (Utility.randomInt(3))
			{
				case 0:
					types = EnumSet.of(Tile.Type.SQUARE, Tile.Type.CIRCLE);
					break;
				case 1:
					types = EnumSet.of(Tile.Type.SQUARE, Tile.Type.PLUS);
					break;
				case 2:
				default:
					types = EnumSet.of(Tile.Type.PLUS, Tile.Type.CIRCLE);
					break;
			}
		}
		else if (tileLuckInPercent < (ternaryChanceInPercent + secondaryChanceInPercent))
		{
			//Three types = Ternary piece
			types = EnumSet.of(Tile.Type.CIRCLE, Tile.Type.SQUARE, Tile.Type.PLUS);
		}
		else
		{
			//One type = Primary piece
			switch (Utility.randomInt(3))
			{
				case 0:
					types = EnumSet.of(Tile.Type.PLUS);
					break;
				case 1:
					types = EnumSet.of(Tile.Type.CIRCLE);
					break;
				case 2:
				default:
					types = EnumSet.of(Tile.Type.SQUARE);
					break;
			}
		}
		return types;
	}

	//Makes a next single Tile, Factory method
	protected Tile makeTile()
	{
		switch (getDifficulty())
		{
			case EASY:
				return new Tile(nextTileType(EASY_SECONDARY_PERCENT, EASY_TERNARY_PERCENT));
			case MEDIUM:
				return new Tile(nextTileType(MEDIUM_SECONDARY_PERCENT, MEDIUM_TERNARY_PERCENT));
			case HARD:
			default:
				return new Tile(nextTileType(HARD_SECONDARY_PERCENT, HARD_TERNARY_PERCENT));
		}
	}

	//Makes an empty Tile, Factory method
	@Deprecated
	protected Tile makeEmptyTile()
	{
		return new Tile(EnumSet.noneOf(Tile.Type.class));
	}

	//Generates and returns a new array of Tiles, needed by Field during its construction
	@Override
	public Tile[][] makeTiles()
	{
		//Creates a FieldTemplate from the Template Setting using factory method to be used during Field construction
		FieldTemplate template = makeFieldTemplate(getTemplate());

		//Uses other FieldBuilder methods to construct every single tile before returning them
		Tile[][] tiles = new Tile[getRows()][getColumns()];
		for (int row = 0; row < getRows(); row++)
		{
			for (int column = 0; column < getColumns(); column++)
			{
				if (template.usingTile(row, column))
				{
					//Template has decided to create a tile
					tiles[row][column] = makeTile();
				}
				else
				{
					//Template has decided not to create a tile
					//tiles[row][column] = makeEmptyTile();
					tiles[row][column] = null;
				}
			}
		}
		return tiles;
	}

	//The main access point, creates a new Field using this builder
	@Override
	public Field getField()
	{
		if (!isReady())
		{
			//There can be a default default implementation, subclass is free to do so.
			throw new FieldBuilderNotReadyException();
		}
		return new Field(this);
	}
}
