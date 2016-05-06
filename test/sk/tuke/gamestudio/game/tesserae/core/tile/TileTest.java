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

package sk.tuke.gamestudio.game.tesserae.core.tile;

import sk.tuke.gamestudio.game.tesserae.core.tile.Tile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.template.FieldTemplate;

import java.util.EnumSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing tiles.
 * <p>
 * Created by Steve on 18.03.2016.
 */
public class TileTest
{
	private FieldBuilder builder;

	//Makes a new tile
	private Tile tile()
	{
		return new Field(this.builder).getTile(0, 0);
	}

	@Before
	public void setUp() throws Exception
	{
		this.builder = new SimpleFieldBuilder
			(
				new Settings(12, 12, Field.DifficultySetting.MEDIUM, FieldTemplate.Setting.RECTANGLE)
			);
	}
	@After
	public void tearDown() throws Exception
	{

	}
	@Test
	public void getType() throws Exception
	{
		assertTrue(new Tile(EnumSet.of(Tile.Type.CIRCLE)).getType().equals(EnumSet.of(Tile.Type.CIRCLE)));
	}
	@Test
	public void isPrimary() throws Exception
	{
		assertTrue(new Tile(EnumSet.of(Tile.Type.CIRCLE)).isPrimary());
		assertFalse(new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS)).isPrimary());
		assertFalse(
			new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS, Tile.Type.PLUS, Tile.Type.SQUARE)).isPrimary());
	}
	@Test
	public void isSecondary() throws Exception
	{
		assertFalse(new Tile(EnumSet.of(Tile.Type.CIRCLE)).isSecondary());
		assertTrue(new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS)).isSecondary());
		assertFalse(
			new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS, Tile.Type.PLUS, Tile.Type.SQUARE)).isSecondary());
	}
	@Test
	public void isTertiary() throws Exception
	{
		assertFalse(new Tile(EnumSet.of(Tile.Type.CIRCLE)).isTertiary());
		assertFalse(new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS)).isTertiary());
		assertTrue(
			new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS, Tile.Type.PLUS, Tile.Type.SQUARE)).isTertiary());
	}
	@Test
	public void setType() throws Exception
	{
		Tile tile = new Tile(EnumSet.of(Tile.Type.CIRCLE));
		tile.setType(EnumSet.of(Tile.Type.SQUARE));
		assertFalse(tile.getType().equals(EnumSet.of(Tile.Type.CIRCLE)));
		assertTrue(tile.getType().equals(EnumSet.of(Tile.Type.SQUARE)));
	}
	@Test
	public void jumpedOverBy() throws Exception
	{
		Tile tile;

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE));
		tile.jumpedOverBy(new Tile(EnumSet.of(Tile.Type.CIRCLE)));
		assertTrue(tile.getType().equals(EnumSet.noneOf(Tile.Type.class)));

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE));
		tile.jumpedOverBy(new Tile(EnumSet.of(Tile.Type.SQUARE)));
		assertTrue(tile.getType().equals(EnumSet.noneOf(Tile.Type.class)));

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.SQUARE));
		tile.jumpedOverBy(new Tile(EnumSet.of(Tile.Type.SQUARE)));
		assertTrue(tile.getType().equals(EnumSet.of(Tile.Type.CIRCLE)));

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.SQUARE, Tile.Type.PLUS));
		tile.jumpedOverBy(new Tile(EnumSet.of(Tile.Type.SQUARE)));
		assertTrue(tile.getType().equals(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.PLUS)));
	}
	@Test
	public void jumpedOnBy() throws Exception
	{
		Tile tile;

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE));
		tile.jumpedOnBy(new Tile(EnumSet.of(Tile.Type.CIRCLE)));
		System.out.println(tile.getType());
		assertTrue(tile.getType().equals(EnumSet.of(Tile.Type.CIRCLE)));

		tile = new Tile(EnumSet.of(Tile.Type.CIRCLE));
		tile.jumpedOnBy(new Tile(EnumSet.of(Tile.Type.SQUARE)));
		assertTrue(tile.getType().equals(EnumSet.of(Tile.Type.CIRCLE, Tile.Type.SQUARE)));
	}
}
