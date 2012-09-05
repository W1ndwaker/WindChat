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

public class ChannelConfiguration extends YamlConfiguration {
	private final Set<Channel> channels = new HashSet<Channel>();

	public ChannelConfiguration() {
		super(new File(WindChat.getInstance().getDataFolder(), "channels.yml"));
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

	public void load(String name) {
		Channel channel = new Channel(name);
		String path = "channels." + name;
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
		channels.add(channel);
	}

	@Override
	public void save() {
		try {
			super.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void set(String path, Object value) {
		getNode(path).setValue(value);
		save();
	}

	public void setInviteOnly(String channel, boolean inviteOnly) {
		set("channels." + channel + ".invite-only", inviteOnly);
	}

	public void setFormat(String channel, ChatArguments format) {
		set("channels." + channel + ".format", format.toFormatString());
	}

	public void setJoinMessage(String channel, ChatArguments joinMessage) {
		set("channels." + channel + ".join-message", joinMessage.toFormatString());
	}

	public void setLeaveMessage(String channel, ChatArguments leaveMessage) {
		set("channels." + channel + ".leave-message", leaveMessage.toFormatString());
	}

	public List<String> getBanned(String channel) {
		return getNode("channels." + channel + ".banned").getStringList();
	}

	public void setBanned(String channel, List<String> banned) {
		set("channels." + channel + ".banned", banned);
	}

	public void addBanned(String channel, String name) {
		List<String> banned = getBanned(channel);
		if (!banned.contains(name)) {
			banned.add(name);
		}
		setBanned(channel, banned);
	}

	public void removeBanned(String channel, String name) {
		List<String> banned = getBanned(channel);
		banned.remove(name);
		setBanned(channel, banned);
	}

	public void setBanMessage(String channel, ChatArguments banMessage) {
		set("channels." + channel + ".ban-message", banMessage.toFormatString());
	}

	public List<String> getListeners(String channel) {
		return getNode("channels." + channel + ".listeners").getStringList();
	}

	public void setListeners(String channel, List<String> listeners) {
		set("channels." + channel + ".listeners", listeners);
	}

	public void setPassword(String channel, String password) {
		set("channels." + channel + ".password", password);
	}

	public void addListener(String channel, String listener) {
		List<String> listeners = getListeners(channel);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		setListeners(channel, listeners);
	}

	public void removeListener(String channel, String listener) {
		List<String> listeners = getListeners(channel);
		listeners.remove(listener);
		setListeners(channel, listeners);
	}

	public Set<Channel> get() {
		return channels;
	}

	public void add(String channel) {
		String path = "channels." + channel;
		getNode(path + ".invite-only").setValue(false);
		getNode(path + ".format").setValue("[" + channel + "] {MESSAGE}");
		getNode(path + ".join-message").setValue("{{BRIGHT_GREEN}}You have joined " + channel + ".");
		getNode(path + ".leave-message").setValue("{{RED}}You have left " + channel + ".");
		getNode(path + ".ban-message").setValue("{{RED}}You have been {{BOLD}}banned{{RESET}}{{RED}} from " + channel + "!");
		save();
		load(channel);
	}

	public Channel getDefault() {
		return get(ChatConfiguration.DEFAULT_CHANNEL.getString());
	}

	public Channel get(String channel) {
		for (Channel c : channels) {
			if (c.getName().equalsIgnoreCase(channel)) {
				return c;
			}
		}
		return null;
	}
}
