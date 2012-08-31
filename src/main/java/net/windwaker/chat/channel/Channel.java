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
package net.windwaker.chat.channel;

import java.util.HashSet;
import java.util.Set;

import net.windwaker.chat.Chat;
import net.windwaker.chat.ChatLogger;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;

public class Channel {
	private final String name;
	private final Set<String> listeners = new HashSet<String>();
	private final ChatLogger logger = ChatLogger.getInstance();
	private String format = "[%channel%] %message%", password;
	private ChatArguments joinMessage, leaveMessage;

	public Channel(String name) {
		this.name = name;
		joinMessage = new ChatArguments(ChatStyle.BRIGHT_GREEN, "You have joined ", name, "!");
		leaveMessage = new ChatArguments(ChatStyle.RED, "You have left ", name, "!");
	}

	public String getName() {
		return name;
	}

	public ChatArguments getJoinMessage() {
		return joinMessage;
	}

	public void setJoinMessage(Object... joinMessage) {
		this.joinMessage = new ChatArguments(joinMessage);
	}

	public ChatArguments getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(Object... leaveMessage) {
		this.leaveMessage = new ChatArguments(leaveMessage);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasPassword() {
		return password != null;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean addListener(Chatter chatter) {
		return listeners.add(chatter.getParent().getName());
	}

	public boolean removeListener(Chatter chatter) {
		return listeners.remove(chatter.getParent().getName());
	}

	public void broadcast(Object... message) {
		for (String n : listeners) {
			Chatter chatter = Chat.getChatter(n);
			if (chatter != null) {
				chatter.getParent().sendMessage(message);
			}
		}
		logger.info(new ChatArguments(message).getPlainString());
	}
}
