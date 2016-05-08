package sk.tuke.gamestudio.game.tesserae.jsf;

import java.io.Serializable;

/**
 * Tesserae Theme.
 * <p>
 * Created by Steve on 07.05.2016.
 */
public class Theme implements Serializable
{
	private String displayedName;
	private boolean textOnly;
	private String pathName;

	//JSF constructor
	public Theme()
	{
	}
	//Graphical Theme constructor
	public Theme(String displayedName, String pathName)
	{
		this.displayedName = displayedName;
		this.pathName = pathName;
		this.textOnly = false;
	}
	//TextOnly Theme option constructor
	public Theme(String displayedName)
	{
		this.displayedName = displayedName;
		this.textOnly = true;
	}

	public String getDisplayedName()
	{
		return displayedName;
	}

	public boolean isTextOnly()
	{
		return textOnly;
	}

	public String getPathName()
	{
		return pathName;
	}
}
