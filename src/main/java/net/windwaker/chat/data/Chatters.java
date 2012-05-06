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

import net.windwaker.chat.Chat;
import net.windwaker.chat.ChatLogger;
import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.player.Player;
import org.spout.api.util.config.yaml.YamlConfiguration;

import java.io.File;

public class Chatters {
	private final YamlConfiguration data = new YamlConfiguration(new File("plugins/WindChat/chatters.yml"));
	private final ChatLogger logger = ChatLogger.getInstance();
	
	public void load() {
		try {
			data.load();
		} catch (ConfigurationException e) {
			logger.severe("Failed to load chatter data: " + e.getMessage());
		}
	}
	
	public void login(Player player) {
		Chat chat = WindChat.getChat();
		Channel channel = chat.getChannel(data.getNode("chatters." + player.getName() + ".channel").getString());
		if (channel == null) {
			channel = chat.getChannel(Configuration.DEFAULT_CHANNEL.getString());
		}

		channel.addChatter(player);
	}
}
