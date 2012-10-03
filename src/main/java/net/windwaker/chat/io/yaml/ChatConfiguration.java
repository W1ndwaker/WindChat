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

import net.windwaker.chat.WindChat;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

/**
 * Represents the general settings of the plugin.
 */
public class ChatConfiguration extends ConfigurationHolderConfiguration {
	/**
	 * The default channel for players to join.
	 */
	public static final ConfigurationHolder DEFAULT_CHANNEL = new ConfigurationHolder("spout", "default-channel");
	/**
	 * The default chat format to fall back on
	 * @see {@link net.windwaker.chat.util.Format#CHAT}
	 */
	public static final ConfigurationHolder DEFAULT_CHAT_FORMAT = new ConfigurationHolder("{NAME}: {MESSAGE}", "default-format");
	/**
	 * The default join message to fall back on
	 * @see {@link net.windwaker.chat.util.Format#JOIN_MESSAGE}
	 */
	public static final ConfigurationHolder DEFAULT_JOIN_MESSAGE_FORMAT = new ConfigurationHolder("{{DARK_CYAN}}{NAME} {{GRAY}}has joined the game.", "default-join-message");
	/**
	 * The default leave message to fall back on
	 * @see {@link net.windwaker.chat.util.Format#LEAVE_MESSAGE}
	 */
	public static final ConfigurationHolder DEFAULT_LEAVE_MESSAGE_FORMAT = new ConfigurationHolder("{{DARK_CYAN}}{NAME} {{GRAY}}has left the game. ({QUIT_MESSAGE})", "default-leave-message");
	/**
	 * The time zone to use with the 'date' cmd.
	 */
	public static final ConfigurationHolder TIME_ZONE = new ConfigurationHolder("default", "time-zone");
	/**
	 * The date format.
	 */
	public static final ConfigurationHolder DATE_FORMAT = new ConfigurationHolder("MM/dd/yyyy", "date-format");
	/**
	 * The time format.
	 */
	public static final ConfigurationHolder TIME_FORMAT = new ConfigurationHolder("HH:mm:ss", "time-format");
	/**
	 * Whether chat should be logged to disk
	 */
	public static final ConfigurationHolder LOG_CHAT = new ConfigurationHolder(true, "log-chat");

	/**
	 * Constructs a new ChatConfiguration at 'plugins/WindChat/config.yml'
	 */
	public ChatConfiguration(WindChat plugin) {
		super(new YamlConfiguration(new File(plugin.getDataFolder(), "config.yml")));
	}

	@Override
	public void load() {
		try {
			super.load();
			super.save();
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
