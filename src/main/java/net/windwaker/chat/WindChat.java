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

import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;
import net.windwaker.chat.command.ChannelCommand;
import net.windwaker.chat.config.Channels;
import net.windwaker.chat.config.Chatters;
import net.windwaker.chat.config.Settings;

import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.player.Player;
import org.spout.api.plugin.CommonPlugin;

/**
 * Chat plugin for the Spout voxel software.
 * @author Windwaker
 */
public class WindChat extends CommonPlugin {
	private final ChatLogger logger = ChatLogger.getInstance();
	private final Settings config = new Settings();
	private final Channels channels = new Channels();
	private final Chatters chatters = new Chatters();

	@Override
	public void onReload() {
		// Save and load all the config
		reloadData();
		logger.info("WindChat v" + getDescription().getVersion() + " by "
				+ getDescription().getAuthors() + " reloaded!");
	}

	@Override
	public void onEnable() {
		// Set the plugin of Chat
		Chat.setPlugin(this);
		// Load config
		loadData();
		// Register events
		Spout.getEventManager().registerEvents(new EventListener(), this);
		// Register commands
		registerCommands();
		logger.info("WindChat v" + getDescription().getVersion() + " by "
				+ getDescription().getAuthors() + " enabled!");
	}

	@Override
	public void onDisable() {
		saveData();
		logger.info("WindChat v" + getDescription().getVersion() + " by "
				+ getDescription().getAuthors() + " disabled.");
	}

	public void onLogin(Player player) {
		chatters.onLogin(player);
	}

	public Channel getChannel(String name) {
		return channels.get(name);
	}

	public Chatter getChatter(String name) {
		return chatters.get(name);
	}

	private void registerCommands() {
		CommandRegistrationsFactory<Class<?>> commandRegFactory =
				new AnnotatedCommandRegistrationFactory(new SimpleInjector(),
						new SimpleAnnotatedCommandExecutorFactory());
		getEngine().getRootCommand().addSubCommands(
				this, ChannelCommand.class, commandRegFactory);
	}

	private void loadData() {
		config.load();
		channels.load();
		chatters.load();
	}

	private void saveData() {
		config.save();
		channels.save();
		chatters.save();
	}

	private void reloadData() {
		saveData();
		loadData();
	}
}
