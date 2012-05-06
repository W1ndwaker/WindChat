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

import net.windwaker.chat.channel.Chatter;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerJoinEvent;

/**
 * Handles formatting of messages from players.
 *
 * @author Windwaker
 */
public class EventListener implements Listener {

	@EventHandler(order = Order.LATEST)
	public void playerChat(PlayerChatEvent event) {

		// Cancel all chat events and handle them through the channels.
		event.setCancelled(true);
		Chatter chatter = WindChat.getChat().getChatter(event.getPlayer().getName());
		if (chatter != null) {
			chatter.chat(event.getMessage());
		}
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {

		Chat chat = WindChat.getChat();
		String name = event.getPlayer().getName();
		Chatter chatter = chat.getChatter(name);

		// Player is not new here.
		if (chatter != null) {
			return;
		}

		// Player joined for the first time
		chat.addChatter(new Chatter(name));
	}
}
