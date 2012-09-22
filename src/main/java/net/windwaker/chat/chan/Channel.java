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
package net.windwaker.chat.chan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private final WindChat plugin;
	private final String name;
	private final Set<String> banned = new HashSet<String>(), muted = new HashSet<String>();
	private final Set<Chatter> listeners = new HashSet<Chatter>();
	private final Map<String, String> censoredWords = new HashMap<String, String>();
	private IrcBot bot;
	private int radius;
	private String password;
	private boolean autoSave, inviteOnly, ircEnabled;
	private ChatArguments joinMessage, leaveMessage, format, banMessage;

	/**
	 * Constructs a new channel object
	 * @param name
	 */
	public Channel(WindChat plugin, String name) {
		this.name = name;
		this.plugin = plugin;
	}

	public boolean isIrcEnabled() {
		return ircEnabled;
	}

	public void setIrcEnabled(boolean ircEnabled) {
		this.ircEnabled = ircEnabled;
	}

	public String getBotName() {
		if (bot == null) {
			return null;
		}
		return bot.getName();
	}

	public String getBotServer() {
		if (bot == null) {
			return null;
		}
		return bot.getServer();
	}

	public String getBotChannel() {
		if (bot == null) {
			return null;
		}
		return bot.getChannel();
	}

	public void connectToIrc(IrcBot bot) {
		if (!ircEnabled) {
			return;
		}
		if (this.bot != null && this.bot.isConnected()) {
			bot.disconnect();
		}
		if (bot == null) {
			throw new IllegalArgumentException("Cannot connect a null bot.");
		}
		if (bot.isConnected()) {
			throw new IllegalStateException("Bot is already connected to IRC.");
		}
		this.bot = bot;
		bot.connect(this);
	}

	/**
	 * Whether the channel saves every time a property is set
	 * @return true if auto save
	 */
	public boolean isAutoSave() {
		return autoSave;
	}

	/**
	 * Sets whether the channel should save every time a property is set
	 * @param autoSave
	 */
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
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
		this.radius = radius;
		if (autoSave) {
			save();
		}
	}

	/**
	 * Censors a word in the channel
	 * @param word to censor
	 */
	public void censor(String word) {
		String replacement = "";
		for (int i = 0; i < word.length(); i++) {
			replacement += "*";
		}
		censor(word, replacement);
	}

	/**
	 * Censors a word in the channel
	 * @param word
	 * @param replacement
	 */
	public void censor(String word, String replacement) {
		word = word.toLowerCase();
		censoredWords.put(word, replacement);
		if (autoSave) {
			save();
		}
	}

	/**
	 * Gets the censored words
	 * @return censored words
	 */
	public Map<String, String> getCensoredWords() {
		return censoredWords;
	}

	/**
	 * Whether the specified word is censored
	 * @param word
	 * @return true if censored
	 */
	public boolean isCensored(String word) {
		return censoredWords.containsKey(word);
	}

	/**
	 * Stops a chatter from being able to chat in the channel.
	 * @param name
	 */
	public void mute(String name) {
		muted.add(name);
		if (autoSave) {
			save();
		}
	}

	/**
	 * Allows the chatter to chat in the channel.
	 * @param name
	 */
	public void unmute(String name) {
		muted.remove(name);
		if (autoSave) {
			save();
		}
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
		banned.add(name);
		if (autoSave) {
			save();
		}
	}

	/**
	 * Unbans a name from the channel.
	 * @param name
	 */
	public void unban(String name) {
		banned.remove(name);
		if (autoSave) {
			save();
		}
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
		this.banMessage = banMessage;
		if (autoSave) {
			save();
		}
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
		this.inviteOnly = inviteOnly;
		if (autoSave) {
			save();
		}
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
		this.format = format;
		if (autoSave) {
			save();
		}
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
		this.joinMessage = joinMessage;
		if (autoSave) {
			save();
		}
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
		this.leaveMessage = leaveMessage;
		if (autoSave) {
			save();
		}
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
		this.password = password;
		if (autoSave) {
			save();
		}
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
	public Set<Chatter> getListeners() {
		return listeners;
	}

	/**
	 * Adds a listener to the channel
	 * @param chatter
	 */
	public void addListener(Chatter chatter) {
		listeners.add(chatter);
		if (autoSave) {
			save();
		}
	}

	/**
	 * Removes a listener from the channel.
	 * @param chatterName
	 */
	public void removeListener(String chatterName) {
		listeners.remove(chatterName);
		if (autoSave) {
			save();
		}
	}

	/**
	 * Broadcasts a message to all listeners of the channel.
	 * @param message
	 */
	public void broadcast(Chatter sender, ChatArguments message) {
		if (format.hasPlaceholder(Placeholders.MESSAGE)) {
			format.setPlaceHolder(Placeholders.MESSAGE, message);
		}
		for (Chatter chatter : listeners) {
			// out of range
			if (!chatter.canHear(sender, this)) {
				continue;
			}
			chatter.getParent().sendMessage(format);
		}
		plugin.getChatLogger().log(format.getPlainString());
	}

	/**
	 * Censors a message from the censored word list.
	 * @param args
	 * @return censored message
	 */
	public ChatArguments censorMessage(ChatArguments args) {
		String str = args.asString();
		for (String word : str.split(" ")) {
			if (censoredWords.containsKey(word.toLowerCase())) {
				str = str.replace(word, censoredWords.get(word.toLowerCase()));
			}
		}
		return ChatArguments.fromFormatString(str);
	}

	/**
	 * Saves the channel to disk
	 */
	public void save() {
		plugin.getChannels().save(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
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
