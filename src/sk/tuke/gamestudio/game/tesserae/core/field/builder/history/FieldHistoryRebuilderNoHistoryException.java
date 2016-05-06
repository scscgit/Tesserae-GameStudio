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

package sk.tuke.gamestudio.game.tesserae.core.field.builder.history;

import sk.tuke.gamestudio.game.tesserae.core.TesseraeRuntimeException;

/**
 * Created by Steve on 05.04.2016.
 */
public class FieldHistoryRebuilderNoHistoryException extends TesseraeRuntimeException
{
	public FieldHistoryRebuilderNoHistoryException()
	{
		super("There is no history to go back to.");
	}
	public FieldHistoryRebuilderNoHistoryException(String message)
	{
		super(message);
	}
	public FieldHistoryRebuilderNoHistoryException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public FieldHistoryRebuilderNoHistoryException(Throwable cause)
	{
		super(cause);
	}
	public FieldHistoryRebuilderNoHistoryException(String message, Throwable cause, boolean enableSuppression,
	                                               boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
