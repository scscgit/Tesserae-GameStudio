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

package sk.tuke.gamestudio.game.tesserae.cui.interpreter;

import java.util.ArrayDeque;
import java.util.StringTokenizer;

/**
 * Custom language interpreter of (almost) any kind.
 * Tokenizes commands using space and implements useful methods to easily read the chain of commands from a queue.
 * <p/>
 * Created by Steve on 3.3.2016.
 */
public abstract class AbstractInterpreter implements Interpreter
{
	//Constants
	//Token used between each separate command in the list of commands
	public static final String TOKEN = " ";

	//Objects
	//Queue of commands to be interpreted
	private ArrayDeque<String> commands;

	public AbstractInterpreter()
	{
		this.commands = new ArrayDeque<String>();
	}

	//Exception thrown when the syntactical analysis cannot fully construct a valid command from the grammar yet
	protected final class InterpreterNoInstructionsException extends InterpreterException
	{
		InterpreterNoInstructionsException()
		{
			super("Instruction is missing required operands.");
		}
	}

	//Exception thrown when the tokenized part of an instruction does not correspond to any valid command
	protected final class InterpreterInvalidInstructionsException extends InterpreterException
	{
		InterpreterInvalidInstructionsException()
		{
			super("Instruction is not valid.");
		}
		InterpreterInvalidInstructionsException(String message)
		{
			super("Instruction is not valid: " + message);
		}
	}

	//Adds next string at the end of the queue
	protected final void add(String string)
	{
		this.commands.add(string);
	}

	//Returns next string in the order of execution and removes it permanently
	protected final String remove()
	{
		if (this.commands.size() > 0)
		{
			return this.commands.remove();
		}
		return "";
	}

	//Peek-a-boo, queue!
	protected final String peek()
	{
		if (this.commands.size() > 0)
		{
			return this.commands.peek();
		}
		return "";
	}

	//Queries the queue for state of its content (whether it's not empty)
	protected final boolean hasNext()
	{
		return !this.commands.isEmpty();
	}

	//Proclaims that the queue must not be empty, otherwise cancels the chain of responsibility by throwing an error
	protected final void assertNext() throws InterpreterException
	{
		if (!hasNext())
		{
			throw new InterpreterNoInstructionsException();
		}
	}

	//Replaces the entire queue by rebuilding it from a new string that consists of more tokenized substrings
	protected final void replaceQueue(String commands)
	{
		//Old queue gets lost
		this.commands.clear();
		if (commands != null)
		{
			//C programmers might remember char* strtok()
			StringTokenizer tokenizer = new StringTokenizer(commands, TOKEN);
			while (tokenizer.hasMoreElements())
			{
				add(tokenizer.nextToken());
			}
		}
	}

	//Interface with a "user" that requests a service from the interpreter.
	//Its job is to basically run a chain of responsibility, starting with the execute() method.
	public final String interpret(String commands) throws InterpreterException
	{
		//Each command interpretation uses its own queue, without remembering the old one
		replaceQueue(commands.toLowerCase());

		//We can prepare an optional result message for the user
		StringBuilder stringBuilder = new StringBuilder();

		//Executes commands one by one
		String command = peek();
		try
		{
			while (hasNext())
			{
				stringBuilder.append(execute());
			}
		}
		catch (InterpreterException exception)
		{
			if (command != null)
			{
				//We show all correct messages before adding a list of problems
				if (stringBuilder.length() != 0)
				{
					stringBuilder.append("\n");
				}
				throw new InterpreterException(stringBuilder.toString() +
				                               "Instructions starting from <" +
				                               command +
				                               "> have failed to get processed.\n" +
				                               exception.getMessage());
			}
		}

		//In case everything goes okay, we return the result of the command chain
		return stringBuilder.toString();
	}

	//Main execute phase of a new command, a starting point for the chain of commands
	protected abstract String execute() throws InterpreterException;
}
