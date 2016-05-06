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

package sk.tuke.gamestudio.service;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Delegates correct method based on overloaded type for PreparedStatement setter.
 * <p/>
 * Created by Steve on 10.03.2016.
 */
public class StatementAttribute
{
	public static void set(PreparedStatement ps, int index, Integer value) throws SQLException
	{
		ps.setInt(index, value);
	}

	public static void set(PreparedStatement ps, int index, Blob value) throws SQLException
	{
		ps.setBlob(index, value);
	}

	public static void set(PreparedStatement ps, int index, Timestamp value) throws SQLException
	{
		ps.setTimestamp(index, value);
	}

	public static void set(PreparedStatement ps, int index, String value) throws SQLException
	{
		ps.setString(index, value);
	}
}
