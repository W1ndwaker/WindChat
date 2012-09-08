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
package net.windwaker.chat.channel;

import java.util.HashSet;
import java.util.Set;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.util.Placeholders;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.util.Named;

/**
 * Represents a channel of chat.
 */
public class Channel implements Named {
	/**
	 * Singleton instance of the plugin
	 */
	private final WindChat plugin = WindChat.getInstance();
	/**
	 * Name of the channel
	 */
	private final String name;
	/**
	 * Set of names of chatters who listen to the channel
	 */
	private final Set<String> listeners = new HashSet<String>();
	/**
	 * Set of banned names from the channel
	 */
	private final Set<String> banned = new HashSet<String>();
	/**
	 * Set off muted names in the channel
	 */
	private final Set<String> muted = new HashSet<String>();
	/**
	 * The radius that the channel can be heard from.
	 */
	private int radius;
	/**
	 * Password for the channel.
	 */
	private String password;
	/**
	 * Whether the channel requires an invitation for entrance.
	 */
	private boolean inviteOnly;
	/**
	 * The join message, leave message, ban message, and general format of the channel.
	 */
	private ChatArguments joinMessage, leaveMessage, format, banMessage;

	/**
	 * Constructs a new channel object
	 * @param name
	 */
	public Channel(String name) {
		this.name = name;
	}

	/**
	 * Gets the radius (in blocks) that the channel can be heard from the original sender.
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius (in blocks) that the channel can be heard from the original sender.
	 * @param radius
	 */
	public void setRadius(int radius) {
		plugin.getChannels().setRadius(name, radius);
		this.radius = radius;
	}

	/**
	 * Stops a chatter from being able to chat in the channel.
	 * @param name
	 */
	public void mute(String name) {
		plugin.getChannels().addMuted(this.name, name);
		muted.add(name);
	}

	/**
	 * Allows the chatter to chat in the channel.
	 * @param name
	 */
	public void unmute(String name) {
		plugin.getChannels().removeMuted(this.name, name);
		muted.remove(name);
	}

	/**
	 * Gets the names of all muted chatters.
	 * @return all muted
	 */
	public Set<String> getMuted() {
		return muted;
	}

	/**
	 * Whether or not the name is muted.
	 * @param name
	 * @return true if muted
	 */
	public boolean isMuted(String name) {
		return muted.contains(name);
	}

	/**
	 * Bans a name from the channel
	 * @param name of chatter
	 */
	public void ban(String name) {
		ban(name, true);
	}

	/**
	 * Bans a name from the channel
	 * @param name of chatter
	 * @param kick whether to kick if online
	 */
	public void ban(String name, boolean kick) {
		ban(name, kick, new ChatArguments(ChatStyle.RED, "Banned from ", name));
	}

	/**
	 * Bans a name from the channel
	 * @param name of chatter
	 * @param kick whether to kick if online
	 * @param reason for kick
	 */
	public void ban(String name, boolean kick, ChatArguments reason) {
		if (kick) {
			Chatter chatter = plugin.getChatters().get(name);
			if (chatter == null) {
				return;
			}
			chatter.kick(this, reason);
		}
		plugin.getChannels().addBanned(this.name, name);
		banned.add(name);
	}

	/**
	 * Unbans a name from the channel.
	 * @param name
	 */
	public void unban(String name) {
		plugin.getChannels().removeBanned(this.name, name);
		banned.remove(name);
	}

	/**
	 * Gets a set of banned names from the channel.
	 * @return name list
	 */
	public Set<String> getBanned() {
		return banned;
	}

	/**
	 * Whether or not a name is banned from the channel.
	 * @param name
	 * @return true if banned
	 */
	public boolean isBanned(String name) {
		return banned.contains(name);
	}

	/**
	 * Gets the message displayed when being rejected from entering a server because of being banned.
	 * @return ban message
	 */
	public ChatArguments getBanMessage() {
		return banMessage;
	}

	/**
	 * Sets the message displayed when being rejected from entering a server because of being banned.
	 * @param banMessage
	 */
	public void setBanMessage(ChatArguments banMessage) {
		plugin.getChannels().setBanMessage(name, banMessage);
		this.banMessage = banMessage;
	}

	/**
	 * Whether or not the channel requires an invitation to join.
	 * @return true if by invitation only.
	 */
	public boolean isInviteOnly() {
		return inviteOnly;
	}

	/**
	 * Sets if the channel requires an invitation to join.
	 * @param inviteOnly
	 */
	public void setInviteOnly(boolean inviteOnly) {
		plugin.getChannels().setInviteOnly(name, inviteOnly);
		this.inviteOnly = inviteOnly;
	}

	/**
	 * Gets the general format of the channel.
	 * @return format
	 */
	public ChatArguments getFormat() {
		return format;
	}

	/**
	 * Sets the general format of the channel.
	 * @param format
	 */
	public void setFormat(ChatArguments format) {
		plugin.getChannels().setFormat(name, format);
		this.format = format;
	}

	/**
	 * Gets the message displayed when joining the channel
	 * @return join message
	 */
	public ChatArguments getJoinMessage() {
		return joinMessage;
	}

	/**
	 * Sets the message displayed when joining the channel
	 * @param joinMessage
	 */
	public void setJoinMessage(ChatArguments joinMessage) {
		plugin.getChannels().setJoinMessage(name, joinMessage);
		this.joinMessage = joinMessage;
	}

	/**
	 * Gets the message displayed when leaving the channel.
	 * @return leave message
	 */
	public ChatArguments getLeaveMessage() {
		return leaveMessage;
	}

	/**
	 * Sets the message displayed when leaving the channel.
	 * @param leaveMessage
	 */
	public void setLeaveMessage(ChatArguments leaveMessage) {
		plugin.getChannels().setLeaveMessage(name, leaveMessage);
		this.leaveMessage = leaveMessage;
	}

	/**
	 * Gets the password of the server required to enter.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password required to enter. Set to null to disable password.
	 * @param password
	 */
	public void setPassword(String password) {
		plugin.getChannels().setPassword(name, password);
		this.password = password;
	}

	/**
	 * Whether or not the channel requires a password for entrance.
	 * @return true if requires password
	 */
	public boolean hasPassword() {
		return password != null;
	}

	/**
	 * Whether or not a name is listening to the channel
	 * @param name
	 * @return true if in listening set
	 */
	public boolean isListening(String name) {
		return listeners.contains(name);
	}

	/**
	 * Gets set of listeners
	 * @return set of listeners
	 */
	public Set<String> getListeners() {
		return listeners;
	}

	/**
	 * Adds a listener to the channel
	 * @param chatterName
	 */
	public void addListener(String chatterName) {
		plugin.getChannels().addListener(name, chatterName);
		listeners.add(chatterName);
	}

	/**
	 * Removes a listener from the channel.
	 * @param chatterName
	 */
	public void removeListener(String chatterName) {
		plugin.getChannels().removeListener(name, chatterName);
		listeners.remove(chatterName);
	}

	/**
	 * Broadcasts a message to all listeners of the channel.
	 * @param message
	 */
	public void broadcast(Chatter sender, ChatArguments message) {
		if (format.hasPlaceholder(Placeholders.MESSAGE)) {
			format.setPlaceHolder(Placeholders.MESSAGE, message);
		}
		for (String n : listeners) {
			Chatter chatter = plugin.getChatters().get(n);
			if (chatter == null) {
				continue;
			}
			// out of range
			if (sender.getParent().getPosition().getDistance(chatter.getParent().getPosition()) > radius && radius != 0) {
				continue;
			}
			chatter.getParent().sendMessage(format);
		}
		plugin.getLogger().info(format.getPlainString());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Channel && ((Channel) obj).getName().equalsIgnoreCase(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
