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
package net.windwaker.chat.chan;

import java.util.HashSet;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.event.bot.BotConnectEvent;
import net.windwaker.chat.event.bot.BotReceiveMessageEvent;
import net.windwaker.chat.handler.IrcChatHandler;
import org.pircbotx.PircBotX;

import org.spout.api.chat.ChatArguments;
import org.spout.api.plugin.PluginDescriptionFile;

public class IrcBot extends PircBotX {
	private final Set<Channel> localChannels = new HashSet<Channel>();
	private final Set<String> ircChannels;
	private final WindChat plugin;

	public IrcBot(WindChat plugin, String name, boolean verbose, String server, Set<String> ircChannels) {
		this.plugin = plugin;
		this.ircChannels = ircChannels;
		this.name = name;
		this.server = server;
		this.verbose = verbose;
		login = plugin.getName();
		PluginDescriptionFile pdf = plugin.getDescription();
		version = login + " v" + pdf.getVersion() + " by " + pdf.getAuthors().toString();
		autoNickChange = true;
		listenerManager.addListener(new IrcChatHandler());
	}

	public boolean connect() {
		try {
			BotConnectEvent event = plugin.getEngine().getEventManager().callEvent(new BotConnectEvent(this, server));
			if (event.isCancelled()) {
				return false;
			}
			connect(event.getServer());
			for (String chan : ircChannels) {
				joinChannel(chan);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void joinLocal(Channel channel) {
		localChannels.add(channel);
	}
	
	public void leaveLocal(Channel channel) {
		localChannels.remove(channel);
	}

	public void messageReceived(String user, String message) {
		BotReceiveMessageEvent event = plugin.getEngine().getEventManager().callEvent(new BotReceiveMessageEvent(this, user, message));
		if (event.isCancelled()) {
			return;
		}
		user = event.getUser();
		message = event.getMessage();
		for (Channel chan : localChannels) {
			chan.broadcast(new ChatArguments(user + ": " + message));
		}
	}
	
	public void sendMessage(ChatArguments message) {
		String str = message.getPlainString();
		for (String chan : ircChannels) {
			sendMessage(chan, str);
		}
	}
}
