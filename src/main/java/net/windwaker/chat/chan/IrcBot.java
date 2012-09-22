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
package net.windwaker.chat.chan;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.handler.IrcChatHandler;
import org.pircbotx.PircBotX;

import org.spout.api.chat.ChatArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.data.ValueHolder;
import org.spout.api.geo.World;
import org.spout.api.lang.Locale;
import org.spout.api.plugin.PluginDescriptionFile;

public class IrcBot extends PircBotX implements CommandSource {
	private final Chatter chatter;
	private String channel;

	public IrcBot(WindChat plugin, String name, boolean verbose, String server, String channel) {
		this.chatter = new Chatter(plugin, this);
		this.channel = channel;
		this.name = name;
		this.server = server;
		this.verbose = verbose;
		login = plugin.getName();
		PluginDescriptionFile pdf = plugin.getDescription();
		version = login + " v" + pdf.getVersion() + " by " + pdf.getAuthors().toString();
		autoNickChange = true;
		listenerManager.addListener(new IrcChatHandler());
	}

	public Chatter getChatter() {
		return chatter;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean connect(Channel chan) {
		try {
			connect(server);
			joinChannel(channel);
			chatter.join(chan);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onMessage(String channel, String user, String message) {
		chatter.chat(new ChatArguments(user, ": ", message));
	}

	@Override
	public boolean sendMessage(Object... message) {
		return sendRawMessage(message);
	}

	@Override
	public void sendCommand(String command, ChatArguments arguments) {
	}

	@Override
	public void processCommand(String command, ChatArguments arguments) {
	}

	@Override
	public boolean sendMessage(ChatArguments message) {
		return sendRawMessage(message);
	}

	@Override
	public boolean sendRawMessage(Object... message) {
		return sendRawMessage(new ChatArguments(message));
	}

	@Override
	public boolean sendRawMessage(ChatArguments message) {
		sendMessage(channel, message.getPlainString());
		return true;
	}

	@Override
	public Locale getPreferredLocale() {
		return null;
	}

	@Override
	public boolean hasPermission(String node) {
		return true;
	}

	@Override
	public boolean hasPermission(World world, String node) {
		return true;
	}

	@Override
	public boolean isInGroup(String group) {
		return false;
	}

	@Override
	public boolean isInGroup(World world, String group) {
		return false;
	}

	@Override
	public String[] getGroups() {
		return new String[0];
	}

	@Override
	public String[] getGroups(World world) {
		return new String[0];
	}

	@Override
	public ValueHolder getData(String node) {
		return null;
	}

	@Override
	public ValueHolder getData(World world, String node) {
		return null;
	}

	@Override
	public boolean hasData(String node) {
		return false;
	}

	@Override
	public boolean hasData(World world, String node) {
		return false;
	}
}
