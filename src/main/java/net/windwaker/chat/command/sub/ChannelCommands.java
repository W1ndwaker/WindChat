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

/**
 * A collection of general channel commands.
 */
public class ChannelCommands {
	private final WindChat plugin = WindChat.getInstance();

	@Command(aliases = "create", usage = "<channel>", desc = "Create a new channel.", min = 1, max = 1)
	@CommandPermissions("windchat.channel.create")
	public void create(CommandContext args, CommandSource source) throws CommandException {
		String channelName = args.getString(0);
		plugin.getChannels().add(args.getString(0));
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Added channel '", channelName, "'.");
	}

	@Command(aliases = "mute", usage = "<player> [channel]", desc = "Mute a player in a channel.", min = 1, max = 2)
	public void mute(CommandContext args, CommandSource source) throws CommandException {
		String playerName = args.getString(0);
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to mute the player in.");
			}
			Player p = (Player) source;
			Chatter chatter = plugin.getChatters().get(p.getName());
			if (chatter == null) {
				p.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 2) {
			channel = plugin.getChannels().get(args.getString(1));
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		if (!source.hasPermission("windchat.mute" + channel.getName())) {
			throw new CommandException("You don't have permission to mute player's in this channel!");
		}
		channel.mute(playerName);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Muted player '", playerName, "' from channel '", channel.getName(), "'.");
	}

	@Command(aliases = "unmute", usage = "<player> [channel]", desc = "Unmute a player in a channel.", min = 1, max = 2)
	public void unmute(CommandContext args, CommandSource source) throws CommandException {
		String playerName = args.getString(0);
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to unmute the player in.");
			}
			Player p = (Player) source;
			Chatter chatter = plugin.getChatters().get(p.getName());
			if (chatter == null) {
				p.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 2) {
			channel = plugin.getChannels().get(args.getString(1));
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		if (!source.hasPermission("windchat.umute" + channel.getName())) {
			throw new CommandException("You don't have permission to unmute player's in this channel!");
		}
		channel.unmute(playerName);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Unmuted player '", playerName, "' from channel '", channel.getName(), "'.");
	}

	@Command(aliases = "ban", usage = "<player> [channel|reason] [reason]", desc = "Ban a player from a channel.", min = 1)
	public void ban(CommandContext args, CommandSource source) throws CommandException {
		String playerName = args.getString(0);
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to ban the player from.");
			}
			Player player = (Player) source;
			Chatter chatter = plugin.getChatters().get(player.getName());
			if (chatter == null) {
				player.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = chatter.getActiveChannel();
		}

		ChatArguments reason = null;
		if (args.length() > 1) {
			Channel c = plugin.getChannels().get(args.getString(1));
			if (c == null) {
				reason = args.getJoinedString(1);
			} else {
				channel = c;
			}
		}

		if (args.length() == 3) {
			reason = args.getJoinedString(2);
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		ChatArguments message = new ChatArguments(ChatStyle.RED, "Banned from channel '" + channel.getName() + "'");
		if (reason != null) {
			message.append(": ", reason);
		}

		if (channel.equals(plugin.getChannels().getDefault())) {
			throw new CommandException("You cannot ban a player from the default channel.");
		}

		if (!source.hasPermission("windchat.ban." + channel.getName())) {
			throw new CommandException("You don't have permission to ban from this channel!");
		}
		channel.ban(playerName, true, message);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Player '", playerName, "' banned from channel '", channel.getName(), "'.");
	}

	@Command(aliases = "unban", usage = "<player> [channel]", desc = "Unban a player from a channel.", min = 1, max = 2)
	public void unban(CommandContext args, CommandSource source) throws CommandException {
		String playerName = args.getString(0);
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to unban the player from.");
			}
			Player player = (Player) source;
			Chatter chatter = plugin.getChatters().get(player.getName());
			if (chatter == null) {
				player.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 2) {
			channel = plugin.getChannels().get(args.getString(1));
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		if (!source.hasPermission("windchat.unban." + channel.getName())) {
			throw new CommandException("You don't have permission to unban players from this channel!");
		}
		channel.unban(playerName);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Player '", playerName, "' unbanned from channel '", channel.getName(), "'.");
	}

	@Command(aliases = "kick", usage = "<player> [channel|reason] [reason]", desc = "Kick a player from a channel.", min = 1)
	public void kick(CommandContext args, CommandSource source) throws CommandException {
		Player player = args.getPlayer(0, false);
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Error: Chatter was null!");
		}
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to kick the player from.");
			}
			Player p = (Player) source;
			Chatter c = plugin.getChatters().get(p.getName());
			if (c == null) {
				p.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = c.getActiveChannel();
		}

		ChatArguments reason = null;
		if (args.length() > 1) {
			Channel c = plugin.getChannels().get(args.getString(1));
			if (c == null) {
				reason = args.getJoinedString(1);
			} else {
				channel = c;
			}
		}

		if (args.length() == 3) {
			reason = args.getJoinedString(2);
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		ChatArguments message = new ChatArguments(ChatStyle.RED, "Kicked from channel '" + channel.getName() + "'");
		if (reason != null) {
			message.append(": ", reason);
		}

		if (channel.equals(plugin.getChannels().getDefault())) {
			throw new CommandException("You cannot kick a player from the default channel.");
		}

		if (!source.hasPermission("windchat.kick." + channel.getName())) {
			throw new CommandException("You don't have permission to kick from this channel!");
		}
		chatter.kick(channel, message);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Kicked player '", player.getName(), "' from channel '", channel.getName(), "'.");
	}

	@Command(aliases = "invite", usage = "<player> [channel]", desc = "Invite a player to a channel.", min = 1, max = 2)
	public void invite(CommandContext args, CommandSource source) throws CommandException {
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to invite the player to.");
			}
			Player player = (Player) source;
			Chatter chatter = plugin.getChatters().get(player.getName());
			if (chatter == null) {
				player.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Error: Chatter was null!");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 2) {
			channel = plugin.getChannels().get(args.getString(1));
		}

		if (channel == null) {
			throw new CommandException("Channel not found!");
		}

		Player player = args.getPlayer(0, false);
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Error: Chatter was null");
		}

		if (!source.hasPermission("windchat.invite." + channel.getName())) {
			throw new CommandException("You don't have permission to invite people to this channel!");
		}

		if (channel.isInviteOnly()) {
			chatter.invite(channel);
		}
		String channelName = channel.getName();
		player.sendMessage(ChatStyle.BRIGHT_GREEN, "You have been invited to channel '", channelName, "'. Use '/join ", channelName, "' to join.");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "You invited player '", player.getName(), "' to join channel '", channelName, "'.");
	}

	@Command(aliases = {"join", "j"}, usage = "<channel>", desc = "Join a channel", min = 1, max = 2)
	public void join(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command!");
		}

		Player player = (Player) source;
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred");
			throw new CommandException("Error: Chatter was null");
		}

		Channel channel = plugin.getChannels().get(args.getString(0));
		if (channel == null) {
			throw new CommandException("Channel doesn't exist!");
		}

		if (channel.equals(chatter.getActiveChannel())) {
			throw new CommandException("You are already in " + channel.getName() + "!");
		}

		if (channel.isBanned(chatter.getParent().getName())) {
			chatter.getParent().sendMessage(channel.getBanMessage());
			return;
		}

		if (channel.isInviteOnly() && !chatter.isInvitedTo(channel)) {
			throw new CommandException("Channel '" + channel.getName() + "' is by invite only!");
		}

		if (!player.hasPermission("windchat.join." + channel.getName())) {
			throw new CommandException("You don't have permission to join " + channel.getName() + "!");
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

	@Command(aliases = {"leave", "l"}, usage = "<channel>", desc = "Leave a channel", min = 1, max = 1)
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

		if (!player.hasPermission("windchat.leave." + channel.getName())) {
			throw new CommandException("You don't have permission to leave " + channel.getName() + "!");
		}
		chatter.leave(channel);
	}

	@Command(aliases = {"who", "players", "users"}, usage = "[channel]", desc = "List all listeners in a channel", min = 0, max = 1)
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

		if (!source.hasPermission("windchat.who" + channel.getName())) {
			throw new CommandException("You don't have permission to view the player list in this channel!");
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

	@Command(aliases = {"pass", "password"}, usage = "<password|off> [channel]", desc = "Sets the password for the channel.", min = 1, max = 2)
	public void pass(CommandContext args, CommandSource source) throws CommandException {
		String pass = args.getString(0);
		if (pass.equalsIgnoreCase("off")) {
			pass = null;
		}
		Channel channel = null;
		if (args.length() == 1) {
			if (!(source instanceof Player)) {
				throw new CommandException("Please specify a channel to set the password of.");
			}
			Player player = (Player) source;
			Chatter chatter = plugin.getChatters().get(player.getName());
			if (chatter == null) {
				player.kick(ChatStyle.RED, "Error: An internal error occurred.");
				throw new CommandException("Chatter was null");
			}
			channel = chatter.getActiveChannel();
		}

		if (args.length() == 2) {
			channel = plugin.getChannels().get(args.getString(1));
		}

		if (channel == null) {
			throw new CommandException("Channel not found.");
		}

		if (!source.hasPermission("windchat.pass." + channel.getName())) {
			throw new CommandException("You don't have permission to set the password on this channel.");
		}
		channel.setPassword(pass);
		ChatArguments message = new ChatArguments();
		if (pass != null) {
			message.append("Set password to '", pass, "'.");
		} else {
			message.append("Password toggled off.");
		}
		source.sendMessage(message);
	}

	@Command(aliases = "list", usage = "[#]", desc = "Lists all channels on the server.", min = 0, max = 1)
	@CommandPermissions("windchat.list")
	public void list(CommandContext args, CommandSource source) throws CommandException {
		List<Channel> channels = new ArrayList<Channel>(plugin.getChannels().get());
		int page = args.getInteger(0, 1);
		int index = (page - 1) * 10;
		if (index >= channels.size() || index < 0) {
			throw new CommandException("Invalid page. Pages: " + ((channels.size() / 10) + 1));
		}
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "----------", ChatStyle.WHITE, " [", ChatStyle.CYAN, ChatStyle.BOLD, "Channels - Page ", page, ChatStyle.RESET, "] ", ChatStyle.BRIGHT_GREEN, "----------");
		for (int i = index; i < index + 9; i++) {
			if (i >= channels.size()) {
				return;
			}
			source.sendMessage(ChatStyle.BLUE, channels.get(i).getName());
		}
	}
}
