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
package net.windwaker.chat.io.yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.chan.Chatter;

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
	public Chatter load(Player player) {
		Chatter chatter = new Chatter(plugin, player);
		String path = "chatters." + player.getName();
		chatter.setAutoSave(false);
		ChannelConfiguration channels = plugin.getChannels();
		for (String channel : getNode(path + ".channels").getStringList()) {
			Channel c = channels.get(channel);
			if (c != null) {
				chatter.join(c, null, false);
			}
		}
		Channel activeChannel = channels.get(getNode(path + ".active-channel").getString());
		if (activeChannel == null) {
			activeChannel = channels.getDefault();
		}
		chatter.join(activeChannel);
		chatter.setAutoSave(true);
		chatters.add(chatter);
		return chatter;
	}

	public void save(Chatter chatter) {
		String path = "chatters." + chatter;
		getNode(path + ".channels").setValue(ChatConfiguration.getNames(chatter.getChannels()));
		getNode(path + ".invites").setValue(ChatConfiguration.getNames(chatter.getInvites()));
		getNode(path + ".active-channel").setValue(chatter.getActiveChannel().getName());
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
