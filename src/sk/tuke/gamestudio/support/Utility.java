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

package sk.tuke.gamestudio.support;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Global static helpers.
 * <p/>
 * Created by Steve on 29.2.2016.
 */
public class Utility
{
	private static Random random = null;

	private static Random getRandom()
	{
		if (Utility.random == null)
		{
			Utility.random = new Random();
		}
		return Utility.random;
	}

	public static Timestamp getCurrentSqlTimestamp()
	{
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	//What power of ten does the number have?
	public static int whatPowerOfTen(int number)
	{
		int power = 0;
		while (number >= 10)
		{
			number /= 10;
			power++;
		}
		return power;
	}

	public static Integer randomInt(Integer max)
	{
		return getRandom().nextInt(max);
	}

	@Deprecated
	public static Boolean randomBool()
	{
		return getRandom().nextBoolean();
	}

	//Formats the date to a string
	public static String formatDate(Date date)
	{
		return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(date);
	}

	//Console color formatting
	public static String redText(String text)
	{
		return "\033[31;1m" + text + "\033[0m";
	}
	public static String greenText(String text)
	{
		return "\033[32;1m" + text + "\033[0m";
	}
	public static String yellowText(String text)
	{
		return "\033[33;1m" + text + "\033[0m";
	}
	public static String blueText(String text)
	{
		return "\033[34;1m" + text + "\033[0m";
	}
	public static String redColor(String text)
	{
		return "\033[41;1m" + text + "\033[0m";
	}
	public static String greenColor(String text)
	{
		return "\033[42;1m" + text + "\033[0m";
	}
	public static String yellowColor(String text)
	{
		return "\033[43;1m" + text + "\033[0m";
	}
	public static String blueColor(String text)
	{
		return "\033[44;1m" + text + "\033[0m";
	}

	public static XMLGregorianCalendar dateToXmlGregorianCalendar(Date date) throws DatatypeConfigurationException
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
	}
	public static long XMLGregorianCalendarToTime(XMLGregorianCalendar xmlGregorianCalendar)
	{
		return xmlGregorianCalendar.toGregorianCalendar().getTime().getTime();
	}
}
