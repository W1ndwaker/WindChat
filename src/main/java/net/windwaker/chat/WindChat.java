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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.windwaker.chat.cmd.ChannelCommand;
import net.windwaker.chat.cmd.ChatCommands;
import net.windwaker.chat.util.DefaultPermissionNodes;
import net.windwaker.chat.util.config.ChannelConfiguration;
import net.windwaker.chat.util.config.ChatConfiguration;
import net.windwaker.chat.util.config.ChatterConfiguration;

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
	/**
	 * Static instance of the plugin. {@link net.windwaker.chat.WindChat#getInstance()} is not safe until {@link net.windwaker.chat.WindChat#onEnable()} is called.
	 */
	private static WindChat instance;
	/**
	 * Represents the general configuration of the plugin.
	 */
	private ChatConfiguration config;
	/**
	 * Represents the collection of {@link net.windwaker.chat.channel.Chatter}s in the plugin.
	 */
	private ChatterConfiguration chatters;
	/**
	 * Represents the collection of {@link net.windwaker.chat.channel.Channel}s in the plugin.
	 */
	private ChannelConfiguration channels;
	/**
	 * The configured {@link DateFormat} for the date in {@link ChatConfiguration}.
	 */
	private DateFormat dateFormat;
	/**
	 * The configured {@link DateFormat} for the time in {@link ChatConfiguration}.
	 */
	private DateFormat timeFormat;

	/**
	 * Constructs a new WindChat object.
	 */
	public WindChat() {
		instance = this;
	}

	/**
	 * Gets the static instance of {@link WindChat}. Note: this method is only safe after {@link net.windwaker.chat.WindChat#onEnable()} has been called.
	 * @return singleton instance
	 */
	public static WindChat getInstance() {
		return instance;
	}

	/**
	 * Gets the collection of {@link net.windwaker.chat.channel.Chatter}s on the plugin.
	 * @return collection of chatters
	 */
	public ChatterConfiguration getChatters() {
		return chatters;
	}

	/**
	 * Gets the collection of {@link net.windwaker.chat.channel.Channel}s on the plugin.
	 * @return collection of channels
	 */
	public ChannelConfiguration getChannels() {
		return channels;
	}

	/**
	 * Gets the formatted date as configured in {@link ChatConfiguration}.
	 * @return configured formatted date
	 */
	public String getFormattedDate() {
		return dateFormat.format(getTime());
	}

	/**
	 * Gets the formatted time as configured in {@link ChatConfiguration}.
	 * @return configured formatted time
	 */
	public String getFormattedTime() {
		return timeFormat.format(getTime());
	}

	/**
	 * Gets the configured {@link TimeZone} of the plugin.
	 * @return time zone
	 */
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(ChatConfiguration.TIME_ZONE.getString());
	}

	/**
	 * Gets an instance of a calendar from the configured {@link TimeZone}
	 * @return calendar from time zone
	 */
	public Calendar getCalendar() {
		return Calendar.getInstance(getTimeZone());
	}

	/**
	 * Gets the current time of the configured {@link TimeZone}.
	 * @return
	 */
	public Date getTime() {
		return getCalendar().getTime();
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
		config = new ChatConfiguration();
		config.load();
		// Load channels
		channels = new ChannelConfiguration();
		channels.load();
		// Load chatters
		chatters = new ChatterConfiguration();
		chatters.load();
		// Load date formats
		dateFormat = new SimpleDateFormat(ChatConfiguration.DATE_FORMAT.getString());
		timeFormat = new SimpleDateFormat(ChatConfiguration.TIME_FORMAT.getString());
		// Register events
		Spout.getEventManager().registerEvents(new ChatListener(), this);
		// Register commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(), new SimpleAnnotatedCommandExecutorFactory());
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
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " disabled.");
	}
}
