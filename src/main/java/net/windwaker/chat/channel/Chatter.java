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
import java.util.List;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.util.Format;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.Placeholder;
import org.spout.api.entity.Player;

public class Chatter {
	public static final Placeholder NAME = new Placeholder("name"), MESSAGE = new Placeholder("message");
	private final WindChat plugin = WindChat.getInstance();
	private final Player parent; // TODO: Make Chatter a component of Player
	private final Set<Channel> channels;
	private Channel activeChannel;

	public Chatter(Player parent, Set<Channel> channels) {
		this.parent = parent;
		this.channels = channels;
	}

	public Player getParent() {
		return parent;
	}

	public void chat(ChatArguments message) {
		ChatArguments template = plugin.getFormat(Format.CHAT, parent);
		if (template.hasPlaceholder(NAME)) {
			template.setPlaceHolder(NAME, new ChatArguments(parent.getDisplayName()));
		}
		if (template.hasPlaceholder(MESSAGE)) {
			template.setPlaceHolder(MESSAGE, message);
		}
		activeChannel.broadcast(template);
	}

	public void join(Channel channel) {
		plugin.getChatters().addChannel(parent.getName(), channel.getName());
		plugin.getChatters().setActiveChannel(parent.getName(), channel.getName());
		channels.add(channel);
		channel.addListener(this);
		activeChannel = channel;
		parent.sendMessage(channel.getJoinMessage());
	}

	public void leave(Channel channel) {
		if (channel.equals(activeChannel)) {
			throw new IllegalArgumentException("A player may not leave the channel he/she is active in.");
		}
		plugin.getChatters().removeChannel(parent.getName(), channel.getName());
		channel.removeListener(this);
		channels.remove(channel);
		parent.sendMessage(channel.getLeaveMessage());
	}

	public Channel getActiveChannel() {
		return activeChannel;
	}

	public Set<Channel> getChannels() {
		return channels;
	}
}                                    
