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
import java.util.List;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;

import org.spout.api.entity.Player;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

/**
 * Represents a collection of {@link Chatter}s
 */
public class ChatterConfiguration extends YamlConfiguration {
	private final WindChat plugin;
	private final Set<Chatter> chatters = new HashSet<Chatter>();

	/**
	 * Constructs a new ChatterConfiguration at 'plugins/WindChat/chatters.yml'
	 */
	public ChatterConfiguration(WindChat plugin) {
		super(new File(plugin.getDataFolder(), "chatters.yml"));
		this.plugin = plugin;
	}

	/**
	 * Handles the login process of the plugin for a player.
	 * @param player to login
	 */
	public void login(Player player) {
		// get channels
		List<String> channelNames = getChannels(player.getName());
		Set<Channel> channels = new HashSet<Channel>();
		for (String s : channelNames) {
			Channel channel = plugin.getChannels().get(s);
			if (channel != null) {
				channels.add(channel);
			}
		}
		Chatter chatter = new Chatter(plugin, player, channels);
		Channel activeChannel = plugin.getChannels().get(getActiveChannel(player.getName()));
		if (activeChannel == null) {
			activeChannel = plugin.getChannels().getDefault();
		}
		// join active channel
		chatter.join(activeChannel);
		chatters.add(chatter);
	}

	/**
	 * Saves a value at the path to disk.
	 * @param path
	 * @param value
	 */
	public void set(String path, Object value) {
		getNode(path).setValue(value);
		save();
	}

	/**
	 * Gets the list of invites for a chatter
	 * @param chatter
	 * @return list of invites
	 */
	public List<String> getInvites(String chatter) {
		return getNode("chatters." + chatter + ".invites").getStringList();
	}

	/**
	 * Saves the list of invites to disk.
	 * @param chatter
	 * @param invites
	 */
	public void setInvites(String chatter, List<String> invites) {
		set("chatters." + chatter + ".invites", invites);
	}

	/**
	 * Adds a new invite to disk.
	 * @param chatter
	 * @param invite
	 */
	public void addInvite(String chatter, String invite) {
		List<String> invites = getInvites(chatter);
		if (!invites.contains(invite)) {
			invites.add(invite);
		}
		setInvites(chatter, invites);
	}

	/**
	 * Remove an invite from disk.
	 * @param chatter
	 * @param invite
	 */
	public void removeInvite(String chatter, String invite) {
		List<String> invites = getInvites(chatter);
		invites.remove(invite);
		setInvites(chatter, invites);
	}

	/**
	 * Gets all channels of a {@link Chatter}.
	 * @param chatter
	 * @return all channels
	 */
	public List<String> getChannels(String chatter) {
		return getNode("chatters." + chatter + ".channels").getStringList();
	}

	/**
	 * Saves channels of a {@link Chatter}
	 * @param chatter
	 * @param channels
	 */
	public void setChannels(String chatter, List<String> channels) {
		set("chatters." + chatter + ".channels", channels);
	}

	/**
	 * Adds a new channel
	 * @param chatter
	 * @param channel
	 */
	public void addChannel(String chatter, String channel) {
		List<String> channels = getChannels(chatter);
		if (!channels.contains(channel)) {
			channels.add(channel);
		}
		setChannels(chatter, channels);
	}

	/**
	 * Removes a channel
	 * @param chatter
	 * @param channel
	 */
	public void removeChannel(String chatter, String channel) {
		List<String> channels = getChannels(chatter);
		channels.remove(channel);
		setChannels(chatter, channels);
	}

	/**
	 * Saves the active channel to disk.
	 * @param chatter
	 * @param channel
	 */
	public void setActiveChannel(String chatter, String channel) {
		set("chatters." + chatter + ".active-channel", channel);
	}

	/**
	 * Gets the active channel from disk.
	 * @param chatter
	 * @return active channel
	 */
	public String getActiveChannel(String chatter) {
		return getNode("chatters." + chatter + ".active-channel").getString();
	}

	/**
	 * Gets the collection of chatters.
	 * @return set of chatters
	 */
	public Set<Chatter> get() {
		return chatters;
	}

	/**
	 * Gets a chatter from the specified name
	 * @param name
	 * @return chatter from name
	 */
	public Chatter get(String name) {
		for (Chatter chatter : chatters) {
			if (chatter.getParent().getName().equalsIgnoreCase(name)) {
				return chatter;
			}
		}
		return null;
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
}
