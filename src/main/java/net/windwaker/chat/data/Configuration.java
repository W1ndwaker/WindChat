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

import java.io.File;

import net.windwaker.chat.ChatLogger;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class Configuration extends ConfigurationHolderConfiguration {
	public static final ConfigurationHolder DEFAULT_CHANNEL = new ConfigurationHolder("spout", "default-channel");
	public static final ConfigurationHolder DEFAULT_FORMAT = new ConfigurationHolder("%player%: %message%", "default-format");
	public static final ConfigurationHolder DEFAULT_JOIN_MESSAGE = new ConfigurationHolder("&3%player%&7 has joined the game.", "default-join-message");
	private final ChatLogger logger = ChatLogger.getInstance();

	public Configuration() {
		super(new YamlConfiguration(new File("plugins/WindChat/config.yml")));
	}

	public void load() {
		try {
			super.load();
			super.save();
		} catch (ConfigurationException e) {
			logger.severe("Failed to load configuration: " + e.getMessage());
		}
	}
}
