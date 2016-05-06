package sk.tuke.gamestudio.game.tesserae.jsf;

import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;

import javax.ejb.EJB;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by Steve on 18.04.2016.
 */
@FacesComponent("Tesserae")
public class TesseaeComponent extends UICommand
{
	@EJB
	private FavoriteGameDatabaseService favoriteService;

	@Inject
	FieldInstanceManager manager;

	@Override
	public void encodeAll(FacesContext context) throws IOException
	{
		super.encodeAll(context);

		//FieldInstanceManager manager = (FieldInstanceManager) getValue();
		ResponseWriter writer = context.getResponseWriter();

		writer.write("<textarea id=\"fieldTextArea\" style=\"width:100%; text-align:center;\" readonly=\"readonly\">");
		if(manager != null)
		{
			Field field = manager.getManagedField();
			if (field != null)
			{
				writer.write(field.toString());
			}
		}
		writer.write("</textarea>");

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
}
