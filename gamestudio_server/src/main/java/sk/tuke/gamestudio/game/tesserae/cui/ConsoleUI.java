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

package sk.tuke.gamestudio.game.tesserae.cui;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.game.tesserae.Tesserae;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilderNoHistoryException;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.InterpreterException;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.support.Utility;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Basic gameplay availability from any old PC that does not support even as much as 256 colors, highly compatible.
 * <p/>
 * Responsibility of this class:
 * Provide console interface access to the game core for a user.
 * <p/>
 * Created by Steve on 22.2.2016.
 */
public class ConsoleUI implements FieldManager
{
	//Fields
	//Instance of an interpreter that is interpreting commands and building a Field
	private FieldInterpreter interpreter;
	//Managed Field instance
	private Field field;
	//Current Scanner, lazy initialization, use getScanner()
	private Scanner inScanner = null;
	//Current field color
	private ColorMode fieldColor = ColorMode.BLACK;
	//Database service
	private FavoriteGameDatabaseService service;
	//History
	private FieldHistoryRebuilder history;

	public ConsoleUI(FieldBuilder builder, FavoriteGameDatabaseService service)
	{
		this.service = service;
		//The builder for interpreter is chosen during creation of an instance of UI
		this.interpreter = new FieldInterpreter(this, builder, service);
		this.field = null;
	}

	private Scanner getScanner()
	{
		if (this.inScanner == null)
		{
			this.inScanner = new Scanner(System.in);
		}
		return this.inScanner;
	}

	//Returns true while the game is about to start, or while its being played
	private boolean isNotEndState()
	{
		return
			this.field == null
			||
			(
				this.field.getState() != Field.GameState.WON
				&&
				this.field.getState() != Field.GameState.LOST
			);
	}

	//Runs an interactive console interaction with a user
	public void runConsoleSession()
	{
		System.out.println("Welcome to the Tesserae. If you need <help>, just ask for it.");
		do
		{
			//Show the field
			if (this.field != null && this.field.getState().equals(Field.GameState.PLAYING))
			{
				System.out.println("\n" + this.field.toString(this.fieldColor));
			}

			//Interpret command
			System.out
				.print("Enter your command" + (Utility.randomInt(8) == 0 ? ", master \uD83D\uDE08" : "") + ":\n> ");
			String command = getScanner().nextLine();
			System.out.println(interpretCommand(command));
			System.out.println();
		}
		while (isNotEndState());

		//Handling end game state and utilizing Favorite Game Service
		if (this.field != null)
		{
			handleEndGameState();

			//Favorites service integration, query for the current state of game favoriteness
			List<FavoriteGameEntity> favoriteGames = loadFavoriteGames(getPlayer());

			//If the user did not like the game yet, let's annoy him by offering him that option
			if (favoriteGames != null)
			{
				Game currentGame = Tesserae.getGame();
				if (!isGameAlreadyFavorite(currentGame, favoriteGames))
				{
					System.out.println("Did you like this game? Make it your favorite by saying \"yes please\"!");
					String command = getScanner().nextLine().toLowerCase();
					if (command.equals("yes please") || command.equals("yes pls") || command.equals("yes please!") ||
					    command.equals("yes pls!"))
					{
						markGameAsFavorite(currentGame);
					}
					else if (command.equals("yes"))
					{
						System.out.println("Sorry, yes was not enough :(");
					}
					else
					{
						System.out.println("Too bad :( We hope you change your opinion next time.");
					}
				}
			}
		}
	}

	protected FavoriteGameDatabaseService getFavoriteService() throws FavoriteException
	{
		if (this.service != null)
		{
			return this.service;
		}
		else
		{
			throw new FavoriteException("There is no Favorites Service available in this game.");
		}
	}

	private boolean isGameAlreadyFavorite(Game currentGame, List<FavoriteGameEntity> favoriteGames)
	{
		for (FavoriteGameEntity favoriteGame : favoriteGames)
		{
			if (favoriteGame.getGame().equals(currentGame.getName()))
			{
				return true;
			}
		}
		return false;
	}

	//Loads all favorite games of a player, or null and prints error if no database connection is possible
	private List<FavoriteGameEntity> loadFavoriteGames(String player)
	{
		List<FavoriteGameEntity> favoriteGames = null;
		try
		{
			FavoriteGameDatabaseService favoriteGameService = getFavoriteService();
			favoriteGames = favoriteGameService.getFavorites(player);
		}
		catch (FavoriteException e)
		{
			System.out.println("I would ask you to make this game one of your favorites,\n" +
			                   "but the database just doesn't work :(\n" + e.getMessage());
		}
		return favoriteGames;
	}

	private void handleEndGameState()
	{
		if (this.field.getState().equals(Field.GameState.WON))
		{
			System.out.println("\n" + this.field);
			System.out.println("Congratz, you've won!");
		}
		else if (this.field.getState().equals(Field.GameState.LOST))
		{
			System.out.println("\n" + this.field);
			System.out.println("GG, you've lost.");
		}
	}

	private void markGameAsFavorite(Game currentGame)
	{
		try
		{
			FavoriteGameDatabaseService favoriteGameService = getFavoriteService();
			favoriteGameService.addFavorite(getPlayer(), currentGame.toString());
		}
		catch (FavoriteException e)
		{
			System.out.println("Sorry, there was a problem with the database:\n" +
			                   e.getMessage() +
			                   "\nTry to make the game your favorite again next time.");
			//switch (e.getErrorCode())
		}
	}

	//Interprets a command from the user, returning the result in a user-friendly format
	public String interpretCommand(String command)
	{
		try
		{
			String result = this.interpreter.interpret(command);
			return "Result> " + (result.isEmpty() ? "OK" : result);
		}
		catch (InterpreterException e)
		{
			return "Error# " + e.getMessage();
		}
	}

	@Override
	public Field getManagedField()
	{
		return this.field;
	}

	@Override
	public void setManagedField(Field field)
	{
		System.out.println("New field has been set.");
		this.field = field;
		//Prepares a new timeline for the Field and saves the first position on top of the "stack"
		this.history = new FieldHistoryRebuilder(field.getRows(), field.getColumns());
		this.history.saveState(field);
	}

	//Console UI does not even let the player choose his own name, it is assumed implicitly
	@Override
	public String getPlayer()
	{
		String property = System.getProperty("user.name");

		//I am paranoid so I don't even trust system methods. I also don't want conflict with users called like "None".
		if (property == null)
		{
			return "";
		}

		return property;
	}

	//Management of the current color setting
	@Override
	public ColorMode getFieldColor()
	{
		return this.fieldColor;
	}
	@Override
	public void setFieldColor(ColorMode color)
	{
		this.fieldColor = color;
	}

	//Goes forwards in time
	@Override
	public void fieldUpdatedCallback()
	{
		this.history.saveState(this.field);
	}
	//Goes back in time
	@Override
	public void goBackInTime() throws FieldHistoryRebuilderNoHistoryException
	{
		this.field = this.history.getField();
	}
	@Override
	public LinkedList<Field> getTimeline()
	{
		return this.history.getTimeline();
	}
}
