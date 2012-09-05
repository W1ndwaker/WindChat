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
import net.windwaker.chat.util.Format;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.Placeholder;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.data.ValueHolder;
import org.spout.api.entity.Player;

public class Chatter {
	public static final Placeholder NAME = new Placeholder("name"), MESSAGE = new Placeholder("message");
	private final WindChat plugin = WindChat.getInstance();
	private final Player parent; // TODO: Make Chatter a component of Player
	private final Set<Channel> channels;
	private final Set<Channel> invites = new HashSet<Channel>();
	private Channel activeChannel;

	public Chatter(Player parent, Set<Channel> channels) {
		this.parent = parent;
		this.channels = channels;
	}

	public Player getParent() {
		return parent;
	}

	public void sendInvite(Channel channel) {
		plugin.getChatters().addInvite(parent.getName(), channel.getName());
		invites.add(channel);
	}

	public void revokeInvite(Channel channel) {
		plugin.getChatters().removeInvite(parent.getName(), channel.getName());
		invites.remove(channel);
	}

	public boolean isInvitedTo(Channel channel) {
		return invites.contains(channel);
	}

	public void chat(ChatArguments message) {
		ChatArguments template = getFormat(Format.CHAT);
		if (template.hasPlaceholder(NAME)) {
			template.setPlaceHolder(NAME, new ChatArguments(parent.getDisplayName()));
		}
		if (template.hasPlaceholder(MESSAGE)) {
			template.setPlaceHolder(MESSAGE, message);
		}
		activeChannel.broadcast(template);
	}

	public void join(Channel channel) {
		join(channel, channel.getJoinMessage());
	}

	public void join(Channel channel, Object... message) {
		plugin.getChatters().addChannel(parent.getName(), channel.getName());
		plugin.getChatters().setActiveChannel(parent.getName(), channel.getName());
		channels.add(channel);
		channel.addListener(parent.getName());
		activeChannel = channel;
		parent.sendMessage(message);
	}

	public void leave(Channel channel) {
		leave(channel, channel.getLeaveMessage());
	}

	public void leave(Channel channel, Object... message) {
		if (channel.equals(activeChannel)) {
			throw new IllegalArgumentException("A player may not leave the channel he/she is active in.");
		}
		plugin.getChatters().removeChannel(parent.getName(), channel.getName());
		channel.removeListener(parent.getName());
		channels.remove(channel);
		parent.sendMessage(message);
	}

	public void ban() {
		ban(activeChannel);
	}

	public void ban(Channel channel) {
		ban(channel, ChatStyle.RED, "Banned from ", channel.getName());
	}

	public void ban(Channel channel, Object... reason) {
		channel.ban(parent.getName(), true, reason);
	}

	public void kick(Channel channel) {
		kick(channel, ChatStyle.RED, "Kicked from ", channel.getName());
	}

	public void kick(Channel channel, Object... reason) {
		leave(channel, ChatStyle.RED, "Kicked from ", channel.getName(), ": ", reason);
	}

	public ChatArguments getFormat(Format format) {
		ChatArguments def = format.getDefault();
		ValueHolder data = parent.getData(format.toString());
		if (data != null && data.getString() != null) {
			def = ChatArguments.fromFormatString(data.getString());
		}
		return def;
	}

	public Channel getActiveChannel() {
		return activeChannel;
	}

	public Set<Channel> getChannels() {
		return channels;
	}
}                                    
