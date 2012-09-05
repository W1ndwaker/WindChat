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

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

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
}
