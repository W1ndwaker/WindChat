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
package net.windwaker.chat.command;

import java.util.ArrayList;
import java.util.List;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;
import net.windwaker.chat.util.Placeholders;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.Placeholder;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

/**
 * A collection of general chat commands.
 */
public class ChatCommands {
	private final WindChat plugin = WindChat.getInstance();

	@Command(aliases = "nick", usage = "<name|off>", desc = "Change your nickname.", min = 1, max = 1)
	@CommandPermissions("windchat.nick")
	public void nick(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command.");
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
		source.sendMessage(ChatStyle.BLUE, ChatStyle.BOLD, "ping");
		source.sendMessage(ChatStyle.GRAY, ChatStyle.ITALIC, "noun");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "1. ", ChatStyle.WHITE, "a short high-pitched resonant sound, as of a bullet striking metal or a sonar echo");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "2. ", ChatStyle.WHITE, "computing  a system for testing whether internet systems are responding and how long in milliseconds it takes them to respond");
		source.sendMessage(ChatStyle.GRAY, ChatStyle.ITALIC, "verb");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "3. ", ChatStyle.WHITE, "to make such a noise");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "4. ", ChatStyle.WHITE, "computing  to send a test message to (a computer or server) in order to check whether it is responding or how long it takes it to respond");
		source.sendMessage(ChatStyle.GRAY, ChatStyle.ITALIC, "origin");
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "1850-55; imitative.");
	}

	@Command(aliases = "quit", usage = "[message]", desc = "Disconnect from the server.", min = 0)
	@CommandPermissions("windchat.quit")
	public void quit(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You can only perform this command as a player.");
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
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "The date is ", plugin.getFormattedDate(), " ", plugin.getFormattedTime(), " ", plugin.getTimeZone().getDisplayName());
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
}
