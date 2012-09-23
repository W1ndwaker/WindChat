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
package net.windwaker.chat.io.yaml;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.irc.IrcBot;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class BotConfiguration extends YamlConfiguration {
	private final WindChat plugin;
	private final Set<IrcBot> bots = new HashSet<IrcBot>();

	public BotConfiguration(WindChat plugin) {
		super(new File(plugin.getDataFolder(), "bots.yml"));
		this.plugin = plugin;
	}

	public IrcBot get(String botName) {
		for (IrcBot bot : bots) {
			if (bot.getName().equalsIgnoreCase(botName)) {
				return bot;
			}
		}
		return null;
	}

	public IrcBot load(String botName) {
		String path = "bots." + botName;
		boolean verbose = getNode(path + ".verbose").getBoolean();
		String server = getNode(path + ".server").getString();
		String channel = getNode(path + ".channel").getString();
		IrcBot bot = new IrcBot(plugin, botName, verbose, server, channel);
		bots.add(bot);
		return bot;
	}

	@Override
	public void load() {
		try {
			super.load();
			if (!getNode("bots").isAttached()) {
				getNode("bots.ChatterBot.verbose").setValue(false);
				getNode("bots.ChatterBot.server").setValue("irc.esper.net");
				getNode("bots.ChatterBot.channel").setValue("#windwaker");
				save();
			}
			for (String bot : getNode("bots").getKeys(false)) {
				load(bot);
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			super.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
