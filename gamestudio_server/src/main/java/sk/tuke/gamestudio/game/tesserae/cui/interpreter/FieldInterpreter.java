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

package sk.tuke.gamestudio.game.tesserae.cui.interpreter;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.game.tesserae.Tesserae;
import sk.tuke.gamestudio.game.tesserae.core.field.Direction;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilderNoHistoryException;
import sk.tuke.gamestudio.game.tesserae.core.field.template.FieldTemplate;
import sk.tuke.gamestudio.game.tesserae.core.tile.TileCannotMoveException;
import sk.tuke.gamestudio.game.tesserae.cui.ColorMode;
import sk.tuke.gamestudio.game.tesserae.cui.FieldManager;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.support.Utility;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom language interpreter for working with a Field of a FieldManager during the gameplay.
 * Stores and uses a FieldBuilder to generate new Fields.
 * Invokes callback on a class implementing FieldManager when any Field manipulation gets requested as a command.
 * <p/>
 * Responsibility of this class:
 * Two-way interaction with user in matters of direct gameplay queries.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class FieldInterpreter extends AbstractInterpreter implements Serializable
{
	//Constants
	private static final Pattern TILE_SELECT_ROW_REGEX = Pattern.compile("([0-9]+)");
	private static final Pattern TILE_SELECT_COLUMN_REGEX = Pattern.compile("([a-z]+)");

	//Objects
	//Manager that manages the field
	private FieldManager manager;
	//Builder used to systematically set up the plan for a Field before starting it
	private FieldBuilder builder;
	//Database service
	private FavoriteGameDatabaseService service;

	/**
	 * @param manager
	 * 	Manager of a Field representing the current game.
	 * @param builder
	 * 	FieldBuilder that will be responsible for creating new Fields when requested by commands.
	 * @param service
	 * 	Helper service that connects to a favorite games database.
	 */
	public FieldInterpreter(FieldManager manager, FieldBuilder builder, FavoriteGameDatabaseService service)
	{
		this.manager = manager;
		this.builder = builder;
		this.service = service;
	}

	protected FieldBuilder getBuilder()
	{
		return this.builder;
	}

	protected FieldManager getManager()
	{
		return this.manager;
	}

	//Field Interpreter interprets everything for a Tesserae game
	protected Game getGame()
	{
		return Tesserae.getGame();
	}

	protected boolean isGameRunning()
	{
		return getManager().getManagedField() != null
		       &&
		       getManager().getManagedField().getState() == Field.GameState.PLAYING;
	}

	protected boolean isGameFinished()
	{
		return getManager().getManagedField() != null
		       &&
		       (
			       getManager().getManagedField().getState() == Field.GameState.WON
			       ||
			       getManager().getManagedField().getState() == Field.GameState.LOST
		       );
	}

	protected FavoriteGameDatabaseService getFavoriteService() throws InterpreterException
	{
		if (this.service != null)
		{
			return this.service;
		}
		else
		{
			throw new InterpreterException("There is no Favorites Service available in this game.");
		}
	}

	//Main execute phase of a new command, a starting point for the chain of commands
	protected String execute() throws InterpreterException
	{
		assertNext();
		String cmd = remove();

		if (cmd.equals("start") || cmd.equals("run") || cmd.equals("go") || cmd.equals("hurry"))
		{
			executeStart();
		}
		else if (cmd.equals("pause") || cmd.equals("wait") || cmd.equals("stall") || cmd.equals("await"))
		{
			executePause();
		}
		else if (cmd.equals("select") || cmd.equals("check") || cmd.equals("find") || cmd.equals("use") ||
		         cmd.equals("manage") || cmd.equals("affect") || cmd.equals("influence") || cmd.equals("sel"))
		{
			executeSelect();
		}
		else if (cmd.equals("move") || cmd.equals("m") || cmd.equals("movement") || cmd.equals("transfer") ||
		         cmd.equals("affect") || cmd.equals("influence"))
		{
			executeMove();
		}
		else if (cmd.equals("back") || cmd.equals("backwards") || cmd.equals("return") || cmd.equals("recall") ||
		         cmd.equals("timeskip") || cmd.equals("timetravel") || cmd.equals("timejump") || cmd.equals("restore"))
		{
			executeBack();
		}
		else if (cmd.equals("history") || cmd.equals("timeline") || cmd.equals("timeywimey") || cmd.equals("past") ||
		         cmd.equals("timespace") || cmd.equals("previous") || cmd.equals("previously") || cmd.equals("old"))
		{
			return executeHistory();
		}
		else if (cmd.equals("forfeit") || cmd.equals("end") || cmd.equals("stop") || cmd.equals("quit") ||
		         cmd.equals("lose") || cmd.equals("exit"))
		{
			executeForfeit();
		}
		else if (cmd.equals("set") || cmd.equals("change") || cmd.equals("switch") || cmd.equals("modify"))
		{
			executeSet();
		}
		else if (cmd.equals("help") || cmd.equals("?") || cmd.equals("what") || cmd.equals("halp") || cmd.equals("how"))
		{
			return executeHelp();
		}
		else if (cmd.equals("favorite") || cmd.equals("favourite") ||
		         cmd.equals("favorites") || cmd.equals("favourites") || cmd.equals("fav") || cmd.equals("like"))
		{
			return executeFavorite();
		}
		/*else if (cmd.equals("display") || cmd.equals("color") || cmd.equals("show") || cmd.equals("text") ||
		         cmd.equals("visual") || cmd.equals("graphics") || cmd.equals("format"))
		{
			executeDisplay();
		}*/
		else
		{
			throw new InterpreterInvalidInstructionsException("Cannot understand command " + cmd + ".");
		}

		//This implementation does not use result information when everything goes OK (methods just returned void)
		return "";
	}

	protected void executePause() throws InterpreterException
	{
		//If the Field exists and the game is currently running, pauses it
		Field currentField = getManager().getManagedField();
		if (currentField != null && currentField.getState().equals(Field.GameState.PLAYING))
		{
			//Tries to pause the game, returning error if the method returned false
			if (!currentField.pause())
			{
				throw new InterpreterException("Could not pause the game.");
			}
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"There is no running game that can be paused.");
		}
	}

	protected void executeDisplay() throws InterpreterException
	{
		assertNext();
		String cmd = remove();

		if (cmd.equals("color") || cmd.equals("colors") || cmd.equals("colorful") || cmd.equals("nice") ||
		    cmd.equals("colored") || cmd.equals("beautiful"))
		{
			getManager().setFieldColor(ColorMode.COLORS);
		}
		else if (cmd.equals("text") || cmd.equals("textonly") || cmd.equals("default") || cmd.equals("simple"))
		{
			getManager().setFieldColor(ColorMode.COLORED_TEXT);
		}
		else if (cmd.equals("black") || cmd.equals("nocolor") || cmd.equals("plain") || cmd.equals("oldschool"))
		{
			getManager().setFieldColor(ColorMode.BLACK);
		}
		else
		{
			throw new InterpreterInvalidInstructionsException("Display format " + cmd + " could not be understood.");
		}
	}

	//Selects a Tile, gives player a visual feedback of all possible movements
	protected void executeSelect() throws InterpreterException
	{
		if (isGameRunning())
		{
			String rowString = null;
			String columnString = null;

			//Load row and column from user, any order is fine
			do
			{
				assertNext();
				String cmd = remove();

				Matcher rowMatcher = TILE_SELECT_ROW_REGEX.matcher(cmd);
				Matcher columnMatcher = TILE_SELECT_COLUMN_REGEX.matcher(cmd);

				if (rowMatcher.matches())
				{
					rowString = rowMatcher.group(1);
				}
				if (columnMatcher.matches())
				{
					columnString = columnMatcher.group(1);
				}
			}
			while (rowString == null || columnString == null);

			if (columnString.length() > 1)
			{
				throw new InterpreterInvalidInstructionsException(
					"Two and more characters of a column are not supported in this interpreter yet. Sorry.");
			}

			//Convert row and column to integers, converted not to start from 0
			int row = Integer.valueOf(rowString) - 1;
			int column = columnString.charAt(0) - 'a';

			//Select the Tile
			try
			{
				getManager().getManagedField().selectTile(row, column);
			}
			catch (Field.FieldInvalidTileSelectionException e)
			{
				throw new InterpreterInvalidInstructionsException(
					"Cannot select a tile on row " + (row + 1) + ", column " + (column + 1) + ".");
			}
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"You cannot select a Tile before you have a Tile. Start the game first.");
		}
	}

	//Moves a (selected) Tile
	protected void executeMove() throws InterpreterException
	{
		if (isGameRunning())
		{
			assertNext();

			Direction.HorizontalDirection horizontalDirection = Direction.HorizontalDirection.NONE;
			Direction.VerticalDirection verticalDirection = Direction.VerticalDirection.NONE;

			//Load all inputs until there is none left
			while (hasNext())
			{
				String cmd = remove();
				if (cmd.equals("up") || cmd.equals("top") || cmd.equals("upwards") || cmd.equals("u"))
				{
					verticalDirection = Direction.VerticalDirection.UP;
				}
				else if (cmd.equals("down") || cmd.equals("bottom") || cmd.equals("downwards") || cmd.equals("d"))
				{
					verticalDirection = Direction.VerticalDirection.DOWN;
				}
				if (cmd.equals("left") || cmd.equals("leftwise") || cmd.equals("l"))
				{
					horizontalDirection = Direction.HorizontalDirection.LEFT;
				}
				else if (cmd.equals("right") || cmd.equals("rightwise") || cmd.equals("r"))
				{
					horizontalDirection = Direction.HorizontalDirection.RIGHT;
				}
			}

			//Move the tile
			try
			{
				getManager().getManagedField().move(Direction.getDirection(horizontalDirection, verticalDirection));
				//Callback that does required tasks after the field changed its state
				//(As of time of writing this, the only task is saving a new history state in the timeline)
				getManager().fieldUpdatedCallback();
			}
			catch (TileCannotMoveException e)
			{
				throw new InterpreterException("Problem happened during request to move a Tile.\n" + e.getMessage());
			}
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"You cannot move a Tile before you have a Tile. Hurry up and start the game first.");
		}
	}

	protected void executeBack() throws InterpreterException
	{
		if (isGameRunning())
		{
			try
			{
				getManager().goBackInTime();
			}
			catch (FieldHistoryRebuilderNoHistoryException e)
			{
				throw new InterpreterInvalidInstructionsException(
					"Cannot go back in time: " + e.getMessage());
			}
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"You cannot go back to a game played in your previous life. Start a game first.");
		}
	}

	private String executeHistory() throws InterpreterException
	{
		ColorMode color = getManager().getFieldColor();
		if (isGameRunning() || isGameFinished())
		{
			int i = 0;
			StringBuilder timelineString = new StringBuilder();
			for (Field field : getManager().getTimeline())
			{
				timelineString.append("Move ").append(i++).append(":\n");
				if (field == null)
				{
					throw new InterpreterException("There is a general problem with the timeline.");
				}

				timelineString.append(field.toString(color));
			}
			return timelineString.toString();
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"You cannot view a history of a game that is not being currently played.");
		}
	}

	//Player decided to stop a running game, therefore losing it
	protected void executeForfeit() throws InterpreterException
	{
		if (isGameRunning())
		{
			getManager().getManagedField().forfeitGame();
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"You cannot end a game that is not being played.");
		}
	}

	//Starts a new game, but only if the player is not in the middle of one already
	protected void executeStart() throws InterpreterException
	{
		//If the Field exists and is paused, unpauses it
		Field currentField = getManager().getManagedField();
		if (currentField != null && currentField.getState().equals(Field.GameState.PAUSED))
		{
			//Tries to unpause the game, returning error if the method returned false
			if (!currentField.unpause())
			{
				throw new InterpreterException("Could not un-pause the game.");
			}
		}
		//If the Field does not exist, it tries to get created
		else if (getBuilder().isReady())
		{
			if
				(
				getManager().getManagedField() == null
				||
				getManager().getManagedField().getState() != Field.GameState.PLAYING
				)
			{
				getManager().setManagedField(getBuilder().getField());
			}
			else
			{
				throw new InterpreterInvalidInstructionsException(
					"Cannot start a game while one is in the progress.\nIf it's too hard for ya, you can always forfeit.");
			}
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"Cannot find field from static context.\nIn other words, set size, template and difficulty first.");
		}
	}

	protected String executeHelp()
	{
		return
			"* List of supported commands *\n" +
			"Start = starts a new game with the chosen settings\n" +
			"Pause = temporarily hides a running game, but does not quit and allows it to be re-started again\n" +
			"Forfeit = quits a running game\n" +
			"Select {row as number | column as letter} = selects a Tile\n" +
			"Move {up|down|left|right} = moves a selected Tile to a direction\n" +
			"Back = returns the game to a previous state after you've done a mistake\n" +
			"History = shows an entire history of your current gameplay\n" +
			"Set ->\n" +
			"set difficulty [easy|medium|hard] = chooses a difficulty of the field\n" +
			"set size <rows> <columns> = chooses a size of the field\n" +
			"set template [rectangle|ellipse|ring|plus] = chooses a template of the field\n" +
			"Favorite ->\n" +
			"favorite show = lists all favorite games of a current user, YOU!\n" +
			"favorite add = adds this game as your favorite game\n" +
			"favorite remove = removes this game from your list of favorite games\n" +
			//"Display ->\n" +
			//"display color = field will be drawn using colors\n" +
			//"display text = field will be drawn with tile types as a colored text\n" +
			//"display black = field will be drawn only as a default console black text\n" +
			"Help = shows this help message\n" +
			"\n @Enjoy playing our game!@";
	}

	protected String executeFavorite() throws InterpreterException
	{
		if (getManager().getPlayer() == null)
		{
			throw new InterpreterInvalidInstructionsException(
				"There is no Player logged in.\nPlease consider registering yourself in our Gamestudio.");
		}

		if (hasNext())
		{
			assertNext();
			String cmd = remove();

			if (cmd.equals("add") || cmd.equals("mark") || cmd.equals("make") || cmd.equals("game"))
			{
				addFavorite();
				return "";
			}
			else if (cmd.equals("remove") || cmd.equals("stop") || cmd.equals("cancel") || cmd.equals("delete") ||
			         cmd.equals("no") || cmd.equals("end"))
			{
				removeFavorite();
				return "";
			}
			else if (!cmd.equals("show"))
			{
				throw new InterpreterInvalidInstructionsException(
					"Could not understand the request with respect to favorite game.");
			}
			else
			{
				//Returns the message with the list of all favorite games
				return showFavorite();
			}
		}
		else
		{
			//Returns the message with the list of all favorite games, command show, implicitly
			return showFavorite();
		}
	}

	//Handles all problems by throwing exception and returns a guaranteed working list of Favorite Games
	private List<FavoriteGameEntity> getWorkingFavoriteGames() throws InterpreterException
	{
		FavoriteGameDatabaseService favoriteService = getFavoriteService();
		List<FavoriteGameEntity> favorites;
		if (favoriteService == null)
		{
			throw new InterpreterException("Could not get a Database Service for favorite games.");
		}
		try
		{
			favorites = favoriteService.getFavorites(getManager().getPlayer());
			if (favorites == null)
			{
				throw new InterpreterException("Database service for favorite games returned invalid list.");
			}
			return favorites;
		}
		catch (FavoriteException e)
		{
			throw new InterpreterException(
				"There was a problem with the Database for favorite games." + e.getMessage(), e);
		}
	}

	//Shows the list of favorite games
	private String showFavorite() throws InterpreterException
	{
		//Loads all favorite games of a current user, throwing all exceptions upwards
		List<FavoriteGameEntity> favorites = getWorkingFavoriteGames();

		//If connection succeeds, we print the list to the user
		if (favorites.size() == 0)
		{
			return getManager().getPlayer() + ", you don't have any games marked as favorite :(";
		}
		else
		{
			StringBuilder message = new StringBuilder();
			message.append(getManager().getPlayer()).append(", this is the list of your favorite games:").append("\n");
			for (FavoriteGameEntity game : favorites)
			{
				message.append("Game ").append(game.getGame()).append(
					", favorite since ").append(Utility.formatDate(game.getChosenOn()));
			}
			return message.toString();
		}
	}

	private void addFavorite() throws InterpreterException
	{
		//Loads all favorite games of a current user, throwing all exceptions upwards
		List<FavoriteGameEntity> favorites = getWorkingFavoriteGames();
		Game game = getGame();

		//Checks for duplicates
		for (FavoriteGameEntity favorite : favorites)
		{
			if (favorite.getGame().equals(game.getName()))
			{
				if (favorite.getChosenOn() == null)
				{
					throw new InterpreterException(
						favorite.getId() + "wow, a null date... this is a serious problem y'know");
				}
				throw new InterpreterException(
					"We understand you really like this game, but seriously,\nyou have already marked this game as your favorite on " +
					Utility.formatDate(favorite.getChosenOn()) + ".");
			}
		}

		FavoriteGameDatabaseService favoriteService = getFavoriteService();
		if (favoriteService == null)
		{
			throw new InterpreterException("Could not get a Database Service for favorite games.");
		}
		try
		{
			favoriteService.addFavorite(getManager().getPlayer(), getGame().toString());
		}
		catch (FavoriteException e)
		{
			throw new InterpreterException(
				"There was a problem the during attempt at adding this game into your favorite games list.", e);
		}
	}

	private void removeFavorite() throws InterpreterException
	{
		//Loads all favorite games of a current user, throwing all exceptions upwards
		List<FavoriteGameEntity> favorites = getWorkingFavoriteGames();
		Game game = getGame();

		//Checks for duplicates
		boolean gameNotFound = false;
		for (FavoriteGameEntity favorite : favorites)
		{
			if (favorite.getGame().equals(game.getName()))
			{
				gameNotFound = true;
			}
		}

		if (!gameNotFound)
		{
			throw new InterpreterException(
				"You did not mark this game as your favorite yet.");
		}

		FavoriteGameDatabaseService favoriteService = getFavoriteService();
		if (favoriteService == null)
		{
			throw new InterpreterException("Could not get a Database Service for favorite games.");
		}
		try
		{
			favoriteService.removeFavorite(getManager().getPlayer(), game.toString());
		}
		catch (FavoriteException e)
		{
			throw new InterpreterException(
				"There was a problem the during attempt at removing this game from your favorite games list.", e);
		}
	}

	//Set command is meant to set some new property
	protected void executeSet() throws InterpreterException
	{
		assertNext();
		String cmd = remove();

		if (cmd.equals("difficulty") || cmd.equals("diff"))
		{
			executeDifficulty();
		}
		else if (cmd.equals("size") || cmd.equals("dimensions"))
		{
			executeSize();
		}
		else if (cmd.equals("template") || cmd.equals("temp") || cmd.equals("shape") || cmd.equals("type"))
		{
			executeTemplate();
		}
		/*else if( cmd.equals("preset"))
		{
			executePreset();
		}*/
		else
		{
			throw new InterpreterInvalidInstructionsException("Set command does not understand <" + cmd + ">.");
		}
	}

	protected void executeSize() throws InterpreterException
	{
		//Read new size parameters
		assertNext();
		String rows = remove();
		assertNext();
		String columns = remove();

		//Apply new size to a builder
		try
		{
			getBuilder().setSize(Integer.valueOf(rows), Integer.valueOf(columns));
		}
		catch (NumberFormatException e)
		{
			throw new InterpreterException("Wrong size format entered.\n" + e.getMessage(), e);
		}
	}

	protected void executeTemplate() throws InterpreterException
	{
		assertNext();
		String cmd = remove();

		//Some people may use better-known words like square or circle, even though sizes are not equal, make em happy
		if (cmd.equals("rectangle") || cmd.equals("square"))
		{
			getBuilder().setTemplate(FieldTemplate.Setting.RECTANGLE);
		}
		else if (cmd.equals("ellipse") || cmd.equals("circle"))
		{
			getBuilder().setTemplate(FieldTemplate.Setting.ELLIPSE);
		}
		else if (cmd.equals("ring"))
		{
			getBuilder().setTemplate(FieldTemplate.Setting.RING);
		}
		else if (cmd.equals("plus") || cmd.equals("cross"))
		{
			getBuilder().setTemplate(FieldTemplate.Setting.PLUS);
		}
		else
		{
			throw new InterpreterInvalidInstructionsException(
				"Set template command does not understand <" + cmd + ">.");
		}
	}

	protected void executeDifficulty() throws InterpreterException
	{
		assertNext();
		String cmd = remove();

		if (cmd.equals("easy") || cmd.equals("ez"))
		{
			getBuilder().setDifficulty(Field.DifficultySetting.EASY);
		}
		else if (cmd.equals("medium") || cmd.equals("med"))
		{
			getBuilder().setDifficulty(Field.DifficultySetting.MEDIUM);
		}
		else if (cmd.equals("hard") || cmd.equals("difficult"))
		{
			getBuilder().setDifficulty(Field.DifficultySetting.HARD);
		}
		else //todo custom difficulty?
		{
			throw new InterpreterInvalidInstructionsException(
				"Set difficulty command does not understand <" + cmd + ">.");
		}
	}
}
