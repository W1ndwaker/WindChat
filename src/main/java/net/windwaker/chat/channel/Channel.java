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

import net.windwaker.chat.WindChat;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;

public class Channel {
	private final WindChat plugin = WindChat.getInstance();
	private final String name;
	private final Set<String> listeners = new HashSet<String>();
	private final Set<String> banned = new HashSet<String>();
	private String password;
	private boolean inviteOnly;
	private ChatArguments joinMessage, leaveMessage, format, banMessage;

	public Channel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void ban(String name) {
		ban(name, true);
	}

	public void ban(String name, boolean kick) {
		ban(name, kick, ChatStyle.RED, "Banned from ", name);
	}

	public void ban(String name, boolean kick, Object... reason) {
		if (kick) {
			Chatter chatter = plugin.getChatters().get(name);
			if (chatter == null) {
				return;
			}
			chatter.kick(this, reason);
		}
		plugin.getChannels().addBanned(this.name, name);
		banned.add(name);
	}

	public void unban(String name) {
		plugin.getChannels().removeBanned(this.name, name);
		banned.remove(name);
	}

	public Set<String> getBanned() {
		return banned;
	}

	public boolean isBanned(String name) {
		return banned.contains(name);
	}

	public ChatArguments getBanMessage() {
		return banMessage;
	}

	public void setBanMessage(ChatArguments banMessage) {
		plugin.getChannels().setBanMessage(name, banMessage);
		this.banMessage = banMessage;
	}

	public boolean isInviteOnly() {
		return inviteOnly;
	}

	public void setInviteOnly(boolean inviteOnly) {
		plugin.getChannels().setInviteOnly(name, inviteOnly);
		this.inviteOnly = inviteOnly;
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
		this.joinMessage = joinMessage;
	}

	public ChatArguments getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(ChatArguments leaveMessage) {
		plugin.getChannels().setLeaveMessage(name, leaveMessage);
		this.leaveMessage = leaveMessage;
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

	public boolean isListening(String name) {
		return listeners.contains(name);
	}

	public Set<String> getListeners() {
		return listeners;
	}

	public boolean addListener(String chatterName) {
		plugin.getChannels().addListener(name, chatterName);
		return listeners.add(chatterName);
	}

	public boolean removeListener(String chatterName) {
		plugin.getChannels().removeListener(name, chatterName);
		return listeners.remove(chatterName);
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
