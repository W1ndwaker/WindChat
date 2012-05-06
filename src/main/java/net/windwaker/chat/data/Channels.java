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
package net.windwaker.chat.data;

import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.ChatLogger;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Channels {
	private final YamlConfiguration data = new YamlConfiguration(new File("plugins/WindChat/channels.yml"));
	private final ChatLogger logger = ChatLogger.getInstance();
	private final Set<Channel> channels = new HashSet<Channel>();
	
	public void load() {
		try {
			data.load();
			data.setWritesDefaults(true);
			addDefaults();
			loadChannels();
			data.save();
		} catch (ConfigurationException e) {
			logger.severe("Failed to load channel data: " + e.getMessage());
		}
	}

	private void addDefaults() {
		data.setNode(new ConfigurationNode(data, new String[] {"channels", "global", "format"}, "[%channel%] %message%"));
	}
	
	private void loadChannels() {
		for (String name : data.getNode("channels").getKeys(false)) {
			Channel channel = new Channel(name);
			String path = "channels." + name;
			channel.setFormat(data.getNode(path + ".format").getString());
			channels.add(channel);
		}
	}
	
	public Channel getChannel(String name) {
		for (Channel channel : channels) {
			if (channel.getName().equalsIgnoreCase(name)) {
				return channel;
			}
		}

		return null;
	}
}
