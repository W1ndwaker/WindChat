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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.chan.Chatter;
import net.windwaker.chat.chan.IrcBot;

import org.spout.api.chat.ChatArguments;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

/**
 * Represents a collection of channels.
 */
public class ChannelConfiguration extends YamlConfiguration {
	private final WindChat plugin;
	private final Set<Channel> channels = new HashSet<Channel>();

	/**
	 * Constructs a new ChannelConfiguration at 'plugins/WindChat/channels.yml'.
	 */
	public ChannelConfiguration(WindChat plugin) {
		super(new File(plugin.getDataFolder(), "channels.yml"));
		this.plugin = plugin;
	}

	/**
	 * Loads a channel from the specified name.
	 * @param name to load
	 */
	public Channel load(String name) {
		System.out.println("Loading channel: " + name);
		Channel channel = new Channel(plugin, name);
		String path = "channels." + name;
		// Don't write everything back to disk we are loading
		channel.setAutoSave(false);
		// Set properties
		channel.setRadius(getNode(path + ".radius").getInt());
		channel.setInviteOnly(getNode(path + ".invite-only").getBoolean());
		channel.setPassword(getNode(path + ".password").getString());
		channel.setJoinMessage(ChatArguments.fromFormatString(getNode(path + ".join-message").getString()));
		channel.setLeaveMessage(ChatArguments.fromFormatString(getNode(path + ".leave-message").getString()));
		channel.setFormat(ChatArguments.fromFormatString(getNode(path + ".format").getString()));
		channel.setBanMessage(ChatArguments.fromFormatString(getNode(path + ".ban-message").getString()));
		channel.setIrcEnabled(getNode(path + ".irc.enabled").getBoolean());
		IrcBot bot = plugin.getBots().get(getNode(path + ".irc.bot").getString());
		if (bot != null) {
			channel.connectToIrc(bot);
		}
		for (String b : getNode(path + ".banned").getStringList()) {
			channel.ban(b, false);
		}
		for (String m : getNode(path + ".muted").getStringList()) {
			channel.mute(m);
		}
		for (String c : getNode(path + ".censored-words").getKeys(false)) {
			channel.censor(c, getNode(path + ".censored-words." + c).getString());
		}
		// Turn auto-save back on
		channel.setAutoSave(true);
		channels.add(channel);
		return channel;
	}

	public void postLoad(Channel channel) {
		String path = "channels." + channel.getName();
		for (String n : getNode(path + ".listeners").getStringList()) {
			Chatter chatter = plugin.getChatters().get(n);
			if (chatter == null) {
				continue;
			}
			channel.addListener(chatter);
		}
	}

	public void postLoad() {
		for (Channel channel : channels) {
			postLoad(channel);
		}
	}

	/**
	 * Saves a channel to disk
	 * @param channel to save
	 */
	public void save(Channel channel) {
		String path = "channels." + channel.getName();
		List<String> listeners = new ArrayList<String>();
		for (Chatter chatter : channel.getListeners()) {
			listeners.add(chatter.getParent().getName());
		}
		getNode(path + ".listeners").setValue(channel.getListeners());
		getNode(path + ".banned").setValue(channel.getBanned());
		getNode(path + ".muted").setValue(channel.getMuted());
		for (Entry<String, String> word : channel.getCensoredWords().entrySet()) {
			getNode(path + ".censored-words." + word.getKey()).setValue(word.getValue());
		}
		getNode(path + ".radius").setValue(channel.getRadius());
		getNode(path + ".password").setValue(channel.getPassword());
		getNode(path + ".invite-only").setValue(channel.isInviteOnly());
		getNode(path + ".join-message").setValue(channel.getJoinMessage().toFormatString());
		getNode(path + ".leave-message").setValue(channel.getLeaveMessage().toFormatString());
		getNode(path + ".format").setValue(channel.getFormat().toFormatString());
		getNode(path + ".ban-message").setValue(channel.getBanMessage().toFormatString());
		getNode(path + ".irc.enabled").setValue(channel.isIrcEnabled());
		getNode(path + ".irc.bot").setValue(channel.getBotName());
		getNode(path + ".irc.server").setValue(channel.getBotServer());
		getNode(path + ".irc.channel").setValue(channel.getBotChannel());
		save();
	}

	/**
	 * Adds and loads a new channel from the specified name.
	 * @param channel
	 */
	public void add(String channel) {
		String path = "channels." + channel;
		getNode(path + ".radius").setValue(0);
		getNode(path + ".invite-only").setValue(false);
		getNode(path + ".format").setValue("[" + channel + "] {MESSAGE}");
		getNode(path + ".join-message").setValue("{{BRIGHT_GREEN}}You have joined " + channel + ".");
		getNode(path + ".leave-message").setValue("{{RED}}You have left " + channel + ".");
		getNode(path + ".ban-message").setValue("{{RED}}You have been {{BOLD}}banned{{RESET}}{{RED}} from " + channel + "!");
		getNode(path + ".irc.enabled").setValue(false);
		getNode(path + ".irc.bot").setValue("ChatterBot");
		getNode(path + ".irc.server").setValue("irc.esper.net");
		getNode(path + ".irc.channel").setValue("#windwaker");
		save();
		load(channel);
	}

	/**
	 * Gets the channel set.
	 * @return set of channels.
	 */
	public Set<Channel> get() {
		return channels;
	}

	/**
	 * Gets the default {@link Channel} of the plugin configured in {@link ChatConfiguration}.
	 * @return default channel
	 */
	public Channel getDefault() {
		return get(ChatConfiguration.DEFAULT_CHANNEL.getString());
	}

	/**
	 * Gets a channel from the specified name.
	 * @param channel name
	 * @return channel object
	 */
	public Channel get(String channel) {
		for (Channel c : channels) {
			if (c.getName().equalsIgnoreCase(channel)) {
				return c;
			}
		}
		return null;
	}

	@Override
	public void load() {
		try {
			super.load();
			if (!getNode("channels").isAttached()) {
				getNode("channels.spout.format").setValue("[{{DARK_CYAN}}spout{{WHITE}}] {MESSAGE}");
				getNode("channels.spout.invite-only").setValue(false);
				getNode("channels.spout.password").setValue("unleashtheflow");
				getNode("channels.spout.join-message").setValue("{{BRIGHT_GREEN}}You have joined Spout.");
				getNode("channels.spout.leave-message").setValue("{{RED}}You have left Spout.");
				getNode("channels.spout.ban-message").setValue("{{RED}}You have been {{BOLD}}banned{{RESET}}{{RED}} from Spout!");
				getNode("channels.spout.listeners").setValue(Arrays.asList("Wulfspider", "Afforess", "alta189", "Top_Cat"));
				getNode("channels.spout.irc.enabled").setValue(false);
				getNode("channels.spout.irc.bot").setValue("ChatterBot");
				getNode("channels.spout.irc.server").setValue("irc.esper.net");
				getNode("channels.spout.irc.channel").setValue("#windwaker");
				save();
			}
			for (String name : getNode("channels").getKeys(false)) {
				load(name);
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
