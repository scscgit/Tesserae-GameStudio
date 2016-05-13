package sk.tuke.gamestudio.game.tesserae.jsf;

import org.primefaces.context.RequestContext;
import sk.tuke.gamestudio.game.tesserae.Tesserae;
import sk.tuke.gamestudio.game.tesserae.core.field.Direction;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.service.GameServices;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

/**
 * Tesserae has many faces.
 * <p>
 * Created by Steve on 18.04.2016.
 */
@FacesComponent ("Tesserae")
public class TesseraeComponent extends UICommand
{
	//FavoriteService for the Interpreter
	@EJB
	private FavoriteGameDatabaseService favoriteService;

	@Inject
	private GameServices gameServices;

	@Inject
	FieldInstanceManager manager;

	@Inject
	private ThemeChooser themeChooser;

	private Theme theme;
	private Integer tileWidth;
	private Integer tileHeight;

	public Theme getTheme()
	{
		return theme;
	}
	public void setTheme(Theme theme)
	{
		if (theme == null)
		{
			//Failsafe Theme
			this.theme = ThemeChooser.DEFAULT_THEME;
		}
		else
		{
			this.theme = theme;
		}
	}

	//Returns true while the game is about to start, or while its being played
	private boolean isNotEndState(Field field)
	{
		return
			field == null
			||
			(
				field.getState() != Field.GameState.WON
				&&
				field.getState() != Field.GameState.LOST
			);
	}

	private void action(Field field, int row, int column)
	{
		Direction direction = field.getAllowedDirectionToCoordinates(row, column);

		//If the direction is a valid direction movement, current Tile gets moved
		if (direction != null)
		{
			field.move(direction);

			//Callback that does required tasks after the field changed its state
			//As of time of writing this, tasks are saving a new history state in the timeline and updating the score
			this.manager.fieldUpdatedCallback();
		}
		//Otherwise a new Tile gets selected
		else
		{
			field.selectTile(row, column);
		}
	}

	private void processParamsAndHandleAction(Field field, FacesContext context)
	{
		Field.GameState state = field.getState();

		if (state == Field.GameState.PLAYING)
		{
			try
			{
				String row = (String) context.getExternalContext().getRequestParameterMap().get("row");
				String column = (String) context.getExternalContext().getRequestParameterMap().get("column");
				action(field, Integer.parseInt(row), Integer.parseInt(column));

				GameServices gameServices = this.gameServices;
				if (gameServices != null)
				{
					if (state.equals(Field.GameState.WON) || state.equals(Field.GameState.LOST))
					{
						gameServices.saveScore(Tesserae.getGameStatic().getName(), field.getScore());
					}
				}
				else
				{
					throw new RuntimeException("GameServices is null");
				}

			}
			catch (NumberFormatException e)
			{
				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				//TODO replace by non-intrusive exception
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	//Component drawing as an HTML reply to the client
	@Override
	public void encodeAll(FacesContext context) throws IOException
	{
		//After uncommenting this, a wild weird button with the getValue().toString() text appears
		//super.encodeAll(context);

		//Updates theme attribute from the tag and loads respective textOnly setting
		Theme attributeTheme = (Theme) getAttributes().get("theme");

		//If there is no theme tag, users ThemeChooser theme (default usage for user-controlled theme)
		if (attributeTheme == null)
		{
			setTheme(this.themeChooser.getTheme());
		}
		else
		{
			setTheme(attributeTheme);
		}

		//Size of the Tile as displayed in HTML, optionally chosen by attributes
		try
		{
			tileWidth = Integer.parseInt((String) getAttributes().get("tileWidth"));
		}
		catch (Exception e)
		{
			tileWidth = null;
		}
		try
		{
			tileHeight = Integer.parseInt((String) getAttributes().get("tileHeight"));
		}
		catch (Exception e)
		{
			tileHeight = null;
		}

		//HTML output response to the client
		ResponseWriter writer = context.getResponseWriter();

		//Debug in case of problems
		if (favoriteService == null)
		{
			writer.write("Warning: favoriteService is null.<br/>");
		}
		if (manager == null)
		{
			writer.write("Warning: manager is null.<br/>");
		}
		if (theme == null)
		{
			writer.write("Warning: theme is null.<br/>");
		}

		//The builder for the interpreter is chosen during the creation of an instance of UI
		//manager = (FieldInstanceManager) getValue();
		if (!manager.isInterpreterAvailable())
		{
			manager.setInterpreter
				(
					new FieldInterpreter(manager, new SimpleFieldBuilder(Settings.SIMPLE_GAME), this.favoriteService)
				);
		}

		Field field = null;
		if (manager != null)
		{
			field = manager.getManagedField();
		}

		//Processes parameters before drawing a new Field
		if (field != null)
		{
			processParamsAndHandleAction(field, context);
		}

		//TextOnly mode draws a text-only version of the Field using textarea
		if (getTheme().isTextOnly())
		{
			writeTesseraeFieldTextOnly(field, writer, context);
		}
		//Otherwise draws a normal version of the Field
		else
		{
			writeTesseraeField(field, writer, context);
		}

		/*
		StringTokenizer tokenizer = new StringTokenizer(field.toString(), "\n");
		while (tokenizer.hasMoreElements())
		{
			writer.write("<br>");
			String row = tokenizer.nextElement().toString();
			writer.writeText(row, null);
			//debug test
			java.util.logging.Logger.getLogger(getClass().getName()).severe(row);
		}
		*/

		//Resizes the result to the current required size
		RequestContext.getCurrentInstance().execute("autosize($('#fieldTextArea'));");
	}

	//Returns an image representing a Tile
	//Format is "[1][2][3]" where Circle=1, Plus=2, Square=3
	private String getTileImage(Tile tile)
	{
		//Calculates the image
		String image;
		Set<Tile.Type> type = tile.getType();
		switch (type.size())
		{
			case 3:
				image = "123";
				break;
			case 2:
				if (type.contains(Tile.Type.CIRCLE) && type.contains(Tile.Type.PLUS))
				{
					image = "12";
				}
				else if (type.contains(Tile.Type.CIRCLE) && type.contains(Tile.Type.SQUARE))
				{
					image = "13";
				}
				else if (type.contains(Tile.Type.SQUARE) && type.contains(Tile.Type.PLUS))
				{
					image = "23";
				}
				else
				{
					throw new IllegalArgumentException("Wrong secondary Tile type");
				}
				break;
			case 1:
				if (type.contains(Tile.Type.CIRCLE))
				{
					image = "1";
				}
				else if (type.contains(Tile.Type.PLUS))
				{
					image = "2";
				}
				else if (type.contains(Tile.Type.SQUARE))
				{
					image = "3";
				}
				else
				{
					throw new IllegalArgumentException("Wrong primary Tile type");
				}
				break;
			case 0:
				image = "0";
				break;
			default:
				throw new IllegalArgumentException("Wrong Tile type size");
		}

		return String.format("resources/images/tesserae/themes/%s/%s.png", getTheme().getPathName(), image);
	}

	private String getURL(FacesContext context, String value)
	{
		return context.getExternalContext().getApplicationContextPath() + context.getViewRoot().getViewId() + value;
	}

	//Draws the Tesserae Field using the HTML and/or Faces components
	private void writeTesseraeField(Field field, ResponseWriter writer, FacesContext context) throws IOException
	{
		if (field == null)
		{
			writer.startElement("p", this);
			writer.writeAttribute("class", "informationalText", null);
			writer.writeText("Start the game please :-)", null);
			writer.endElement("p");
		}
		else if (field.getState().equals(Field.GameState.PAUSED))
		{
			writer.startElement("p", this);
			writer.writeAttribute("class", "informationalText", null);
			writer.write("Game postponed for later :-)");
			writer.endElement("p");
		}
		//Draws its content when the Field is available during gameplay or after the end
		else
		{
			//If the Field is already in the end state, displays a corresponding message
			if (!isNotEndState(field))
			{
				writer.startElement("p", this);
				writer.writeAttribute("class", "informationalText", null);
				if (field.getState().equals(Field.GameState.WON))
				{
					writer.write("Congratz, you've won!");
					writer.startElement("br", this);
					writer.endElement("br");
					writer.write("Score: " + field.getScore());
				}
				else if (field.getState().equals(Field.GameState.LOST))
				{
					writer.write("GG, you've lost.");
					writer.startElement("br", this);
					writer.endElement("br");
					writer.write("Score: " + field.getScore());
				}
				writer.endElement("p");
			}

			writer.startElement("table", this);

			for (int row = 0; row < field.getRows(); row++)
			{
				writer.startElement("tr", this);
				for (int column = 0; column < field.getColumns(); column++)
				{
					Tile tile = field.getTile(row, column);
					writer.startElement("td", this);

					//Draws a Tile only if it exists
					if (tile != null)
					{
						writeTesseraeTile(tile, row, column, field, writer, context);
					}

					writer.endElement("td");
				}
				writer.endElement("tr");
			}

			writer.endElement("table");
		}
	}

	private void writeTesseraeTile(Tile tile, int row, int column, Field field, ResponseWriter writer,
	                               FacesContext context) throws IOException
	{
		//Dispatches a style class to the table data cell based on the current Tile meaning
		if
			(
			field.getSelectedTile() != null &&
			row == field.getSelectedRow() &&
			column == field.getSelectedColumn()
			)
		{
			writer.writeAttribute("class", "tesseraeSelectedTile", null);
		}
		else if (field.isAllowedMovementTo(row, column))
		{
			writer.writeAttribute("class", "tesseraeAllowedTile", null);
		}

		//Empty tile will be a hyperlink so that tile can also jump to an empty tile
		//if (!tile.getType().isEmpty())
		//{
		writer.startElement("a", this);
		writer.writeAttribute("href",
		                      getURL(context, String.format("?row=%d&column=%d", row, column)),
		                      null);
		//}

		writer.startElement("img", this);
		writer.writeAttribute("src", getTileImage(tile), null);

		//Optionally sets tileWidth and tileHeight based on attributes
		if (tileWidth != null)
		{
			writer.writeAttribute("width", tileWidth, null);
		}
		if (tileHeight != null)
		{
			writer.writeAttribute("height", tileHeight, null);
		}

		writer.endElement("img");
		//if (!tile.getType().isEmpty())
		//{
		writer.endElement("a");
		//}
	}

	//Draws a text-only version of the Field using textarea
	private void writeTesseraeFieldTextOnly(Field field, ResponseWriter writer, FacesContext context) throws IOException
	{
		writer.startElement("textarea", this);
		writer.writeAttribute("id", "fieldTextArea", null);
		writer.writeAttribute("class", "fieldTextArea", null);
		writer.writeAttribute("readonly", "readonly", null);

		//Write a happy face if no field is available
		if (field == null)
		{
			writer.write(":-)");
		}
		else if (field.getState().equals(Field.GameState.PAUSED))
		{
			writer.write("Game postponed for later :-)");
		}
		//Draws its content when the Field is available during gameplay or after the end
		else
		{
			//If the Field is already in the end state, displays a corresponding message
			if (!isNotEndState(field))
			{
				if (field.getState().equals(Field.GameState.WON))
				{
					writer.write("Congratz, you've won!\nScore: " + field.getScore() + "\n\n");
				}
				else if (field.getState().equals(Field.GameState.LOST))
				{
					writer.write("GG, you've lost.\nScore: " + field.getScore() + "\n\n");
				}
			}

			writer.write(field.toString(manager.getFieldColor()));
		}

		writer.endElement("textarea");
	}

	//Does some magic I guess? But may not be needed.
	@Override
	public boolean getRendersChildren()
	{
		return true;
	}
}
