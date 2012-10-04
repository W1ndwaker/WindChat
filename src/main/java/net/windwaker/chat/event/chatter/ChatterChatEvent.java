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
package net.windwaker.chat.event.chatter;

import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.chan.Chatter;

import org.spout.api.chat.ChatArguments;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

public class ChatterChatEvent extends ChatterEvent implements Cancellable {
	public static final HandlerList handlers = new HandlerList();
	private Channel channel;
	private ChatArguments message;

	public ChatterChatEvent(Chatter chatter, Channel channel, ChatArguments message) {
		super(chatter);
		this.channel = channel;
		this.message = message;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ChatArguments getMessage() {
		return message;
	}

	public void setMessage(ChatArguments message) {
		this.message = message;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
