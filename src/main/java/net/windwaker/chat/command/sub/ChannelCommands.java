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
package net.windwaker.chat.command.sub;

import java.util.ArrayList;
import java.util.List;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

public class ChannelCommands {
	private final WindChat plugin = WindChat.getInstance();

	@Command(aliases = "create", usage = "<channel>", desc = "Create a new channel.", min = 1, max = 1)
	@CommandPermissions("windchat.command.channel.create")
	public void create(CommandContext args, CommandSource source) throws CommandException {
		String channelName = args.getString(0);
		plugin.getChannels().add(args.getString(0));
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Added channel '" + channelName + "'.");
	}

	@Command(aliases = {"join", "j"}, desc = "Join a channel", min = 1, max = 2)
	@CommandPermissions("windchat.command.channel.join")
	public void join(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command!");
		}

		Player player = (Player) source;
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick("Error: Chatter was null");
			throw new CommandException("Chatter was null");
		}

		Channel channel = plugin.getChannels().get(args.getString(0));
		if (channel == null) {
			throw new CommandException("Channel doesn't exist!");
		}

		if (channel.equals(chatter.getActiveChannel())) {
			throw new CommandException("You are already in " + channel.getName() + "!");
		}

		if (args.length() == 1) {
			if (channel.hasPassword()) {
				throw new CommandException(channel.getName() + " requires a password for entrance!");
			}
			chatter.join(channel);
		}

		if (args.length() == 2) {
			if (channel.hasPassword() && !channel.getPassword().equalsIgnoreCase(args.getString(1))) {
				throw new CommandException("Access denied.");
			}
			chatter.join(channel);
		}
	}

	@Command(aliases = {"leave", "l"}, desc = "Leave a channel", min = 1, max = 1)
	@CommandPermissions("windchat.command.channel.leave")
	public void leave(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command!");
		}

		Player player = (Player) source;
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Chatter was null");
		}

		Channel channel = plugin.getChannels().get(args.getString(0));
		if (channel == null) {
			throw new CommandException("Channel doesn't exist!");
		}

		if (channel.equals(chatter.getActiveChannel())) {
			throw new CommandException("You may not leave the channel you are active in!");
		}

		if (!chatter.getChannels().contains(channel)) {
			throw new CommandException("You are not in " + channel.getName() + "!");
		}
		chatter.leave(channel);
	}

	@Command(aliases = {"who", "players"}, usage = "[channel]", desc = "List all listeners in a channel", min = 0, max = 1)
	@CommandPermissions("windchat.command.channel.who")
	public void who(CommandContext args, CommandSource source) throws CommandException {
		Channel channel = null;
		if (args.length() == 0) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to list.");
			}
			Player player = (Player) source;
			Chatter chatter = plugin.getChatters().get(player.getName());
			if (chatter == null) {
				player.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Chatter was null");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 1) {
			String channelName = args.getString(0);
			channel = plugin.getChannels().get(channelName);
			if (channel == null) {
				throw new CommandException("Unknown channel '" + channelName + "'.");
			}
		}

		if (channel == null) {
			throw new CommandException("Channel not found.");
		}

		List<String> listeners = new ArrayList<String>(channel.getListeners());
		ChatArguments message = new ChatArguments(ChatStyle.BRIGHT_GREEN, channel.getName(), " (", ChatStyle.BLUE, listeners.size(), ChatStyle.BRIGHT_GREEN, "): ");
		for (int i = 0; i < listeners.size(); i++) {
			message.append(ChatStyle.BRIGHT_GREEN, listeners.get(i));
			if (i != listeners.size() - 1) {
				message.append(ChatStyle.WHITE, ",");
			}
		}
		source.sendMessage(message);
	}
}
