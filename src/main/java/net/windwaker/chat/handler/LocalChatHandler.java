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
package net.windwaker.chat.handler;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.Chatter;
import net.windwaker.chat.util.Format;
import net.windwaker.chat.util.Placeholders;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.protocol.Protocol;

/**
 * Handles formatting of messages from players.
 * @author Windwaker
 */
public class LocalChatHandler implements Listener {
	private final WindChat plugin;

	/**
	 * Constructs a new LocalChatHandler
	 * @param plugin
	 */
	public LocalChatHandler(WindChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.LATEST)
	public void playerChat(PlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			return;
		}
		chatter.chat(event.getMessage());
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		// Load player
		Chatter chatter = plugin.getChatters().load(player);
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			return;
		}
		ChatArguments template = chatter.getFormat(Format.JOIN_MESSAGE);
		Placeholders.format(Placeholders.NAME, template, new ChatArguments(player.getDisplayName()));
		event.setMessage(template);
	}

	@EventHandler
	public void playerLeave(PlayerLeaveEvent event) {
		Player player = event.getPlayer();
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			return;
		}
		ChatArguments template = chatter.getFormat(Format.LEAVE_MESSAGE);
		Placeholders.format(Placeholders.NAME, template, new ChatArguments(player.getDisplayName()));
		ChatArguments quitMessage = chatter.getQuitMessage();
		String reason = event.isKick() ? "Kicked" : "Quit";
		Placeholders.format(Placeholders.QUIT_MESSAGE, template, quitMessage == null ? new ChatArguments(reason) : quitMessage);
		event.setMessage(template);
	}
}
