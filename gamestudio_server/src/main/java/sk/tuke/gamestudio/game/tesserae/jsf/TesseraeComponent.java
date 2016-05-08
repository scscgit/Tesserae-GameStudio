package sk.tuke.gamestudio.game.tesserae.jsf;

import org.primefaces.context.RequestContext;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
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
	@EJB
	private FavoriteGameDatabaseService favoriteService;

	@Inject
	FieldInstanceManager manager;

	@Inject
	private ThemeChooser themeChooser;

	private Theme theme;

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

	//Component drawing as an HTML reply to the client
	@Override
	public void encodeAll(FacesContext context) throws IOException
	{
		//After uncommenting this, a wild weird button with the getValue().toString() text appears
		//super.encodeAll(context);

		//Updates theme attribute from the tag and loads respective textOnly setting
		//setTheme((Theme) getAttributes().get("theme"));
		setTheme(this.themeChooser.getTheme());

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
			writer.writeText("Start the game please :-)", null);
			writer.endElement("p");
		}
		else if (field.getState().equals(Field.GameState.PAUSED))
		{
			writer.startElement("p", this);
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
				}
				else if (field.getState().equals(Field.GameState.LOST))
				{
					writer.write("GG, you've lost.");
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
						//Empty tile will not be a hyperlink
						if (!tile.getType().isEmpty())
						{
							writer.startElement("a", this);
							writer.writeAttribute("href",
							                      getURL(context, String.format("?row=%d&column=%d", row, column)),
							                      null);
						}

						writer.startElement("img", this);
						writer.writeAttribute("src", getTileImage(tile), null);
						writer.endElement("img");
						if (!tile.getType().isEmpty())
						{
							writer.endElement("a");
						}
					}

					writer.endElement("td");
				}
				writer.endElement("tr");
			}

			writer.endElement("table");
		}
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
					writer.write("Congratz, you've won!\n\n");
				}
				else if (field.getState().equals(Field.GameState.LOST))
				{
					writer.write("GG, you've lost.\n\n");
				}
			}

			writer.write(field.toString(manager.getFieldColor()));
		}

		writer.endElement("textarea");
	}

	//No longer used: the service reference flow was expected to be in an opposite direction
	@Deprecated
	public FavoriteGameDatabaseService getFavoriteService()
	{
		return this.favoriteService;
	}

	//Does some magic I guess? But may not be needed.
	@Override
	public boolean getRendersChildren()
	{
		return true;
	}
}
