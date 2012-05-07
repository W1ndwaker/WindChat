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
package net.windwaker.chat;

import net.windwaker.chat.data.Configuration;
import org.spout.api.Spout;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Chat plugin for the Spout voxel software.
 *
 * @author Windwaker
 */
public class WindChat extends CommonPlugin {
	private final ChatLogger logger = ChatLogger.getInstance();
	private final Configuration config = new Configuration();
	private static final Chat chat = new Chat();

	@Override
	public void onEnable() {

		// Load config
		config.load();

		// Register events
		Spout.getEventManager().registerEvents(new EventListener(), this);

		// Load chat
		chat.initialize();

		// Hello world!
		logger.info("WindChat v" + getDescription().getVersion() + " by " + getDescription().getAuthors() + " enabled!");
	}

	@Override
	public void onDisable() {
		logger.info("WindChat v" + getDescription().getVersion() + " by " + getDescription().getAuthors() + " disabled.");
	}

	public static Chat getChat() {
		return chat;
	}
}
