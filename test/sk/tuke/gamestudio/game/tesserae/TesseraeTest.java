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

package sk.tuke.gamestudio.game.tesserae;

import junit.framework.TestCase;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.template.FieldTemplate;
import sk.tuke.gamestudio.game.tesserae.cui.FieldManager;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.Interpreter;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.InterpreterException;
import sk.tuke.gamestudio.test.NumberGenerator;
import sk.tuke.gamestudio.test.RandomNumberGenerator;

/**
 * Testing the tester, but also testing the game.
 * <p/>
 * Created by Steve on 1.3.2016.
 */
public class TesseraeTest extends TestCase
{
	private class FieldBox
	{
		public int rows, columns;
		public Field.DifficultySetting difficulty;
		public FieldTemplate.Setting template;
		public Field field;
	}

	//Testing dimensions
	private static final int MIN_FIELD_ROWS = 1;
	private static final int MAX_FIELD_ROWS = 150;
	private static final int MIN_FIELD_COLUMNS = 1;
	private static final int MAX_FIELD_COLUMNS = 26;

	//Objects
	private NumberGenerator numberGenerator = null;

	public void testField()
	{
		for (int i = 0; i < 100; i++)
		{
			FieldBox box = randomField();
			Field field = box.field;
			System.out.println(box.field);

			//Selection test
			int row, column;
			int timeout = box.rows * box.columns;

			//We use a simple Field builder ad-hoc to check for correctness of a position before clicking
			do
			{
				row = nextInt(box.rows);
				column = nextInt(box.columns);
				timeout--;
			}
			while (timeout > 0 && !new SimpleFieldBuilder(box.rows, box.columns).makeFieldTemplate(box.template)
			                                                                    .usingTile(row, column));

			//When we don't get lucky with our testing sample, we just skip the sample
			if (timeout == 0)
			{
				System.out.println("TIMEOUT happened");
				continue;
			}

			field.selectTile(row, column);

			/*try
			{

			}
			catch (Exception e)
			{
				for(int r=0; r<field.getRows(); r++)
				{
					for(int c=0; c<field.getColumns(); c++)
					{
						System.out.println(r+" "+c+" "+field.getTile(r,c));
					}
				}
				System.out.println("PROBLEM<"+field.getTile(row,column)+">");
				throw new RuntimeException(e);
			}*/

			try
			{
				assert field.getSelectedColumn() == column;
				assert field.getSelectedRow() == row;
				if (box.rows >= 3 && box.columns >= 3)
				{
					assert field.isSolvable();
				}
				if (box.rows >= 3 && box.columns >= 3)
				{
					assert !field.isSolved();
				}
			}
			catch (AssertionError e)
			{
				System.out.println(e.getMessage());
				System.out.println(field.toString());
				throw new RuntimeException(e);
			}

			System.out.println("Test " + i + 1 + " OK.");
		}
		System.out.println();
	}

	public int nextRowsInt()
	{
		return nextInt(MAX_FIELD_ROWS - MIN_FIELD_ROWS) + MIN_FIELD_ROWS;
	}

	public int nextColumnsInt()
	{
		return nextInt(MAX_FIELD_COLUMNS - MIN_FIELD_COLUMNS) + MIN_FIELD_COLUMNS;
	}

	//Gets a next int from the current number generator, initializes it to a random one if needed
	public int nextInt(int max)
	{
		if (this.numberGenerator == null)
		{
			this.numberGenerator = new RandomNumberGenerator();
		}
		return this.numberGenerator.nextInt(max);
	}

	public int nextInt(int min, int max)
	{
		if (this.numberGenerator == null)
		{
			this.numberGenerator = new RandomNumberGenerator();
		}
		return this.numberGenerator.nextInt(min, max);
	}

	//Chooses a random field
	public FieldBox randomField()
	{
		//Variables are inside a box
		FieldBox box = new FieldBox();

		//Choosing attributes
		box.rows = nextRowsInt();
		box.columns = nextColumnsInt();

		switch (nextInt(3))
		{
			case 0:
				box.difficulty = Field.DifficultySetting.EASY;
				break;
			case 1:
				box.difficulty = Field.DifficultySetting.MEDIUM;
				break;
			case 2:
				box.difficulty = Field.DifficultySetting.HARD;
				break;
			default:
				throw new RuntimeException("Unknown difficulty");
		}

		switch (nextInt(3))
		{
			case 0:
				box.template = FieldTemplate.Setting.ELLIPSE;
				break;
			case 1:
				box.template = FieldTemplate.Setting.RECTANGLE;
				break;
			case 2:
				box.template = FieldTemplate.Setting.RING;
				break;
			default:
				throw new RuntimeException("Unknown template");
		}

		//Field generation
		switch (nextInt(2))
		{
			case 0:
				box.field = setupField(box.rows, box.columns, box.difficulty, box.template);
				return box;
			case 1:
				box.field = buildField(box.rows, box.columns, box.difficulty, box.template);
				return box;
			default:
				throw new RuntimeException("Unknown field generation strategy");
		}
	}

	public Field setupField(int rows, int columns, Field.DifficultySetting difficulty, FieldTemplate.Setting template)
	{
		return new Field(new SimpleFieldBuilder(new Settings(rows, columns, difficulty, template)));
	}
	public Field buildField(int rows, int columns, Field.DifficultySetting difficulty, FieldTemplate.Setting template)
	{
		FieldBuilder fieldBuilder = new SimpleFieldBuilder();
		fieldBuilder.setSize(rows, columns);
		fieldBuilder.setDifficulty(difficulty);
		fieldBuilder.setTemplate(template);
		return fieldBuilder.getField();
	}

	public void testInterpreterSimpleRequests()
	{
		//The test user name should be usable everywhere as all classes actually have to go there to ask for user name
		FieldManager manager = Field.createVirtualManager("testUserName");

		//ConsoleUI console = new ConsoleUI();
		Interpreter interpreter = new FieldInterpreter(manager, new SimpleFieldBuilder(), null);

		String[] commands = new String[]
			{
				"Help",
				"seT DIFfiCUlTy eAsy",
				"set difficulty medium",
				"set difficulty hard",
				"set size 3 9",
				"set size 5 5",
				"set size 100 100",
				"set size 9 3",
				"set template ellipse",
				"set template plus",
				"set template rectangle",
				"start",
				"select a 3",
				"stop"
			};
		for (String command : commands)
		{
			try
			{
				System.out.println("> " + command);
				System.out.println(interpreter.interpret(command));
				System.out.println(manager.getManagedField());
			}
			catch (InterpreterException e)
			{
				throw new RuntimeException("Assertion problem", e);
				//System.out.println(e.getMessage());
			}
		}
	}
}
