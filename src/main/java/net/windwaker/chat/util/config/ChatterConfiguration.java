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
package net.windwaker.chat.util.config;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;

import org.spout.api.entity.Player;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class ChatterConfiguration extends YamlConfiguration {
	private final WindChat plugin = WindChat.getInstance();
	private final Set<Chatter> chatters = new HashSet<Chatter>();

	public ChatterConfiguration() {
		super(new File(WindChat.getInstance().getDataFolder(), "chatters.yml"));
	}

	@Override
	public void load() {
		try {
			super.load();
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

	public void login(Player player) {
		List<String> channelNames = getChannels(player.getName());
		Set<Channel> channels = new HashSet<Channel>();
		for (String s : channelNames) {
			Channel channel = plugin.getChannels().get(s);
			if (channel != null) {
				channels.add(channel);
			}
		}
		Chatter chatter = new Chatter(player, channels);
		Channel activeChannel = plugin.getChannels().get(getActiveChannel(player.getName()));
		if (activeChannel == null) {
			activeChannel = plugin.getChannels().getDefault();
		}
		chatter.join(activeChannel);
		chatters.add(chatter);
	}

	public void set(String path, Object value) {
		getNode(path).setValue(value);
		save();
	}

	public List<String> getChannels(String chatter) {
		return getNode("chatters." + chatter + ".channels").getStringList();
	}

	public void setChannels(String chatter, List<String> channels) {
		set("chatters." + chatter + ".channels", channels);
	}

	public void addChannel(String chatter, String channel) {
		List<String> channels = getChannels(chatter);
		if (!channels.contains(channel)) {
			channels.add(channel);
		}
		setChannels(chatter, channels);
	}

	public void removeChannel(String chatter, String channel) {
		List<String> channels = getChannels(chatter);
		channels.remove(channel);
		setChannels(chatter, channels);
	}

	public void setActiveChannel(String chatter, String channel) {
		set("chatters." + chatter + ".active-channel", channel);
	}

	public String getActiveChannel(String chatter) {
		return getNode("chatters." + chatter + ".active-channel").getString();
	}

	public Set<Chatter> get() {
		return chatters;
	}

	public Chatter get(String name) {
		for (Chatter chatter : chatters) {
			if (chatter.getParent().getName().equalsIgnoreCase(name)) {
				return chatter;
			}
		}
		return null;
	}
}
