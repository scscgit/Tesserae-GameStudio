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

package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.service.DatabaseException;

/**
 * Problem with Favorites.
 * <p/>
 * Created by Steve on 14.03.2016.
 */
public class FavoriteException extends DatabaseException
{
	public FavoriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public FavoriteException()
	{
	}
	public FavoriteException(String message)
	{
		super(message);
	}
	public FavoriteException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public FavoriteException(Throwable cause)
	{
		super(cause);
	}

	//Lets throw the original SQL error code
	public FavoriteException setErrorCode(int errorCode)
	{
		super.setErrorCode(errorCode);
		return this;
	}

	//Most useful constructor
	public FavoriteException(DatabaseException cause)
	{
		setErrorCode(cause.getErrorCode());
	}
}
