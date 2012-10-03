/* Copyright (c) 2012 Walker Crouse, <http://windwaker.net/>
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
package net.windwaker.chat.cmd;

import java.util.ArrayList;
import java.util.List;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.chan.Chatter;
import net.windwaker.chat.cmd.sub.ChannelCommands;
import net.windwaker.chat.handler.DateHandler;
import net.windwaker.chat.util.Placeholders;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.Placeholder;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

/**
 * A collection of general chat commands.
 */
public class ChatCommands {
	private final WindChat plugin;

	public ChatCommands(WindChat plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = "nick", usage = "<name|off>", desc = "Change your nickname.", min = 1, max = 1)
	@CommandPermissions("windchat.nick")
	public void nick(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this cmd.");
		}
		String name = args.getString(0);
		Player player = (Player) source;
		if (name.equalsIgnoreCase("off")) {
			name = player.getName();
		}
		player.setDisplayName(name);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Set nick to '", name, "'.");
	}

	@Command(aliases = "whois", usage = "<player>", desc = "Get more information about a player.", min = 1, max = 1)
	@CommandPermissions("windchat.whois")
	public void whoIs(CommandContext args, CommandSource source) throws CommandException {
		Player player = args.getPlayer(0, false);
		String playerName = player.getName();
		source.sendMessage(ChatStyle.BRIGHT_GREEN, playerName, " is ", ChatStyle.BLUE, playerName, ChatStyle.BRIGHT_GREEN, "@", ChatStyle.BLUE, player.getAddress().getHostAddress());
		Chatter chatter = plugin.getChatters().get(playerName);
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Error: An internal error occurred.");
		}
		ChatArguments message = new ChatArguments(ChatStyle.BRIGHT_GREEN, player.getName(), " on ");
		List<Channel> channels = new ArrayList<Channel>(chatter.getChannels());
		for (int i = 0; i < channels.size(); i++) {
			message.append(ChatStyle.BLUE, channels.get(i).getName());
			if (i != channels.size() - 1) {
				message.append(ChatStyle.WHITE, ",");
			}
		}
		source.sendMessage(message);
	}

	@Command(aliases = "ping", desc = "Test your connection.", min = 0, max = 0)
	@CommandPermissions("windchat.ping")
	public void ping(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Pong!");
	}

	@Command(aliases = "quit", usage = "[message]", desc = "Disconnect from the server.", min = 0)
	@CommandPermissions("windchat.quit")
	public void quit(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You can only perform this cmd as a player.");
		}
		Player player = (Player) source;
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter != null && args.length() > 0) {
			chatter.setQuitMessage(args.getJoinedString(0));
		}
		player.kick(ChatStyle.BRIGHT_GREEN, "Disconnected from server.");
	}

	@Command(aliases = {"date", "cal", "calendar"}, desc = "Gets the time of the hosted server.", min = 0, max = 0)
	@CommandPermissions("windchat.date")
	public void date(CommandContext args, CommandSource source) throws CommandException {
		DateHandler dateHandler = plugin.getDateHandler();
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "The date is ", dateHandler.getFormattedDate(), " ", dateHandler.getFormattedTime(), " ", dateHandler.getTimeZone().getDisplayName());
	}

	@Command(aliases = "styles", desc = "List all chat styles.", min = 0, max = 0)
	@CommandPermissions("windchat.styles")
	public void styles(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage(ChatStyle.YELLOW, "Tip: ", ChatStyle.ITALIC, " Styles are always surrounded by two sets of curly brackets. (ie {{DARK_AQUA}})");
		for (ChatStyle style : ChatStyle.getValues()) {
			source.sendMessage(style, style.getLookupName());
		}
	}

	@Command(aliases = "placeholders", desc = "List all the placeholders.", min = 0, max = 0)
	@CommandPermissions("windchat.placeholders")
	public void placeholders(CommandContext args, CommandSource source) throws CommandException {
		source.sendMessage(ChatStyle.YELLOW, "Tip: ", ChatStyle.ITALIC, " Placeholders are always surrounded by one set of curly brackets. (ie {NAME})");
		for (Placeholder placeholder : Placeholders.getValues()) {
			source.sendMessage(placeholder.getName());
		}
	}

	@Command(aliases = {"query", "pm", "tell"}, usage = "<player> <message>", desc = "Sends a message to the specified player.", min = 2)
	@CommandPermissions("windchat.query")
	public void query(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command.");
		}
		Chatter chatter = getChatter(plugin, args.getPlayer(0, false));
		Chatter sender = getChatter(plugin, (Player) source);
		chatter.sendPrivateMessage(sender, new ChatArguments(args.getJoinedString(1)));
	}

	@Command(aliases = {"reply", "r"}, usage = "<message>", desc = "Quickly reply to the last person you received a private message from.", min = 1)
	@CommandPermissions("windchat.reply")
	public void reply(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command.");
		}
		Chatter sender = getChatter(plugin, (Player) source);
		if (sender.getLastSender() == null) {
			throw new CommandException("No one to send to.");
		}
		sender.reply(new ChatArguments(args.getJoinedString(0)));
	}

	@Command(aliases = {"channel", "ch"}, desc = "Parent cmd for WindChat")
	@CommandPermissions("windchat.cmd.channel")
	@NestedCommand(ChannelCommands.class)
	public void channel(CommandContext args, CommandSource source) throws CommandException {
	}

	public static Chatter getChatter(WindChat plugin, Player player) throws CommandException {
		Chatter chatter = plugin.getChatters().get(player.getName());
		if (chatter == null) {
			player.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Error: Chatter was null!");
		}
		return chatter;
	}

	public static Channel getChannel(WindChat plugin, CommandContext args, CommandSource source, int length) throws CommandException {
		Channel channel = null;
		if (args.length() == length) {
			channel = getActiveChannel(plugin, source);
		}
		if (args.length() == length + 1) {
			channel = getChannel(plugin, args, 1);
		}
		return channel;
	}

	public static Channel getChannel(WindChat plugin, CommandContext args, int index) throws CommandException {
		Channel channel = plugin.getChannels().get(args.getString(index));
		if (channel == null) {
			throw new CommandException("Channel not found!");
		}
		return channel;
	}

	public static Channel getActiveChannel(WindChat plugin, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Please specify a channel to mute the player in.");
		}
		Player p = (Player) source;
		Chatter chatter = plugin.getChatters().get(p.getName());
		if (chatter == null) {
			p.kick(ChatStyle.RED, "Error: An internal error occurred.");
			throw new CommandException("Error: Chatter was null!");
		}
		Channel channel = chatter.getActiveChannel();
		if (channel == null) {
			throw new CommandException("Channel not found!");
		}
		return channel;
	}

	public static void checkPermission(CommandSource source, String node) throws CommandException {
		if (!source.hasPermission(node)) {
			throw new CommandException("You don't have permission to do that!");
		}
	}
}
