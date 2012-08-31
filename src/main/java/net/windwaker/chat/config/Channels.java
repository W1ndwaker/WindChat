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

import net.windwaker.chat.ChatLogger;
import net.windwaker.chat.channel.Channel;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class Channels {
	private final YamlConfiguration data = new YamlConfiguration(new File("plugins/WindChat/channels.yml"));
	private final ChatLogger logger = ChatLogger.getInstance();
	private final Set<Channel> channels = new HashSet<Channel>();

	public void load() {
		try {
			// Add defaults
			data.setWritesDefaults(true);
			data.setNode(new ConfigurationNode(data, new String[]{"channels", "spout", "format"}, "[%channel%] %message%"));
			data.setNode(new ConfigurationNode(data, new String[]{"channels", "spout", "password"}, "unleashtheflow"));
			data.setNode(new ConfigurationNode(data, new String[]{"channels", "spout", "join-message"}, "{{BRIGHT_GREEN}}You have joined Spout!"));
			data.setNode(new ConfigurationNode(data, new String[]{"channels", "spout", "leave-message"}, "{{RED}}You have left Spout."));
			// Load config
			data.load();
			// Load channels
			for (String name : data.getNode("channels").getKeys(false)) {
				Channel channel = new Channel(name);
				String path = "channels." + name;
				System.out.println("Loading: " + name);
				channel.setFormat(data.getNode(path + ".format").getString());
				channel.setPassword(data.getNode(path + ".password").getString());
				channel.setJoinMessage(data.getNode(path + ".join-message").getString());
				channel.setLeaveMessage(data.getNode(path + ".leave-message").getString());
				channels.add(channel);
			}
		} catch (ConfigurationException e) {
			logger.severe("Failed to load channel config: " + e.getMessage());
		}
	}

	public void save() {
		try {
			data.save();
		} catch (ConfigurationException e) {
			logger.severe("Failed to save channel config: " + e.getMessage());
		}
	}

	public Channel get(String name) {
		for (Channel channel : channels) {
			if (channel.getName().equalsIgnoreCase(name)) {
				return channel;
			}
		}
		return null;
	}
}
