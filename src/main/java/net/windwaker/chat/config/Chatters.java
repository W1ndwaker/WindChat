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
package net.windwaker.chat.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.chat.Chat;
import net.windwaker.chat.ChatLogger;
import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.entity.Player;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class Chatters {
	private final YamlConfiguration data = new YamlConfiguration(new File("plugins/WindChat/chatters.yml"));
	private final ChatLogger logger = ChatLogger.getInstance();
	private final Set<Chatter> chatters = new HashSet<Chatter>();

	public void load() {
		try {
			data.load();
		} catch (ConfigurationException e) {
			logger.severe("Failed to load chatter config: " + e.getMessage());
		}
	}

	public void save() {
		try {
			data.save();
		} catch (ConfigurationException e) {
			logger.severe("Failed to save chatter config: " + e.getMessage());
		}
	}

	public Chatter get(String name) {
		for (Chatter chatter : chatters) {
			if (chatter.getParent().getName().equalsIgnoreCase(name)) {
				return chatter;
			}
		}
		return null;
	}

	public void onLogin(Player player) {
		Channel channel = Chat.getChannel(data.getNode("chatters." + player.getName() + ".channel").getString());
		if (channel == null) {
			channel = Chat.getChannel(Settings.DEFAULT_CHANNEL.getString());
		}
		Chatter chatter = new Chatter(player);
		chatter.join(channel);
		chatters.add(chatter);
	}
}
