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

import net.windwaker.chat.command.ChannelCommand;
import net.windwaker.chat.command.ChatCommands;
import net.windwaker.chat.util.Format;
import net.windwaker.chat.util.config.ChannelConfiguration;
import net.windwaker.chat.util.config.ChatConfiguration;
import net.windwaker.chat.util.config.ChatterConfiguration;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.data.ValueHolder;
import org.spout.api.entity.Player;
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
		// Register events
		Spout.getEventManager().registerEvents(new ChatListener(), this);
		// Register commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(), new SimpleAnnotatedCommandExecutorFactory());
		getEngine().getRootCommand().addSubCommands(this, ChannelCommand.class, commandRegFactory);
		getEngine().getRootCommand().addSubCommands(this, ChatCommands.class, commandRegFactory);
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("WindChat " + getDescription().getVersion() + " by " + getDescription().getAuthors() + " disabled.");
	}

	public static WindChat getInstance() {
		return instance;
	}

	public ChatArguments getFormat(Format format, Player player) {
		ChatArguments def = format.getDefault();
		ValueHolder data = player.getData(format.toString());
		if (data != null && data.getString() != null) {
			def = ChatArguments.fromFormatString(data.getString());
		}
		return def;
	}

	public ChatterConfiguration getChatters() {
		return chatters;
	}

	public ChannelConfiguration getChannels() {
		return channels;
	}
}
