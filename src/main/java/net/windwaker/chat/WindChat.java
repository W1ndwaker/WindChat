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

import net.windwaker.chat.command.ChannelCommand;
import net.windwaker.chat.command.ChatCommands;
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
 * Chat plugin for the Spout voxel software.
 * @author Windwaker
 */
public class WindChat extends CommonPlugin {
	private static WindChat instance;
	private ChatConfiguration config;
	private ChatterConfiguration chatters;
	private ChannelConfiguration channels;
	private DateFormat dateFormat;
	private DateFormat timeFormat;

	public WindChat() {
		instance = this;
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
		DefaultPermissions.addDefaultPermission("windchat.join.*");
		DefaultPermissions.addDefaultPermission("windchat.leave.*");
		DefaultPermissions.addDefaultPermission("windchat.chat.*");
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " disabled.");
	}

	public static WindChat getInstance() {
		return instance;
	}

	public ChatterConfiguration getChatters() {
		return chatters;
	}

	public ChannelConfiguration getChannels() {
		return channels;
	}

	public String getFormattedDate() {
		return dateFormat.format(getTime());
	}

	public String getFormattedTime() {
		return timeFormat.format(getTime());
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(ChatConfiguration.TIME_ZONE.getString());
	}

	public Calendar getCalendar() {
		return Calendar.getInstance(getTimeZone());
	}

	public Date getTime() {
		return getCalendar().getTime();
	}
}
