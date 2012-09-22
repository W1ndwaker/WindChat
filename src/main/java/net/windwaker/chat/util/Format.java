/* Copyright (c) 2012 Walker Crouse, http://windwaker.net/
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

import net.windwaker.chat.io.yaml.ChatConfiguration;

import org.spout.api.chat.ChatArguments;

/**
 * Represents the Format type of a message.
 * @see {@link net.windwaker.chat.channel.Chatter#getFormat(Format)}
 */
public enum Format {
	/**
	 * The standard format for chat messages.
	 */
	CHAT("chat-format", ChatArguments.fromFormatString(ChatConfiguration.DEFAULT_CHAT_FORMAT.getString())),
	/**
	 * The standard format for messages when joining the game.
	 */
	JOIN_MESSAGE("join-message-format", ChatArguments.fromFormatString(ChatConfiguration.DEFAULT_JOIN_MESSAGE_FORMAT.getString())),
	/**
	 * The standard format for messages when leaving the game.
	 */
	LEAVE_MESSAGE("leave-message-format", ChatArguments.fromFormatString(ChatConfiguration.DEFAULT_LEAVE_MESSAGE_FORMAT.getString()));

	private final String node;
	private final ChatArguments def;

	private Format(String node, ChatArguments def) {
		this.node = node;
		this.def = def;
	}

	/**
	 * Gets the default value of the Format.
	 * @return default value
	 */
	public ChatArguments getDefault() {
		return def;
	}

	@Override
	public String toString() {
		return node;
	}
}
