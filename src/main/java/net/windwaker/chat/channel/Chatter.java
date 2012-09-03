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

import org.spout.api.entity.Player;

public class Chatter {
	private final Player parent; // TODO: Make Chatter a component of Player
	private final Set<Channel> channels = new HashSet<Channel>();
	private Channel activeChannel;

	public Chatter(Player parent) {
		this.parent = parent;
	}

	public Player getParent() {
		return parent;
	}

	public void chat(Object... message) {
		activeChannel.broadcast(message);
	}

	public void join(Channel channel) {
		channels.add(channel);
		channel.addListener(this);
		activeChannel = channel;
		parent.sendMessage(channel.getJoinMessage());
	}

	public void leave(Channel channel) {
		if (channel.equals(activeChannel)) {
			throw new IllegalArgumentException("A player may not leave the channel he/she is active in.");
		}
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
