/* Copyright (c) 2012 Walker Crouse, <http://windwaker.net/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.windwaker.chat.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.spout.api.chat.ChatArguments;

import org.spout.api.chat.Placeholder;

public class Placeholders {
	/**
	 * Placeholder to be replaced by a players name
	 */
	public static final Placeholder NAME = new Placeholder("name");
	/**
	 * Placeholder to be replaced by a message
	 */
	public static final Placeholder MESSAGE = new Placeholder("message");
	/**
	 * Placeholder to be replaced by a quit message
	 */
	public static final Placeholder QUIT_MESSAGE = new Placeholder("quit_message");
	/**
	 * Represents the address of a message.
	 */
	public static final Placeholder ADDRESS = new Placeholder("address");

	private static final Set<Placeholder> VALUES = new HashSet<Placeholder>();

	static {
		for (Field field : Placeholders.class.getFields()) {
			try {
				Object obj = field.get(null);
				if (obj instanceof Placeholder) {
					VALUES.add((Placeholder) obj);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets all defined {@link Placeholder}s in this {@link Class}.
	 * @return all placeholders
	 */
	public static Set<Placeholder> getValues() {
		return VALUES;
	}
	
	/**
	 * Formats a message sent to a channel.
	 * 
	 * @param format to set
	 * @param message to use for placeholder
	 */
	public static void format(Placeholder placeholder, ChatArguments template, ChatArguments args) {
		if (template.hasPlaceholder(placeholder)) {
			template.setPlaceHolder(placeholder, args);
		}
	}
}
