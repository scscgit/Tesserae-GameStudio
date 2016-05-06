package sk.tuke.gamestudio.game.tesserae.jsf;

import org.primefaces.component.dnd.Draggable;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;
import java.io.IOException;

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

	private boolean textOnly;

	public boolean isTextOnly()
	{
		return textOnly;
	}
	public void setTextOnly(boolean textOnly)
	{
		this.textOnly = textOnly;
	}

	@Override
	public void encodeAll(FacesContext context) throws IOException
	{
		//After uncommenting this, a wild weird button with the getValue().toString() text appears
		//super.encodeAll(context);

		ResponseWriter writer = context.getResponseWriter();
		setTextOnly((boolean) getAttributes().get("textOnly"));

		//Debug in case of problems
		if (favoriteService == null)
		{
			writer.write("favoriteService is null.\n");
		}
		if (manager == null)
		{
			writer.write("manager is null.\n");
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
		if (textOnly)
		{
			writer.startElement("textarea", this);
			writer.writeAttribute("id", "fieldTextArea", null);
			writer.writeAttribute("style", "width:100%; text-align:center;", null);
			writer.writeAttribute("readonly", "readonly", null);
			if (field != null)
			{
				writer.writeText(field.toString(manager.getFieldColor()), null);
			}
			else
			{
				//Write a happy face
				writer.write(":-)");
			}
			writer.endElement("textarea");
		}
		//Otherwise draws a normal version of the Field
		else
		{
			writeTesseraeField(field, writer);
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
	}

	//Draws the Tesserae Field using the HTML and/or Faces components
	private void writeTesseraeField(Field field, ResponseWriter writer) throws IOException
	{
		if (field == null)
		{

		}
		else
		{
			Draggable draggable = new Draggable();
			writer.startElement("dragger", draggable);
			writer.write("test");
			writer.endElement("dragger");
		}
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
