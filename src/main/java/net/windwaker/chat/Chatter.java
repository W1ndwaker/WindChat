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
package net.windwaker.chat;

import org.spout.api.player.Player;

import java.util.HashSet;
import java.util.Set;

public class Chatter {
	private final Player parent;
	private final Set<Channel> channels = new HashSet<Channel>();
	private Channel activeChannel;
	
	public Chatter(Player parent) {
		this.parent = parent;
	}
	
	public void send(String message) {
		parent.sendMessage(message);
	}
	
	public String getName() {
		return parent.getName();
	}
	
	public void join(Channel channel) {
		channels.add(channel);
		channel.addChatter(this);
		activeChannel = channel;
	}
	
	public void leave(Channel channel) {
		channels.remove(channel);
	}
	
	public void setActiveChannel(Channel activeChannel) {
		this.activeChannel = activeChannel;
	}
	
	public Channel getActiveChannel() {
		return activeChannel;
	}
}                                    
