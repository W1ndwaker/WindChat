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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;

import org.spout.api.chat.ChatArguments;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

/**
 * Represents a collection of channels.
 */
public class ChannelConfiguration extends YamlConfiguration {
	/**
	 * The actual set of channels for the plugin.
	 */
	private final Set<Channel> channels = new HashSet<Channel>();

	/**
	 * Constructs a new ChannelConfiguration at 'plugins/WindChat/channels.yml'.
	 */
	public ChannelConfiguration() {
		super(new File(WindChat.getInstance().getDataFolder(), "channels.yml"));
	}

	/**
	 * Loads a channel from the specified name.
	 * @param name to load
	 */
	public void load(String name) {
		Channel channel = new Channel(name);
		String path = "channels." + name;
		channel.setRadius(getNode(path + ".radius").getInt());
		channel.setInviteOnly(getNode(path + ".invite-only").getBoolean());
		channel.setPassword(getNode(path + ".password").getString());
		channel.setJoinMessage(ChatArguments.fromFormatString(getNode(path + ".join-message").getString()));
		channel.setLeaveMessage(ChatArguments.fromFormatString(getNode(path + ".leave-message").getString()));
		channel.setFormat(ChatArguments.fromFormatString(getNode(path + ".format").getString()));
		channel.setBanMessage(ChatArguments.fromFormatString(getNode(path + ".ban-message").getString()));
		for (String n : getNode(path + ".listeners").getStringList()) {
			channel.addListener(n);
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
		channels.add(channel);
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
		save();
		load(channel);
	}

	/**
	 * Sets a value in memory and saves to disk.
	 * @param path to set
	 * @param value to use
	 */
	public void set(String path, Object value) {
		getNode(path).setValue(value);
		save();
	}

	/**
	 * Adds a censored word to the channel
	 * @param channel
	 * @param word
	 * @param replacement
	 */
	public void addCensoredWord(String channel, String word, String replacement) {
		set("channels." + channel + ".censored-words." + word, replacement);
	}

	/**
	 * Sets the radius of the specified channel
	 * @param channel
	 * @param radius
	 */
	public void setRadius(String channel, int radius) {
		set("channels." + channel + ".radius", radius);
	}

	/**
	 * Gets a list of muted names in the specified channel
	 * @param channel
	 * @return list of muted names
	 */
	public List<String> getMuted(String channel) {
		return getNode("channels." + channel + ".muted").getStringList();
	}

	/**
	 * Sets the list of muted names in the specified channel
	 * @param channel
	 * @param muted
	 */
	public void setMuted(String channel, List<String> muted) {
		set("channels." + channel + ".muted", muted);
	}

	/**
	 * Adds a muted name to the list from the specified channel
	 * @param channel
	 * @param name
	 */
	public void addMuted(String channel, String name) {
		List<String> muted = getMuted(channel);
		if (!muted.contains(name)) {
			muted.add(name);
		}
		setMuted(channel, muted);
	}

	/**
	 * Removes a muted name from the list in the channel
	 * @param channel
	 * @param name
	 */
	public void removeMuted(String channel, String name) {
		List<String> muted = getMuted(channel);
		muted.remove(name);
		setMuted(channel, muted);
	}

	/**
	 * Saves the invite only status to disk.
	 * @param channel
	 * @param inviteOnly
	 */
	public void setInviteOnly(String channel, boolean inviteOnly) {
		set("channels." + channel + ".invite-only", inviteOnly);
	}

	/**
	 * Saves the format status to disk.
	 * @param channel
	 * @param format
	 */
	public void setFormat(String channel, ChatArguments format) {
		set("channels." + channel + ".format", format.toFormatString());
	}

	/**
	 * Saves the join message to disk.
	 * @param channel
	 * @param joinMessage
	 */
	public void setJoinMessage(String channel, ChatArguments joinMessage) {
		set("channels." + channel + ".join-message", joinMessage.toFormatString());
	}

	/**
	 * Saves the leave message to disk
	 * @param channel
	 * @param leaveMessage
	 */
	public void setLeaveMessage(String channel, ChatArguments leaveMessage) {
		set("channels." + channel + ".leave-message", leaveMessage.toFormatString());
	}

	/**
	 * Gets the banned names of a channel.
	 * @param channel
	 * @return ban list
	 */
	public List<String> getBanned(String channel) {
		return getNode("channels." + channel + ".banned").getStringList();
	}

	/**
	 * Sets the banned names of a channel.
	 * @param channel
	 * @param banned
	 */
	public void setBanned(String channel, List<String> banned) {
		set("channels." + channel + ".banned", banned);
	}

	/**
	 * Adds a banned name to the ban list of a channel
	 * @param channel
	 * @param name
	 */
	public void addBanned(String channel, String name) {
		List<String> banned = getBanned(channel);
		if (!banned.contains(name)) {
			banned.add(name);
		}
		setBanned(channel, banned);
	}

	/**
	 * Removes a banned name from the ban list of a channel
	 * @param channel
	 * @param name
	 */
	public void removeBanned(String channel, String name) {
		List<String> banned = getBanned(channel);
		banned.remove(name);
		setBanned(channel, banned);
	}

	/**
	 * Saves the ban message to disk.
	 * @param channel
	 * @param banMessage
	 */
	public void setBanMessage(String channel, ChatArguments banMessage) {
		set("channels." + channel + ".ban-message", banMessage.toFormatString());
	}

	/**
	 * Gets the list of listeners for a channel
	 * @param channel
	 * @return listener list
	 */
	public List<String> getListeners(String channel) {
		return getNode("channels." + channel + ".listeners").getStringList();
	}

	/**
	 * Sets the list of listeners for a channel
	 * @param channel
	 * @param listeners
	 */
	public void setListeners(String channel, List<String> listeners) {
		set("channels." + channel + ".listeners", listeners);
	}

	/**
	 * Saves the password of a channel.
	 * @param channel
	 * @param password
	 */
	public void setPassword(String channel, String password) {
		set("channels." + channel + ".password", password);
	}

	/**
	 * Adds a listener to the listener list of a channel.
	 * @param channel
	 * @param listener
	 */
	public void addListener(String channel, String listener) {
		List<String> listeners = getListeners(channel);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		setListeners(channel, listeners);
	}

	/**
	 * Removes a listener from the listener list of a channel.
	 * @param channel
	 * @param listener
	 */
	public void removeListener(String channel, String listener) {
		List<String> listeners = getListeners(channel);
		listeners.remove(listener);
		setListeners(channel, listeners);
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
