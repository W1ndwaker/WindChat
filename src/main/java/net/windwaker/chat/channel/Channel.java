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
import java.util.logging.Logger;

import net.windwaker.chat.WindChat;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.Placeholder;
import org.spout.api.chat.style.ChatStyle;

public class Channel {
	private final WindChat plugin = WindChat.getInstance();
	private final String name;
	private final Set<String> listeners = new HashSet<String>();
	private String password;
	private ChatArguments joinMessage, leaveMessage, format;

	public Channel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ChatArguments getFormat() {
		return format;
	}

	public void setFormat(ChatArguments format) {
		plugin.getChannels().setFormat(name, format);
		this.format = format;
	}

	public ChatArguments getJoinMessage() {
		return joinMessage;
	}

	public void setJoinMessage(ChatArguments joinMessage) {
		plugin.getChannels().setJoinMessage(name, joinMessage);
		this.joinMessage = new ChatArguments(joinMessage);
	}

	public ChatArguments getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(ChatArguments leaveMessage) {
		plugin.getChannels().setLeaveMessage(name, leaveMessage);
		this.leaveMessage = new ChatArguments(leaveMessage);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		plugin.getChannels().setPassword(name, password);
		this.password = password;
	}

	public boolean hasPassword() {
		return password != null;
	}

	public boolean addListener(Chatter chatter) {
		plugin.getChannels().addListener(name, chatter.getParent().getName());
		return listeners.add(chatter.getParent().getName());
	}

	public boolean removeListener(Chatter chatter) {
		plugin.getChannels().removeListener(name, chatter.getParent().getName());
		return listeners.remove(chatter.getParent().getName());
	}

	public void broadcast(ChatArguments message) {
		if (format.hasPlaceholder(Chatter.MESSAGE)) {
			format.setPlaceHolder(Chatter.MESSAGE, message);
		}
		for (String n : listeners) {
			Chatter chatter = plugin.getChatters().get(n);
			if (chatter != null) {
				chatter.getParent().sendMessage(format);
			}
		}
		plugin.getLogger().info(format.getPlainString());
	}
}
