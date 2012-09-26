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

import net.windwaker.chat.cmd.ChannelCommand;
import net.windwaker.chat.cmd.ChatCommands;
import net.windwaker.chat.handler.DateHandler;
import net.windwaker.chat.handler.LocalChatHandler;
import net.windwaker.chat.io.ChatLogger;
import net.windwaker.chat.io.yaml.BotConfiguration;
import net.windwaker.chat.io.yaml.ChannelConfiguration;
import net.windwaker.chat.io.yaml.ChatConfiguration;
import net.windwaker.chat.io.yaml.ChatterConfiguration;
import net.windwaker.chat.util.DefaultPermissionNodes;

import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.permissions.DefaultPermissions;
import org.spout.api.plugin.CommonPlugin;

/**
 * Chat plugin for the Spout platform.
 * @author Windwaker
 */
public class WindChat extends CommonPlugin {
	private final DateHandler dateHandler = new DateHandler();
	private final ChatLogger logger = new ChatLogger(this);
	private ChatConfiguration config;
	private ChatterConfiguration chatters;
	private ChannelConfiguration channels;
	private BotConfiguration bots;

	/**
	 * Gets the collection of {@link net.windwaker.chat.chan.Chatter}s on the 
	 * plugin.
	 * 
	 * @return collection of chatters
	 */
	public ChatterConfiguration getChatters() {
		return chatters;
	}

	/**
	 * Gets the collection of {@link net.windwaker.chat.chan.Channel}s on the 
	 * plugin.
	 * 
	 * @return collection of channels
	 */
	public ChannelConfiguration getChannels() {
		return channels;
	}

	/**
	 * Gets the collection of loaded {@link net.windwaker.chat.chan.irc.IrcBot}
	 * on the plugin
	 * 
	 * @return loaded bots
	 */
	public BotConfiguration getBots() {
		return bots;
	}

	/**
	 * Gets the {@link DateHandler} of the plugin which handles formatting 
	 * dates.
	 * 
	 * @return date handler
	 */
	public DateHandler getDateHandler() {
		return dateHandler;
	}

	/**
	 * Gets the {@link ChatLogger} of the plugin.
	 * 
	 * @return chat logger
	 */
	public ChatLogger getChatLogger() {
		return logger;
	}

	@Override
	public void onReload() {
		config.load();
		chatters.load();
		channels.load();
	}

	@Override
	public void onEnable() {
		
		// Load config
		config = new ChatConfiguration(this);
		config.load();
		// Load bots
		bots = new BotConfiguration(this);
		bots.load();
		// Load channels
		channels = new ChannelConfiguration(this);
		channels.load();
		// Load chatters
		chatters = new ChatterConfiguration(this);
		chatters.load();
		// Load rest of channel data
		channels.postLoad();
		// Load date formats
		dateHandler.init();
		// Initialize chat logger
		logger.start();
		// Register events
		Spout.getEventManager().registerEvents(new LocalChatHandler(this), this);
		// Register commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());	
		getEngine().getRootCommand().addSubCommands(this, ChannelCommand.class, commandRegFactory);
		getEngine().getRootCommand().addSubCommands(this, ChatCommands.class, commandRegFactory);
		// Add default permissions
		DefaultPermissionNodes nodes = new DefaultPermissionNodes();
		for (String node : nodes.get()) {
			DefaultPermissions.addDefaultPermission(node);
		}
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " enabled!");
	}

	@Override
	public void onDisable() {
		// Save logged messages
		logger.stop();
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " disabled.");
	}
}
